package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um hospital, agregando um conjunto de enfermarias.
 * Permite adicionar e consultar enfermarias pelo seu identificador único.
 */
public class Hospital {

    /** Nome do hospital. */
    private String nome;

    /** Lista de enfermarias do hospital (composição). */
    private List<Enfermaria> enfermarias;

    /**
     * Cria um novo hospital com o nome indicado.
     *
     * @param nome nome do hospital
     */
    public Hospital(String nome) {
        this.nome = nome;
        this.enfermarias = new ArrayList<>();
    }

    /**
     * Adiciona uma enfermaria ao hospital.
     *
     * @param enfermaria enfermaria a adicionar
     */
    public void adicionarEnfermaria(Enfermaria enfermaria) {
        this.enfermarias.add(enfermaria);
    }

    /**
     * Procura e retorna uma enfermaria pelo seu identificador único.
     *
     * @param id identificador da enfermaria a procurar
     * @return a enfermaria correspondente, ou {@code null} se não for encontrada
     */
    public Enfermaria obterEnfermaria(String id) {
        for (Enfermaria enf : enfermarias) {
            if (enf.getIdentificador().equals(id)) {
                return enf;
            }
        }
        return null;
    }

    /**
     * Retorna o nome do hospital.
     *
     * @return nome do hospital
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna uma cópia da lista de enfermarias do hospital.
     *
     * @return lista de enfermarias (cópia defensiva)
     */
    public List<Enfermaria> getEnfermarias() {
        return new ArrayList<>(enfermarias);
    }

    /**
     * Retorna uma representação textual do hospital.
     *
     * @return string com o nome do hospital e o número de enfermarias
     */
    @Override
    public String toString() {
        return String.format("Hospital: %s | Enfermarias: %d", nome, enfermarias.size());
    }
}
