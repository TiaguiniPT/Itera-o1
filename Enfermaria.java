package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe abstrata que representa uma enfermaria hospitalar.
 * Contém um identificador único, um número de camas e uma lista de episódios de internamento.
 * Serve de base para os tipos especializados de enfermaria.
 */
public abstract class Enfermaria implements Analisavel {

    /** Identificador único da enfermaria. */
    private String identificador;

    /** Número total de camas disponíveis na enfermaria. */
    private int numeroCamas;

    /** Lista de episódios de internamento associados a esta enfermaria. */
    protected List<Episodio> episodios;

    /**
     * Cria uma nova enfermaria com o identificador e número de camas indicados.
     *
     * @param identificador identificador único da enfermaria
     * @param numeroCamas   número total de camas
     */
    public Enfermaria(String identificador, int numeroCamas) {
        this.identificador = identificador;
        this.numeroCamas = numeroCamas;
        this.episodios = new ArrayList<>();
    }

    /**
     * Adiciona um episódio de internamento à lista da enfermaria.
     *
     * @param episodio episódio a adicionar
     */
    public void adicionarEpisodio(Episodio episodio) {
        this.episodios.add(episodio);
    }

    /**
     * Retorna os episódios ordenados por data de admissão (ordem crescente).
     *
     * @return nova lista ordenada dos episódios
     */
    public List<Episodio> getEpisodiosOrdenadosPorAdmissao() {
        List<Episodio> ordenados = new ArrayList<>(episodios);
        Collections.sort(ordenados);
        return ordenados;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOcupacaoAbsoluta(LocalDate data) {
        int ocupadas = 0;
        for (Episodio ep : episodios) {
            boolean admitidoAteData = !ep.getDataAdmissao().isAfter(data);
            boolean semAltaOuAltaDepois = !ep.isFlagAlta() || ep.getDataAlta().isAfter(data);
            if (admitidoAteData && semAltaOuAltaDepois) {
                ocupadas++;
            }
        }
        return ocupadas;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTaxaOcupacao(LocalDate data) {
        if (numeroCamas == 0) return 0.0;
        return ((double) getOcupacaoAbsoluta(data) / numeroCamas) * 100.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean emPressao(LocalDate data) {
        return getTaxaOcupacao(data) > 85.0;
    }

    /**
     * Retorna o identificador único da enfermaria.
     *
     * @return identificador da enfermaria
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Retorna o número total de camas da enfermaria.
     *
     * @return número de camas
     */
    public int getNumeroCamas() {
        return numeroCamas;
    }

    /**
     * Retorna uma cópia da lista de episódios de internamento.
     *
     * @return lista de episódios (cópia defensiva)
     */
    public List<Episodio> getEpisodios() {
        return new ArrayList<>(episodios);
    }


    /**
     * Retorna uma representação textual da enfermaria com identificador e número de camas.
     *
     * @return string formatada com os dados da enfermaria
     */
    @Override
    public String toString() {
        return String.format("Enfermaria [%s] | Camas: %d | Episodios: %d",
                identificador, numeroCamas, episodios.size());
    }
}
