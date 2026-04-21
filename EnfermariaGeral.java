package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma enfermaria geral, com suporte a acompanhantes e recursos disponíveis.
 */
public class EnfermariaGeral extends Enfermaria {

    /** Número máximo de acompanhantes permitidos. */
    private int limiteAcompanhantes;

    /** Horário de visitas da enfermaria. */
    private String horarioVisitas;

    /** Lista de recursos disponíveis na enfermaria. */
    private List<String> recursosDisponiveis;

    /**
     * Cria uma nova enfermaria geral.
     *
     * @param id identificador único
     * @param camas número total de camas
     * @param acompanhantes número máximo de acompanhantes
     * @param horario horário de visitas
     */
    public EnfermariaGeral(String id, int camas, int acompanhantes, String horario) {
        super(id, camas);
        this.limiteAcompanhantes = acompanhantes;
        this.horarioVisitas = horario;
        this.recursosDisponiveis = new ArrayList<>();
    }

    /**
     * Adiciona um recurso à lista de recursos disponíveis.
     *
     * @param recurso nome do recurso a adicionar
     */
    public void adicionarRecurso(String recurso) {
        if (recurso != null && !recurso.isBlank()) {
            recursosDisponiveis.add(recurso);
        }
    }

    /**
     * Remove um recurso da lista de recursos disponíveis.
     *
     * @param recurso nome do recurso a remover
     * @return true se removido com sucesso, false se não existia
     */
    public boolean removerRecurso(String recurso) {
        return recursosDisponiveis.remove(recurso);
    }

    /** @return número máximo de acompanhantes */
    public int getLimiteAcompanhantes() { return limiteAcompanhantes; }

    /** @return horário de visitas */
    public String getHorarioVisitas() { return horarioVisitas; }

    /** @return cópia da lista de recursos disponíveis */
    public List<String> getRecursosDisponiveis() { return new ArrayList<>(recursosDisponiveis); }
}
