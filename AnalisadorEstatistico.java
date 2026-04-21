package utils;

import modelo.Enfermaria;
import modelo.Episodio;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnalisadorEstatistico {

    // Nested Class Estática (Matéria nova!)
    public static class SumarioLoS {
        public double media, desvioPadrao;
        public long minimo, maximo;

        public SumarioLoS(double m, double dp, long min, long max) {
            this.media = m; this.desvioPadrao = dp; this.minimo = min; this.maximo = max;
        }

        @Override
        public String toString() {
            return String.format("Média: %.2f | DP: %.2f | Mín: %d | Máx: %d", media, desvioPadrao, minimo, maximo);
        }
    }

    public static SumarioLoS calcularEstatisticasLoS(Enfermaria enf) {
        long soma = 0, min = Long.MAX_VALUE, max = Long.MIN_VALUE;
        int altas = 0;
        List<Episodio> eps = enf.getEpisodios();

        for (Episodio ep : eps) {
            if (ep.isFlagAlta()) {
                long los = ep.getLoS();
                soma += los; altas++;
                if (los < min) min = los;
                if (los > max) max = los;
            }
        }
        if (altas == 0) return new SumarioLoS(0,0,0,0);
        double m = (double) soma / altas;
        double somaVar = 0;
        for (Episodio ep : eps) {
            if (ep.isFlagAlta()) somaVar += Math.pow(ep.getLoS() - m, 2);
        }
        return new SumarioLoS(m, Math.sqrt(somaVar / altas), min, max);
    }

    // Classe Anónima para ordenação (Matéria nova!)
    public static void ordenarPorTaxa(List<Enfermaria> lista, LocalDate data) {
        Collections.sort(lista, new Comparator<Enfermaria>() {
            @Override
            public int compare(Enfermaria e1, Enfermaria e2) {
                return Double.compare(e2.getTaxaOcupacao(data), e1.getTaxaOcupacao(data));
            }
        });
    }
}