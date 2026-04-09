package ca.qc.bdeb.sim.galak_sim.addons;

import ca.qc.bdeb.sim.galak_sim.graphics.Simulation;

public class Modeles {

    public static void chargerSystemeSolaire(Simulation simulation) {
        simulation.viderPlanetes();
        simulation.reinitialiserVue();

        // Soleil
        simulation.ajouterNouvellePlanete(0, 0, 0, 0, 80, 1.989e30, "Soleil");

        // Planètes internes
        simulation.ajouterNouvellePlanete(58, 0, 0, 47400, 14, 3.30e23, "Mercure");
        simulation.ajouterNouvellePlanete(108, 0, 0, 35000, 18, 4.87e24, "Vénus");
        simulation.ajouterNouvellePlanete(150, 0, 0, 29780, 20, 5.97e24, "Terre");
        simulation.ajouterNouvellePlanete(228, 0, 0, 24100, 16, 6.42e23, "Mars");

        // Géantes gazeuses
        simulation.ajouterNouvellePlanete(778, 0, 0, 13100, 36, 1.90e27, "Jupiter");
        simulation.ajouterNouvellePlanete(1430, 0, 0, 9700, 32, 5.68e26, "Saturne");

        // Géantes de glace
        simulation.ajouterNouvellePlanete(2870, 0, 0, 6800, 26, 8.68e25, "Uranus");
        simulation.ajouterNouvellePlanete(4500, 0, 0, 5400, 24, 1.02e26, "Neptune");
    }

    public static void chargerCollision(Simulation simulation) {
        simulation.viderPlanetes();
        simulation.reinitialiserVue();

        simulation.ajouterNouvellePlanete(-100, 0, 2, 0, 40, 4.0e6, "A");
        simulation.ajouterNouvellePlanete(100, 0, -2, 0, 40, 4.0e6, "B");
    }
}