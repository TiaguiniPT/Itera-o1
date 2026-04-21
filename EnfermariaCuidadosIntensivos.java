package modelo;

public class EnfermariaCuidadosIntensivos extends Enfermaria {
    private String horarioVisitas;
    private double pressaoAtmosferica;
    private double pressaoAtmosfericaReferencia;

    public EnfermariaCuidadosIntensivos(String id, int camas, String horario, double p, double pref) {
        super(id, camas);
        this.horarioVisitas = horario;
        this.pressaoAtmosferica = p;
        this.pressaoAtmosfericaReferencia = pref;
    }

    public String getHorarioVisitas() { return horarioVisitas; }
    public double getPressaoAtmosferica() { return pressaoAtmosferica; }
    public double getPressaoAtmosfericaReferencia() { return pressaoAtmosfericaReferencia; }
}