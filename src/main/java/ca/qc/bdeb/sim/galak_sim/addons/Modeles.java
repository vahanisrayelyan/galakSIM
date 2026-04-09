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
        simulation.ajouterNouvellePlanete(0, 0, 0, 0, 80, 5.0e10, "Soleil",new Image(Modeles.class.getResource("/planetesSystemeSolaire/soleil.png").toExternalForm()),Color.YELLOW);

        // Planètes internes
        simulation.ajouterNouvellePlanete(220, 0, 0, -389, 14, 8.0e5, "Mercure",new Image(Modeles.class.getResource("/planetesSystemeSolaire/mercure.png").toExternalForm()),Color.LIGHTGRAY);
        simulation.ajouterNouvellePlanete(320, 0, 0, -323, 18, 1.2e6, "Vénus",new Image(Modeles.class.getResource("/planetesSystemeSolaire/venus.png").toExternalForm()),Color.YELLOW);
        simulation.ajouterNouvellePlanete(430, 0, 0, -279, 20, 1.5e6, "Terre",new Image(Modeles.class.getResource("/planetesSystemeSolaire/terre.png").toExternalForm()),Color.SKYBLUE);
        simulation.ajouterNouvellePlanete(560, 0, 0, -244, 16, 9.0e5, "Mars",new Image(Modeles.class.getResource("/planetesSystemeSolaire/mars.png").toExternalForm()),Color.INDIANRED);

        // Géantes gazeuses
        simulation.ajouterNouvellePlanete(760, 0, 0, -210, 36, 1.2e7, "Jupiter",new Image(Modeles.class.getResource("/planetesSystemeSolaire/jupiter.png").toExternalForm()),Color.BEIGE);
        simulation.ajouterNouvellePlanete(980, 0, 0, -185, 45, 9.0e6, "Saturne",new Image(Modeles.class.getResource("/planetesSystemeSolaire/saturne.png").toExternalForm()),Color.SADDLEBROWN);

        // Géantes de glace
        simulation.ajouterNouvellePlanete(1220, 0, 0, -160, 26, 4.0e6, "Uranus",new Image(Modeles.class.getResource("/planetesSystemeSolaire/uranus.png").toExternalForm()),Color.AQUAMARINE);
        simulation.ajouterNouvellePlanete(1480, 0, 0, -145, 24, 3.8e6, "Neptune",new Image(Modeles.class.getResource("/planetesSystemeSolaire/neptune.png").toExternalForm()),Color.BLUEVIOLET);
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