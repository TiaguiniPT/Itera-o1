package utils;

import modelo.Enfermaria;
import modelo.Episodio;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Classe utilitária com métodos estáticos para análise estatística de enfermarias.
 * Inclui cálculo de sumário de LoS, análise de pressão por intervalo de datas
 * e ordenação de enfermarias.
 */
public class AnalisadorEstatistico {

    /**
     * Classe interna estática que representa as medidas de sumário do Length of Stay (LoS).
     * Agrupa média, desvio padrão, mínimo e máximo.
     */
    public static class SumarioLoS {

        /** Média do LoS em dias. */
        public double media;

        /** Desvio padrão do LoS em dias. */
        public double desvioPadrao;

        /** Valor mínimo do LoS em dias. */
        public long minimo;

        /** Valor máximo do LoS em dias. */
        public long maximo;

        /**
         * Cria um novo sumário de LoS com os valores indicados.
         *
         * @param media       média do LoS
         * @param desvioPadrao desvio padrão do LoS
         * @param minimo      valor mínimo do LoS
         * @param maximo      valor máximo do LoS
         */
        public SumarioLoS(double media, double desvioPadrao, long minimo, long maximo) {
            this.media = media;
            this.desvioPadrao = desvioPadrao;
            this.minimo = minimo;
            this.maximo = maximo;
        }

        /**
         * Retorna uma representação textual formatada do sumário de LoS.
         *
         * @return string com média, desvio padrão, mínimo e máximo
         */
        @Override
        public String toString() {
            return String.format("Media: %.2f dias | DP: %.2f | Min: %d dias | Max: %d dias",
                    media, desvioPadrao, minimo, maximo);
        }
    }

    /**
     * Calcula as medidas de sumário do Length of Stay para os episódios com alta de uma enfermaria.
     *
     * @param enf enfermaria a analisar
     * @return sumário com média, desvio padrão, mínimo e máximo; zeros se não existirem altas
     */
    public static SumarioLoS calcularEstatisticasLoS(Enfermaria enf) {
        long soma = 0;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        int totalAltas = 0;

        List<Episodio> eps = enf.getEpisodios();

        for (Episodio ep : eps) {
            if (ep.isFlagAlta()) {
                long los = ep.getLoS();
                soma += los;
                totalAltas++;
                if (los < min) min = los;
                if (los > max) max = los;
            }
        }

        if (totalAltas == 0) {
            return new SumarioLoS(0.0, 0.0, 0L, 0L);
        }

        double media = (double) soma / totalAltas;
        double somaVariancia = 0.0;

        for (Episodio ep : eps) {
            if (ep.isFlagAlta()) {
                somaVariancia += Math.pow(ep.getLoS() - media, 2);
            }
        }

        double desvioPadrao = Math.sqrt(somaVariancia / totalAltas);
        return new SumarioLoS(media, desvioPadrao, min, max);
    }

    /**
     * Analisa o estado de pressão de uma enfermaria para cada dia de um intervalo de datas.
     * Classifica cada dia como "Em pressao" (taxa de ocupação acima de 85%) ou "Estado normal".
     * No final, apresenta a percentagem de dias em situação de pressão.
     *
     * @param enf        enfermaria a analisar
     * @param dataInicio data de início do intervalo (inclusive)
     * @param dataFim    data de fim do intervalo (inclusive)
     */
    public static void analisarPressaoPorIntervalo(Enfermaria enf, LocalDate dataInicio, LocalDate dataFim) {
        int totalDias = 0;
        int diasEmPressao = 0;

        LocalDate dataAtual = dataInicio;
        while (!dataAtual.isAfter(dataFim)) {
            totalDias++;
            String estado;
            if (enf.emPressao(dataAtual)) {
                estado = "Em pressao";
                diasEmPressao++;
            } else {
                estado = "Estado normal";
            }
            System.out.printf("  %s | Taxa: %.1f%% | %s%n",
                    dataAtual, enf.getTaxaOcupacao(dataAtual), estado);
            dataAtual = dataAtual.plusDays(1);
        }

        if (totalDias > 0) {
            double percentagemPressao = ((double) diasEmPressao / totalDias) * 100.0;
            System.out.printf("  --> Dias em pressao: %d/%d (%.1f%%)%n",
                    diasEmPressao, totalDias, percentagemPressao);
        }
    }

    /**
     * Ordena uma lista de enfermarias por taxa de ocupação decrescente numa data de referência,
     * utilizando uma classe anónima como {@link Comparator}.
     *
     * @param lista lista de enfermarias a ordenar (modificada no local)
     * @param data  data de referência para o cálculo da taxa de ocupação
     */
    public static void ordenarPorTaxaOcupacao(List<Enfermaria> lista, LocalDate data) {
        Collections.sort(lista, new Comparator<Enfermaria>() {
            /**
             * Compara duas enfermarias por taxa de ocupação decrescente.
             *
             * @param e1 primeira enfermaria
             * @param e2 segunda enfermaria
             * @return valor positivo se e1 tiver menor taxa que e2, negativo se maior
             */
            @Override
            public int compare(Enfermaria e1, Enfermaria e2) {
                return Double.compare(e2.getTaxaOcupacao(data), e1.getTaxaOcupacao(data));
            }
        });
    }
}
