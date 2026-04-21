package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma enfermaria hospitalar com um conjunto de camas e episódios de internamento.
 */
public abstract class Enfermaria implements IAnalisavel {

    /** Identificador único da enfermaria. */
    private String identificador;

    /** Número total de camas da enfermaria. */
    private int numeroCamas;

    /** Lista de episódios de internamento. */
    protected List<Episodio> episodios;

    /**
     * Cria uma nova enfermaria.
     *
     * @param identificador identificador único
     * @param numeroCamas número total de camas
     */
    public Enfermaria(String identificador, int numeroCamas) {
        this.identificador = identificador;
        this.numeroCamas = numeroCamas;
        this.episodios = new ArrayList<>();
    }

    /**
     * Adiciona um episódio de internamento à enfermaria.
     *
     * @param episodio episódio a adicionar
     */
    public void adicionarEpisodio(Episodio episodio) {
        this.episodios.add(episodio);
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
            if (admitidoAteData && semAltaOuAltaDepois) ocupadas++;
        }
        return ocupadas;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTaxaOcupacao(LocalDate data) {
        if (numeroCamas == 0) return 0.0;
        return ((double) getOcupacaoAbsoluta(data) / numeroCamas) * 100;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean emPressao(LocalDate data) {
        return getTaxaOcupacao(data) > 85.0;
    }

    /** @return identificador da enfermaria */
    public String getIdentificador() { return identificador; }

    /** @return número total de camas */
    public int getNumeroCamas() { return numeroCamas; }

    /** @return cópia da lista de episódios */
    public List<Episodio> getEpisodios() { return new ArrayList<>(episodios); }
}