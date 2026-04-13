package ca.qc.bdeb.sim.galak_sim.addons;

import ca.qc.bdeb.sim.galak_sim.graphics.Simulation;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Modeles {

    public static void chargerSystemeSolaire(Simulation simulation) {
        simulation.viderPlanetes();
        simulation.reinitialiserVue();

        // Soleil
        simulation.ajouterNouvellePlanete(
                0, 0,
                0, 0,
                6.9634e8,                 // rayon réel (m)
                1.989e30,                 // masse réelle (kg)
                "Soleil",
                chargerImage("/planetesSystemeSolaire/soleil.png"),
                Color.YELLOW
        );

        // Mercure
        simulation.ajouterNouvellePlanete(
                5.7909e10, 0,            // distance moyenne au Soleil (m)
                0, 47400,                // vitesse orbitale moyenne (m/s)
                2.4397e6,                // rayon réel (m)
                3.3011e23,               // masse réelle (kg)
                "Mercure",
                chargerImage("/planetesSystemeSolaire/mercure.png"),
                Color.LIGHTGRAY
        );

        // Vénus
        simulation.ajouterNouvellePlanete(
                1.0821e11, 0,
                0, 35020,
                6.0518e6,
                4.8675e24,
                "Vénus",
                chargerImage("/planetesSystemeSolaire/venus.png"),
                Color.GOLD
        );

        // Terre
        simulation.ajouterNouvellePlanete(
                1.4960e11, 0,
                0, 29780,
                6.3710e6,
                5.9722e24,
                "Terre",
                chargerImage("/planetesSystemeSolaire/terre.png"),
                Color.DEEPSKYBLUE
        );

        // Mars
        simulation.ajouterNouvellePlanete(
                2.2792e11, 0,
                0, 24070,
                3.3895e6,
                6.4171e23,
                "Mars",
                chargerImage("/planetesSystemeSolaire/mars.png"),
                Color.INDIANRED
        );

        // Jupiter
        simulation.ajouterNouvellePlanete(
                7.7857e11, 0,
                0, 13070,
                6.9911e7,
                1.8982e27,
                "Jupiter",
                chargerImage("/planetesSystemeSolaire/jupiter.png"),
                Color.BEIGE
        );

        // Saturne
        simulation.ajouterNouvellePlanete(
                1.4335e12, 0,
                0, 9680,
                5.8232e7,
                5.6834e26,
                "Saturne",
                chargerImage("/planetesSystemeSolaire/saturne.png"),
                Color.KHAKI
        );

        // Uranus
        simulation.ajouterNouvellePlanete(
                2.8725e12, 0,
                0, 6800,
                2.5362e7,
                8.6810e25,
                "Uranus",
                chargerImage("/planetesSystemeSolaire/uranus.png"),
                Color.AQUAMARINE
        );

        // Neptune
        simulation.ajouterNouvellePlanete(
                4.4951e12, 0,
                0, 5430,
                2.4622e7,
                1.02413e26,
                "Neptune",
                chargerImage("/planetesSystemeSolaire/neptune.png"),
                Color.ROYALBLUE
        );
    }

    public static void chargerCollision(Simulation simulation) {
        simulation.viderPlanetes();
        simulation.reinitialiserVue();

        simulation.ajouterNouvellePlanete(
                -2.5e7, 0,
                5000, 0,
                1.0e7,
                4.0e22,
                "A",
                null,
                Color.WHITE
        );

        simulation.ajouterNouvellePlanete(
                2.5e7, 0,
                -5000, 0,
                1.0e7,
                4.0e22,
                "B",
                null,
                Color.LIGHTBLUE
        );
    }

    private static Image chargerImage(String chemin) {
        return new Image(Modeles.class.getResource(chemin).toExternalForm());
    }
}