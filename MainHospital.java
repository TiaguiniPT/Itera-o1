package main;

import io.GestorFicheiros;
import modelo.*;
import utils.AnalisadorEstatistico;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Classe principal da aplicação de monitorização de ocupação de camas por enfermaria.
 * Cria automaticamente um conjunto de objetos de teste e demonstra todas as funcionalidades.
 */
public class MainHospital {

    /**
     * Ponto de entrada da aplicação.
     *
     * @param args argumentos da linha de comandos (não utilizados)
     * @throws IOException se ocorrer erro na leitura de ficheiros
     */
    public static void main(String[] args) throws IOException {

        // -------------------------------------------------------
        // 1. CRIAR HOSPITAL
        // -------------------------------------------------------
        Hospital hospital = new Hospital("Hospital Central XYZ");
        System.out.println("=== " + hospital + " ===\n");

        // -------------------------------------------------------
        // 2. CRIAR ENFERMARIAS MANUALMENTE
        // -------------------------------------------------------
        EnfermariaGeral egeral = new EnfermariaGeral("G1", 4, 2, "09h-20h");
        egeral.adicionarRecurso("Ventilador");
        egeral.adicionarRecurso("Monitor Cardiaco");

        EnfermariaPsiquiatrica epsi = new EnfermariaPsiquiatrica("P1", 4, "14h-17h", "ALTO");

        EnfermariaCuidadosIntensivos euci = new EnfermariaCuidadosIntensivos("I1", 4, "10h-12h", 101.3, 101.0);

        hospital.adicionarEnfermaria(egeral);
        hospital.adicionarEnfermaria(epsi);
        hospital.adicionarEnfermaria(euci);

        // -------------------------------------------------------
        // 3. CRIAR EPISÓDIOS MANUALMENTE
        // -------------------------------------------------------

        // Enfermaria Geral - 4 camas, admissões sobrepostas para forçar pressão
        Episodio ep1 = new Episodio("C1", LocalDate.of(2024, 3, 1));
        ep1.darAlta(LocalDate.of(2024, 3, 10));

        Episodio ep2 = new Episodio("C2", LocalDate.of(2024, 3, 3));
        ep2.darAlta(LocalDate.of(2024, 3, 15));

        Episodio ep3 = new Episodio("C3", LocalDate.of(2024, 3, 3));
        ep3.darAlta(LocalDate.of(2024, 3, 20));

        Episodio ep4 = new Episodio("C4", LocalDate.of(2024, 3, 3));
        ep4.darAlta(LocalDate.of(2024, 3, 18));

        egeral.adicionarEpisodio(ep1);
        egeral.adicionarEpisodio(ep2);
        egeral.adicionarEpisodio(ep3);
        egeral.adicionarEpisodio(ep4);

        // Enfermaria Psiquiátrica
        Episodio ep5 = new Episodio("C1", LocalDate.of(2024, 3, 1));
        ep5.darAlta(LocalDate.of(2024, 3, 8));

        Episodio ep6 = new Episodio("C2", LocalDate.of(2024, 3, 5));

        epsi.adicionarEpisodio(ep5);
        epsi.adicionarEpisodio(ep6);

        // Enfermaria UCI
        Episodio ep7 = new Episodio("C1", LocalDate.of(2024, 3, 2));
        ep7.darAlta(LocalDate.of(2024, 3, 12));

        Episodio ep8 = new Episodio("C2", LocalDate.of(2024, 3, 2));
        ep8.darAlta(LocalDate.of(2024, 3, 7));

        Episodio ep9 = new Episodio("C3", LocalDate.of(2024, 3, 2));
        Episodio ep10 = new Episodio("C4", LocalDate.of(2024, 3, 2));

        euci.adicionarEpisodio(ep7);
        euci.adicionarEpisodio(ep8);
        euci.adicionarEpisodio(ep9);
        euci.adicionarEpisodio(ep10);

        // -------------------------------------------------------
        // 4. RF1 - CARREGAR A PARTIR DE FICHEIROS CSV
        // -------------------------------------------------------
        System.out.println("=== RF1: Leitura de Ficheiros CSV ===");
        Hospital hospitalCSV = new Hospital("Hospital CSV");
        GestorFicheiros.carregarEnfermarias("enfermarias.csv", hospitalCSV);
        GestorFicheiros.carregarEpisodios("episodios.csv", hospitalCSV);
        System.out.println("Enfermarias carregadas: " + hospitalCSV.getEnfermarias().size());
        for (Enfermaria enf : hospitalCSV.getEnfermarias()) {
            System.out.println("  " + enf);
        }
        System.out.println();

        // -------------------------------------------------------
        // 5. RF2 - INDICADORES DE OCUPAÇÃO
        // -------------------------------------------------------
        System.out.println("=== RF2: Indicadores de Ocupacao ===");
        LocalDate dataRef = LocalDate.of(2024, 3, 5);

        for (Enfermaria enf : hospital.getEnfermarias()) {
            System.out.println("\n" + enf.getIdentificador() + " em " + dataRef + ":");
            System.out.println("  Ocupacao absoluta : " + enf.getOcupacaoAbsoluta(dataRef) + " camas");
            System.out.printf ("  Taxa de ocupacao  : %.1f%%%n", enf.getTaxaOcupacao(dataRef));
            System.out.println("  Em pressao        : " + (enf.emPressao(dataRef) ? "Sim" : "Nao"));
        }
        System.out.println();

        // LoS da enfermaria geral
        System.out.println("=== RF2: Sumario LoS - Enfermaria " + egeral.getIdentificador() + " ===");
        AnalisadorEstatistico.SumarioLoS sumario = AnalisadorEstatistico.calcularEstatisticasLoS(egeral);
        System.out.println("  " + sumario);
        System.out.println();

        // -------------------------------------------------------
        // 6. RF3 - ANÁLISE DE PRESSÃO POR INTERVALO DE DATAS
        // -------------------------------------------------------
        System.out.println("=== RF3: Analise de Pressao por Intervalo ===");
        LocalDate inicio = LocalDate.of(2024, 3, 1);
        LocalDate fim = LocalDate.of(2024, 3, 10);

        for (Enfermaria enf : hospital.getEnfermarias()) {
            System.out.println("\nEnfermaria " + enf.getIdentificador() + " (" + inicio + " a " + fim + "):");
            AnalisadorEstatistico.analisarPressaoPorIntervalo(enf, inicio, fim);
        }
        System.out.println();

        // -------------------------------------------------------
        // 7. RF4 - LISTAGENS ORDENADAS
        // -------------------------------------------------------
        System.out.println("=== RF4: Episodios Ordenados por Admissao - Enfermaria " + egeral.getIdentificador() + " ===");
        List<Episodio> ordenados = egeral.getEpisodiosOrdenadosPorAdmissao();
        for (Episodio ep : ordenados) {
            System.out.println("  " + ep);
        }
        System.out.println();

        System.out.println("=== RF4: Enfermarias Ordenadas por Taxa de Ocupacao em " + dataRef + " ===");
        List<Enfermaria> listaOrdenada = hospital.getEnfermarias();
        AnalisadorEstatistico.ordenarPorTaxaOcupacao(listaOrdenada, dataRef);
        for (Enfermaria enf : listaOrdenada) {
            System.out.printf("  %s | Taxa: %.1f%%%n", enf.getIdentificador(), enf.getTaxaOcupacao(dataRef));
        }
    }
}
