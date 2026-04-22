package io;

import modelo.*;
import java.io.*;
import java.time.LocalDate;
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
    private static void logErro(String msg) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter("erros_validacao.log", true));
        out.println("[ERRO] " + LocalDate.now() + ": " + msg);
        out.close();
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
     * Valida se uma string representa um inteiro válido.
     *
     * @param valor string a validar
     * @return true se for um inteiro válido, false caso contrário
     */
    private static boolean validarInteiro(String valor) {
        if (!validarString(valor)) return false;
        for (char c : valor.trim().toCharArray()) {
            if (!Character.isDigit(c) && c != '-') return false;
        }
        return true;
    }

    /**
     * Valida se uma string representa um número decimal válido.
     *
     * @param valor string a validar
     * @return true se for um decimal válido, false caso contrário
     */
    private static boolean validarDecimal(String valor) {
        if (!validarString(valor)) return false;
        int pontos = 0;
        for (char c : valor.trim().toCharArray()) {
            if (c == '.') pontos++;
            else if (!Character.isDigit(c) && c != '-') return false;
        }
        return pontos <= 1;
    }

    /**
     * Valida se uma string representa uma data no formato YYYY-MM-DD.
     *
     * @param valor string a validar
     * @return true se for uma data válida, false caso contrário
     */
    private static boolean validarData(String valor) {
        if (!validarString(valor)) return false;
        String[] partes = valor.trim().split("-");
        if (partes.length != 3) return false;
        if (!validarInteiro(partes[0]) || !validarInteiro(partes[1]) || !validarInteiro(partes[2])) return false;
        int mes = Integer.parseInt(partes[1]);
        int dia = Integer.parseInt(partes[2]);
        return mes >= 1 && mes <= 12 && dia >= 1 && dia <= 31;
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
    public static void carregarEnfermarias(String path, Hospital h) throws IOException {
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("Ficheiro não encontrado: " + path);
            return;
        }

        Scanner sc = new Scanner(f);
        if (sc.hasNextLine()) sc.nextLine();

        int linha = 1;
        while (sc.hasNextLine()) {
            linha++;
            String[] d = sc.nextLine().split(";");

            if (d.length < 3) {
                logErro("Linha " + linha + ": campos insuficientes.");
                continue;
            }

            String tipo = d[0].trim().toUpperCase();
            String id = d[1].trim();
            String capStr = d[2].trim();

            if (!validarString(id)) {
                logErro("Linha " + linha + ": identificador inválido.");
                continue;
            }
            if (!validarInteiro(capStr)) {
                logErro("Linha " + linha + ": capacidade não é um número inteiro válido.");
                continue;
            }

            int cap = Integer.parseInt(capStr);
            if (!validarCapacidade(cap)) {
                logErro("Linha " + linha + ": capacidade inválida (" + cap + ").");
                continue;
            }

            if (tipo.equals("GERAL")) {
                if (d.length < 4 || !validarInteiro(d[3].trim())) {
                    logErro("Linha " + linha + ": GERAL requer número de acompanhantes válido.");
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
                if (d.length < 5 || !validarString(d[3]) || !validarString(d[4])) {
                    logErro("Linha " + linha + ": PSIQUIATRICA requer horário e nível de segurança.");
                    continue;
                }
                h.adicionarEnfermaria(new EnfermariaPsiquiatrica(id, cap, d[3].trim(), d[4].trim()));

            } else if (tipo.equals("INTENSIVOS")) {
                if (d.length < 6 || !validarString(d[3]) || !validarDecimal(d[4]) || !validarDecimal(d[5])) {
                    logErro("Linha " + linha + ": INTENSIVOS requer horário, pressão e pressão de referência válidos.");
                    continue;
                }
                double pressao = Double.parseDouble(d[4].trim());
                double pressaoRef = Double.parseDouble(d[5].trim());
                if (pressao <= 0 || pressaoRef <= 0) {
                    logErro("Linha " + linha + ": pressões devem ser positivas.");
                    continue;
                }
                h.adicionarEnfermaria(new EnfermariaCuidadosIntensivos(id, cap, d[3].trim(), pressao, pressaoRef));

            } else {
                logErro("Linha " + linha + ": tipo de enfermaria desconhecido (" + tipo + ").");
            }
        }
        sc.close();
    }

    /**
     * Carrega episódios a partir de um ficheiro CSV e associa-os às enfermarias do hospital.
     * Formato esperado: ID_ENFERMARIA;ID_CAMA;DATA_ADMISSAO;DATA_ALTA (opcional)
     * Datas no formato YYYY-MM-DD. Entradas inválidas são registadas no log.
     *
     * @param path caminho para o ficheiro CSV
     * @param h    hospital com as enfermarias já carregadas
     */
    public static void carregarEpisodios(String path, Hospital h) throws IOException {
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("Ficheiro não encontrado: " + path);
            return;
        }

        Scanner sc = new Scanner(f);
        if (sc.hasNextLine()) sc.nextLine();

        int linha = 1;
        while (sc.hasNextLine()) {
            linha++;
            String[] d = sc.nextLine().split(";");

            if (d.length < 3) {
                logErro("Linha " + linha + ": campos insuficientes no episódio.");
                continue;
            }

            String idEnfermaria = d[0].trim();
            String idCama = d[1].trim();
            String dataAdmissaoStr = d[2].trim();

            if (!validarString(idEnfermaria)) {
                logErro("Linha " + linha + ": ID de enfermaria inválido.");
                continue;
            }
            if (!validarString(idCama)) {
                logErro("Linha " + linha + ": ID de cama inválido.");
                continue;
            }
            if (!validarData(dataAdmissaoStr)) {
                logErro("Linha " + linha + ": data de admissão inválida.");
                continue;
            }

            Enfermaria enf = h.obterEnfermaria(idEnfermaria);
            if (enf == null) {
                logErro("Linha " + linha + ": enfermaria não encontrada (" + idEnfermaria + ").");
                continue;
            }

            LocalDate admissao = LocalDate.parse(dataAdmissaoStr);
            Episodio ep = new Episodio(idCama, admissao);

            if (d.length >= 4 && validarString(d[3])) {
                if (!validarData(d[3].trim())) {
                    logErro("Linha " + linha + ": data de alta inválida.");
                    continue;
                }
                LocalDate alta = LocalDate.parse(d[3].trim());
                if (!alta.isAfter(admissao)) {
                    logErro("Linha " + linha + ": data de alta não pode ser anterior ou igual à admissão.");
                    continue;
                }
                ep.darAlta(alta);
            }

            enf.adicionarEpisodio(ep);
        }
        sc.close();
    }
}
