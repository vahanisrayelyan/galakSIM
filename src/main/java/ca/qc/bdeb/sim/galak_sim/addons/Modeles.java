package ca.qc.bdeb.sim.galak_sim.addons;

import ca.qc.bdeb.sim.galak_sim.graphics.Simulation;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;



public class Modeles {

    public static void chargerSystemeSolaire(Simulation simulation) {
        simulation.viderPlanetes();
        simulation.reinitialiserVue();
        Image image = null;


        // Soleil
        simulation.ajouterNouvellePlanete(0, 0, 0, 0, 80, 1.989e30, "Soleil",new Image(Modeles.class.getResource("/planetesSystemeSolaire/soleil.png").toExternalForm()),Color.YELLOW);

        // Planètes internes
        simulation.ajouterNouvellePlanete(50, 0, 0, 47400, 14, 3.30e23, "Mercure",new Image(Modeles.class.getResource("/planetesSystemeSolaire/mercure.png").toExternalForm()),Color.LIGHTGRAY);
        simulation.ajouterNouvellePlanete(108, 0, 0, 35000, 18, 4.87e24, "Vénus",new Image(Modeles.class.getResource("/planetesSystemeSolaire/venus.png").toExternalForm()),Color.YELLOW);
        simulation.ajouterNouvellePlanete(150, 0, 0, 29780, 20, 5.97e24, "Terre",new Image(Modeles.class.getResource("/planetesSystemeSolaire/terre.png").toExternalForm()),Color.SKYBLUE);
        simulation.ajouterNouvellePlanete(228, 0, 0, 24100, 16, 6.42e23, "Mars",new Image(Modeles.class.getResource("/planetesSystemeSolaire/mars.png").toExternalForm()),Color.INDIANRED);

        // Géantes gazeuses
        simulation.ajouterNouvellePlanete(778, 0, 0, 13100, 36, 1.90e27, "Jupiter",new Image(Modeles.class.getResource("/planetesSystemeSolaire/jupiter.png").toExternalForm()),Color.BEIGE);
        simulation.ajouterNouvellePlanete(1430, 0, 0, 9700, 45, 5.68e26, "Saturne",new Image(Modeles.class.getResource("/planetesSystemeSolaire/saturne.png").toExternalForm()),Color.SADDLEBROWN);

        // Géantes de glace
        simulation.ajouterNouvellePlanete(2870, 0, 0, 6800, 26, 8.68e25, "Uranus",new Image(Modeles.class.getResource("/planetesSystemeSolaire/uranus.png").toExternalForm()),Color.AQUAMARINE);
        simulation.ajouterNouvellePlanete(4500, 0, 0, 5400, 24, 1.02e26, "Neptune",new Image(Modeles.class.getResource("/planetesSystemeSolaire/neptune.png").toExternalForm()),Color.BLUEVIOLET);
    }

    public static void chargerCollision(Simulation simulation) {
        Image image = null;
        Color color = null;
        simulation.viderPlanetes();
        simulation.reinitialiserVue();

        simulation.ajouterNouvellePlanete(-100, 0, 2, 0, 40, 4.0e6, "A",image,color);
        simulation.ajouterNouvellePlanete(100, 0, -2, 0, 40, 4.0e6, "B",image,color);
    }
}