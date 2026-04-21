package modelo;

public class EnfermariaPsiquiatrica extends Enfermaria {
    private String horarioVisitas;
    private String nivelSeguranca;

    public EnfermariaPsiquiatrica(String id, int camas, String horario, String seguranca) {
        super(id, camas);
        this.horarioVisitas = horario;
        this.nivelSeguranca = seguranca;
    }

    public String getHorarioVisitas() { return horarioVisitas; }
    public String getNivelSeguranca() { return nivelSeguranca; }
}