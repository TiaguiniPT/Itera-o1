package modelo;

import java.util.ArrayList;
import java.util.List;

public class Hospital {
    private String nome;
    private List<Enfermaria> enfermarias;

    public Hospital(String nome) {
        this.nome = nome;
        this.enfermarias = new ArrayList<>();
    }

    public void adicionarEnfermaria(Enfermaria e) { this.enfermarias.add(e); }

    public Enfermaria obterEnfermaria(String id) {
        for (Enfermaria enf : enfermarias) {
            if (enf.getIdentificador().equals(id)) return enf;
        }
        return null;
    }

    public String getNome() { return nome; }
    public List<Enfermaria> getEnfermarias() { return new ArrayList<>(enfermarias); }
}