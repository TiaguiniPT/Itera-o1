package modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Representa um episódio de internamento de um paciente numa cama específica.
 */
public class Episodio implements Comparable<Episodio> {

    /** Identificador único da cama associada ao episódio. */
    private String identificadorCama;

    /** Data de admissão do paciente. */
    private LocalDate dataAdmissao;

    /** Data de alta do paciente. Null se ainda não tiver alta. */
    private LocalDate dataAlta;

    /** Indica se o paciente já teve alta. */
    private boolean flagAlta;

    /**
     * Cria um novo episódio de internamento.
     *
     * @param identificadorCama identificador da cama
     * @param dataAdmissao data de admissão do paciente
     */
    public Episodio(String identificadorCama, LocalDate dataAdmissao) {
        this.identificadorCama = identificadorCama;
        this.dataAdmissao = dataAdmissao;
        this.flagAlta = false;
    }

    /**
     * Regista a alta do paciente.
     *
     * @param dataAlta data de alta (deve ser posterior à data de admissão)
     */
    public void darAlta(LocalDate dataAlta) {
        if (dataAlta != null && !dataAlta.isBefore(this.dataAdmissao)) {
            this.dataAlta = dataAlta;
            this.flagAlta = true;
        }
    }

    /**
     * Calcula o Length of Stay (LoS) em dias.
     * Retorna 0 se o paciente ainda não tiver alta.
     *
     * @return número de dias de internamento
     */
    public long getLoS() {
        if (!flagAlta) return 0;
        return ChronoUnit.DAYS.between(dataAdmissao, dataAlta);
    }

    /**
     * @return identificador da cama
     */
    public String getIdentificadorCama() { return identificadorCama; }

    /**
     * @return data de admissão
     */
    public LocalDate getDataAdmissao() { return dataAdmissao; }

    /**
     * @return data de alta, ou null se ainda não tiver alta
     */
    public LocalDate getDataAlta() { return dataAlta; }

    /**
     * @return true se o paciente já teve alta, false caso contrário
     */
    public boolean isFlagAlta() { return flagAlta; }

    /**
     * Compara episódios por data de admissão (ordem crescente).
     */
    @Override
    public int compareTo(Episodio outro) {
        return this.dataAdmissao.compareTo(outro.dataAdmissao);
    }

    /**
     * @return representação textual do episódio
     */
    @Override
    public String toString() {
        return String.format("Cama: %s | Admissão: %s | Alta: %s | LoS: %d dias",
                identificadorCama,
                dataAdmissao,
                flagAlta ? dataAlta.toString() : "Em internamento",
                getLoS());
    }
}