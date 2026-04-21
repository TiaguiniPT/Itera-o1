package modelo;

/**
 * Representa uma enfermaria de cuidados intensivos (UCI).
 * Para além dos atributos base, inclui horário de visitas e valores de pressão atmosférica
 * para monitorização do ambiente clínico.
 * Estende {@link Enfermaria} com atributos específicos de uma unidade de cuidados intensivos.
 */
public class EnfermariaCuidadosIntensivos extends Enfermaria {

    /** Horário de visitas permitido na unidade. */
    private String horarioVisitas;

    /** Pressão atmosférica atual registada na unidade (em hPa). */
    private double pressaoAtmosferica;

    /** Pressão atmosférica de referência para a unidade (em hPa). */
    private double pressaoAtmosfericaReferencia;

    /**
     * Cria uma nova enfermaria de cuidados intensivos.
     *
     * @param id                         identificador único da enfermaria
     * @param camas                      número total de camas
     * @param horarioVisitas             horário de visitas permitido
     * @param pressaoAtmosferica         pressão atmosférica atual (hPa)
     * @param pressaoAtmosfericaReferencia pressão atmosférica de referência (hPa)
     */
    public EnfermariaCuidadosIntensivos(String id, int camas, String horarioVisitas,
                                        double pressaoAtmosferica, double pressaoAtmosfericaReferencia) {
        super(id, camas);
        this.horarioVisitas = horarioVisitas;
        this.pressaoAtmosferica = pressaoAtmosferica;
        this.pressaoAtmosfericaReferencia = pressaoAtmosfericaReferencia;
    }

    /**
     * Retorna o horário de visitas da unidade.
     *
     * @return horário de visitas
     */
    public String getHorarioVisitas() {
        return horarioVisitas;
    }

    /**
     * Retorna a pressão atmosférica atual registada na unidade.
     *
     * @return pressão atmosférica atual em hPa
     */
    public double getPressaoAtmosferica() {
        return pressaoAtmosferica;
    }

    /**
     * Atualiza o valor de pressão atmosférica atual da unidade.
     *
     * @param pressaoAtmosferica novo valor de pressão em hPa
     */
    public void setPressaoAtmosferica(double pressaoAtmosferica) {
        this.pressaoAtmosferica = pressaoAtmosferica;
    }

    /**
     * Retorna a pressão atmosférica de referência da unidade.
     *
     * @return pressão atmosférica de referência em hPa
     */
    public double getPressaoAtmosfericaReferencia() {
        return pressaoAtmosfericaReferencia;
    }

    /**
     * Retorna uma representação textual da enfermaria de cuidados intensivos.
     *
     * @return string com identificador, camas, horário de visitas e pressões atmosféricas
     */
    @Override
    public String toString() {
        return String.format("%s | Tipo: UCI | Visitas: %s | Pressao: %.1f hPa (ref: %.1f hPa)",
                super.toString(), horarioVisitas, pressaoAtmosferica, pressaoAtmosfericaReferencia);
    }
}
