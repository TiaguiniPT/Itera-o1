package main;

import modelo.*;
import utils.AnalisadorEstatistico;
import java.time.LocalDate;

public class MainHospital {
    public static void main(String[] args) {
        Hospital h = new Hospital("Hospital Central XYZ");

        EnfermariaGeral e1 = new EnfermariaGeral("G1", 10, 2, "14h-19h");
        Episodio ep1 = new Episodio(LocalDate.of(2024, 3, 1));
        ep1.darAlta(LocalDate.of(2024, 3, 10)); // 9 dias LoS

        e1.adicionarEpisodio(ep1);
        h.adicionarEnfermaria(e1);

        System.out.println("Estatísticas " + e1.getIdentificador() + ":");
        System.out.println(AnalisadorEstatistico.calcularEstatisticasLoS(e1));
    }
}
