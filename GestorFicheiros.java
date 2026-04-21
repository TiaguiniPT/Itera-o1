package io;

import modelo.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Classe responsável pela leitura e validação de dados a partir de ficheiros CSV.
 */
public class GestorFicheiros {

    /** Limiar mínimo de capacidade de camas válida. */
    private static final int CAPACIDADE_MINIMA = 1;

    /**
     * Regista uma mensagem de erro no ficheiro de log.
     *
     * @param msg mensagem de erro a registar
     */
    private static void logErro(String msg) {
        try (PrintWriter out = new PrintWriter(new FileWriter("erros_validacao.log", true))) {
            out.println("[ERRO] " + LocalDate.now() + ": " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Valida se uma string não é nula nem vazia.
     *
     * @param valor string a validar
     * @return true se válida, false caso contrário
     */
    private static boolean validarString(String valor) {
        return valor != null && !valor.isBlank();
    }

    /**
     * Valida se uma capacidade é válida (maior ou igual ao mínimo).
     *
     * @param capacidade valor a validar
     * @return true se válida, false caso contrário
     */
    private static boolean validarCapacidade(int capacidade) {
        return capacidade >= CAPACIDADE_MINIMA;
    }

    /**
     * Carrega enfermarias a partir de um ficheiro CSV e adiciona-as ao hospital.
     * Formato esperado: TIPO;ID;CAPACIDADE;[campos adicionais por tipo]
     * Tipos suportados: GERAL, PSIQUIATRICA, INTENSIVOS
     * Entradas inválidas são registadas no log.
     *
     * @param path caminho para o ficheiro CSV
     * @param h    hospital onde as enfermarias serão adicionadas
     */
    public static void carregarEnfermarias(String path, Hospital h) {
        try (Scanner sc = new Scanner(new File(path))) {
            if (sc.hasNextLine()) sc.nextLine();

            int linha = 1;
            while (sc.hasNextLine()) {
                linha++;
                String[] d = sc.nextLine().split(";");

                try {
                    if (d.length < 3) {
                        logErro("Linha " + linha + ": campos insuficientes.");
                        continue;
                    }

                    String tipo = d[0].trim().toUpperCase();
                    String id = d[1].trim();
                    int cap = Integer.parseInt(d[2].trim());

                    if (!validarString(id)) {
                        logErro("Linha " + linha + ": identificador inválido.");
                        continue;
                    }
                    if (!validarCapacidade(cap)) {
                        logErro("Linha " + linha + ": capacidade inválida (" + cap + ").");
                        continue;
                    }

                    if (tipo.equals("GERAL")) {
                        if (d.length < 4) {
                            logErro("Linha " + linha + ": GERAL requer acompanhantes.");
                            continue;
                        }
                        int acomp = Integer.parseInt(d[3].trim());
                        if (acomp < 0) {
                            logErro("Linha " + linha + ": número de acompanhantes inválido.");
                            continue;
                        }
                        EnfermariaGeral eg = new EnfermariaGeral(id, cap, acomp, "09h-20h");
                        for (int i = 4; i < d.length; i++) {
                            if (validarString(d[i])) eg.adicionarRecurso(d[i].trim());
                        }
                        h.adicionarEnfermaria(eg);

                    } else if (tipo.equals("PSIQUIATRICA")) {
                        if (d.length < 5) {
                            logErro("Linha " + linha + ": PSIQUIATRICA requer horário e nível de segurança.");
                            continue;
                        }
                        String horarioPsi = d[3].trim();
                        String seguranca = d[4].trim();
                        if (!validarString(horarioPsi) || !validarString(seguranca)) {
                            logErro("Linha " + linha + ": horário ou nível de segurança inválido.");
                            continue;
                        }
                        h.adicionarEnfermaria(new EnfermariaPsiquiatrica(id, cap, horarioPsi, seguranca));

                    } else if (tipo.equals("INTENSIVOS")) {
                        if (d.length < 6) {
                            logErro("Linha " + linha + ": INTENSIVOS requer horário, pressão e pressão de referência.");
                            continue;
                        }
                        String horarioInt = d[3].trim();
                        double pressao = Double.parseDouble(d[4].trim());
                        double pressaoRef = Double.parseDouble(d[5].trim());
                        if (!validarString(horarioInt) || pressao <= 0 || pressaoRef <= 0) {
                            logErro("Linha " + linha + ": dados de INTENSIVOS inválidos.");
                            continue;
                        }
                        h.adicionarEnfermaria(new EnfermariaCuidadosIntensivos(id, cap, horarioInt, pressao, pressaoRef));

                    } else {
                        logErro("Linha " + linha + ": tipo de enfermaria desconhecido (" + tipo + ").");
                    }

                } catch (NumberFormatException e) {
                    logErro("Linha " + linha + ": erro de formato numérico — " + e.getMessage());
                } catch (Exception e) {
                    logErro("Linha " + linha + ": erro inesperado — " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Ficheiro não encontrado: " + path);
        }
    }

    /**
     * Carrega episódios a partir de um ficheiro CSV e associa-os às enfermarias do hospital.
     * Formato esperado: ID_ENFERMARIA;ID_CAMA;DATA_ADMISSAO;DATA_ALTA (opcional)
     * Datas no formato YYYY-MM-DD. Entradas inválidas são registadas no log.
     *
     * @param path caminho para o ficheiro CSV
     * @param h    hospital com as enfermarias já carregadas
     */
    public static void carregarEpisodios(String path, Hospital h) {
        try (Scanner sc = new Scanner(new File(path))) {
            if (sc.hasNextLine()) sc.nextLine();

            int linha = 1;
            while (sc.hasNextLine()) {
                linha++;
                String[] d = sc.nextLine().split(";");

                try {
                    if (d.length < 3) {
                        logErro("Linha " + linha + ": campos insuficientes no episódio.");
                        continue;
                    }

                    String idEnfermaria = d[0].trim();
                    String idCama = d[1].trim();
                    LocalDate admissao = LocalDate.parse(d[2].trim());

                    if (!validarString(idEnfermaria)) {
                        logErro("Linha " + linha + ": ID de enfermaria inválido.");
                        continue;
                    }
                    if (!validarString(idCama)) {
                        logErro("Linha " + linha + ": ID de cama inválido.");
                        continue;
                    }

                    Enfermaria enf = h.obterEnfermaria(idEnfermaria);
                    if (enf == null) {
                        logErro("Linha " + linha + ": enfermaria não encontrada (" + idEnfermaria + ").");
                        continue;
                    }

                    Episodio ep = new Episodio(idCama, admissao);

                    if (d.length >= 4 && validarString(d[3])) {
                        LocalDate alta = LocalDate.parse(d[3].trim());
                        if (!alta.isAfter(admissao)) {
                            logErro("Linha " + linha + ": data de alta não pode ser anterior ou igual à admissão.");
                            continue;
                        }
                        ep.darAlta(alta);
                    }

                    enf.adicionarEpisodio(ep);

                } catch (DateTimeParseException e) {
                    logErro("Linha " + linha + ": formato de data inválido — " + e.getMessage());
                } catch (Exception e) {
                    logErro("Linha " + linha + ": erro inesperado — " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Ficheiro não encontrado: " + path);
        }
    }
}
