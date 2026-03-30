package ca.qc.bdeb.sim.galak_sim.addons;

import ca.qc.bdeb.sim.galak_sim.graphics.Simulation;

public class Presets {

    public static void chargerSystemeSolaire(Simulation simulation) {
        simulation.viderPlanetes();
        simulation.reinitialiserVue();

        // Soleil
        simulation.ajouterNouvellePlanete(0, 0, 0, 0, 80, 5.0e10, "Soleil");

        // Planètes internes
        simulation.ajouterNouvellePlanete(220, 0, 0, -389, 14, 8.0e5, "Mercure");
        simulation.ajouterNouvellePlanete(320, 0, 0, -323, 18, 1.2e6, "Vénus");
        simulation.ajouterNouvellePlanete(430, 0, 0, -279, 20, 1.5e6, "Terre");
        simulation.ajouterNouvellePlanete(560, 0, 0, -244, 16, 9.0e5, "Mars");

        // Géantes gazeuses
        simulation.ajouterNouvellePlanete(760, 0, 0, -210, 36, 1.2e7, "Jupiter");
        simulation.ajouterNouvellePlanete(980, 0, 0, -185, 32, 9.0e6, "Saturne");

        // Géantes de glace
        simulation.ajouterNouvellePlanete(1220, 0, 0, -160, 26, 4.0e6, "Uranus");
        simulation.ajouterNouvellePlanete(1480, 0, 0, -145, 24, 3.8e6, "Neptune");
    }
}