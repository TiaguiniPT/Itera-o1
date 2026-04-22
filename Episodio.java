package modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Representa um episódio de internamento de um paciente numa cama específica.
 * Contém informação sobre a cama, datas de admissão e alta, e o estado do episódio.
 */
public class Episodio implements Comparable<Episodio> {

    /** Identificador único da cama associada ao episódio. */
    private String identificadorCama;

    /** Data de admissão do paciente. */
    private LocalDate dataAdmissao;

    /** Data de alta do paciente. {@code null} se o paciente ainda estiver internado. */
    private LocalDate dataAlta;

    /** Indica se o paciente já teve alta. */
    private boolean flagAlta;

    /**
     * Cria um novo episódio de internamento.
     *
     * @param identificadorCama identificador único da cama
     * @param dataAdmissao      data de admissão do paciente
     */
    public Episodio(String identificadorCama, LocalDate dataAdmissao) {
        this.identificadorCama = identificadorCama;
        this.dataAdmissao = dataAdmissao;
        this.dataAlta = null;
        this.flagAlta = false;
    }

    /**
     * Regista a alta do paciente, definindo a data de alta e ativando a flag de alta.
     * A operação só é executada se a data de alta for igual ou posterior à data de admissão.
     *
     * @param dataAlta data de alta do paciente
     */
    public void darAlta(LocalDate dataAlta) {
        if (dataAlta != null && dataAlta.isAfter(this.dataAdmissao)) { // <- esta linha
            this.dataAlta = dataAlta;
            this.flagAlta = true;
        }
    }

    /**
     * Calcula o Length of Stay (LoS) em dias.
     * Retorna 0 se o paciente ainda não tiver alta.
     *
     * @return número de dias de internamento, ou 0 se sem alta
     */
    public long getLoS() {
        if (!flagAlta) return 0;
        return ChronoUnit.DAYS.between(dataAdmissao, dataAlta);
    }

    /**
     * Retorna o identificador da cama.
     *
     * @return identificador da cama
     */
    public String getIdentificadorCama() {
        return identificadorCama;
    }

    /**
     * Retorna a data de admissão do paciente.
     *
     * @return data de admissão
     */
    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    /**
     * Retorna a data de alta do paciente.
     *
     * @return data de alta, ou {@code null} se ainda não tiver alta
     */
    public LocalDate getDataAlta() {
        return dataAlta;
    }

    /**
     * Indica se o paciente já teve alta.
     *
     * @return {@code true} se o paciente já teve alta, {@code false} caso contrário
     */
    public boolean isFlagAlta() {
        return flagAlta;
    }

    /**
     * Compara episódios por data de admissão em ordem crescente.
     *
     * @param outro episódio a comparar
     * @return valor negativo, zero ou positivo consoante este episódio seja anterior,
     *         igual ou posterior ao outro
     */
    @Override
    public int compareTo(Episodio outro) {
        return this.dataAdmissao.compareTo(outro.dataAdmissao);
    }

    /**
     * Retorna uma representação textual do episódio com cama, datas e LoS.
     *
     * @return string formatada com os dados do episódio
     */
    @Override
    public String toString() {
        return String.format("Cama: %s | Admissao: %s | Alta: %s | LoS: %d dias",
                identificadorCama,
                dataAdmissao,
                flagAlta ? dataAlta.toString() : "Em internamento",
                getLoS());
    }
}
