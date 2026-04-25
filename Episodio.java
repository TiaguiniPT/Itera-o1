package modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Representa um episódio de internamento de um paciente numa cama específica.
 */
public class Episodio implements Comparable<Episodio> {

    /** Contador para gerar identificadores simples. */
    private static int proximoNumero = 1;

    /** Identificador do episódio. */
    private String episodioId; 

    /** Identificador único da cama associada ao episódio. */
    private String identificadorCama;

    /** Data de admissão do paciente. */
    private LocalDate dataAdmissao;

    /** Data de alta do paciente. {@code null} se o paciente ainda estiver internado. */
    private LocalDate dataAlta;

    /** Indica se o paciente já teve alta. */
    private boolean flagAlta;

    /**
     * Cria um episódio ativo com identificador gerado automaticamente.
     *
     * @param identificadorCama identificador da cama
     * @param dataAdmissao data de admissão
     */
    public Episodio(String identificadorCama, LocalDate dataAdmissao) {
        this("EP" + proximoNumero++, identificadorCama, dataAdmissao, null, false);
    }

    /**
     * Cria um episódio com todos os dados definidos.
     *
     * @param episodioId identificador do episódio
     * @param identificadorCama identificador da cama
     * @param dataAdmissao data de admissão
     * @param dataAlta data de alta
     * @param flagAlta indica se tem alta
     */
    public Episodio(String episodioId, String identificadorCama, LocalDate dataAdmissao, LocalDate dataAlta, boolean flagAlta) {
        this.episodioId = episodioId;
        this.identificadorCama = identificadorCama;
        this.dataAdmissao = dataAdmissao;
        this.dataAlta = dataAlta;
        this.flagAlta = flagAlta;
    }

    /**
     * Devolve o identificador do episódio.
     *
     * @return identificador do episódio
     */
    public String getEpisodioId() {
        return episodioId;
    }
    
    /**
     * Regista a alta do paciente.
     *
     * @param dataAlta data de alta
     */
    public void darAlta(LocalDate dataAlta) {
        if (dataAlta != null && dataAlta.isAfter(this.dataAdmissao)) {
            this.dataAlta = dataAlta;
            this.flagAlta = true;
        }
    }
    
    /**
     * Calcula o Length of Stay em dias.
     *
     * @return número de dias, ou 0 se sem alta
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
     * @return {@code true} se o paciente já teve alta
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
