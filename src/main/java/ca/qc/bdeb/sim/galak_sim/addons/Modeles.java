package ca.qc.bdeb.sim.galak_sim.addons;

import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import ca.qc.bdeb.sim.galak_sim.graphics.Simulation;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.*;

public class Modeles {

    public static void chargerSystemeSolaire(Simulation simulation) {
        simulation.viderPlanetes();
        simulation.reinitialiserVue(null);

        // Soleil
        simulation.ajouterNouvellePlanete(
                0, 0,
                0, 0,
                6.9634e8,
                1.989e30,
                "Soleil",
                chargerImage("/planetesSystemeSolaire/soleil.png"),
                Color.YELLOW,
                lireDescription("soleil.txt")
        );

        // Mercure
        simulation.ajouterNouvellePlanete(
                5.7909e10, 0,
                0, 47400,
                2.4397e6,
                3.3011e23,
                "Mercure",
                chargerImage("/planetesSystemeSolaire/mercure.png"),
                Color.LIGHTGRAY,
                lireDescription("mercure.txt")
        );

        // Vénus
        simulation.ajouterNouvellePlanete(
                1.0821e11, 0,
                0, 35020,
                6.0518e6,
                4.8675e24,
                "Vénus",
                chargerImage("/planetesSystemeSolaire/venus.png"),
                Color.GOLD,
                lireDescription("venus.txt")
        );

        // Terre
        simulation.ajouterNouvellePlanete(
                1.4960e11, 0,
                0, 29780,
                6.3710e6,
                5.9722e24,
                "Terre",
                chargerImage("/planetesSystemeSolaire/terre.png"),
                Color.DEEPSKYBLUE,
                lireDescription("terre.txt")
        );

        // Mars
        simulation.ajouterNouvellePlanete(
                2.2792e11, 0,
                0, 24070,
                3.3895e6,
                6.4171e23,
                "Mars",
                chargerImage("/planetesSystemeSolaire/mars.png"),
                Color.INDIANRED,
                lireDescription("mars.txt")
        );

        // Jupiter
        simulation.ajouterNouvellePlanete(
                7.7857e11, 0,
                0, 13070,
                6.9911e7,
                1.8982e27,
                "Jupiter",
                chargerImage("/planetesSystemeSolaire/jupiter.png"),
                Color.BEIGE,
                lireDescription("jupiter.txt")
        );

        // Saturne
        simulation.ajouterNouvellePlanete(
                1.4335e12, 0,
                0, 9680,
                5.8232e7,
                5.6834e26,
                "Saturne",
                chargerImage("/planetesSystemeSolaire/saturne.png"),
                Color.KHAKI,
                lireDescription("saturne.txt")
        );

        // Uranus
        simulation.ajouterNouvellePlanete(
                2.8725e12, 0,
                0, 6800,
                2.5362e7,
                8.6810e25,
                "Uranus",
                chargerImage("/planetesSystemeSolaire/uranus.png"),
                Color.AQUAMARINE,
                lireDescription("uranus.txt")
        );

        // Neptune
        simulation.ajouterNouvellePlanete(
                4.4951e12, 0,
                0, 5430,
                2.4622e7,
                1.02413e26,
                "Neptune",
                chargerImage("/planetesSystemeSolaire/neptune.png"),
                Color.ROYALBLUE,
                lireDescription("neptune.txt")
        );
    }

    public static void chargerCollision(Simulation simulation) {
        simulation.viderPlanetes();
        simulation.reinitialiserVue("1e-6");

        simulation.ajouterNouvellePlanete(
                -2.5e7, 0,
                5000, 0,
                1.0e7,
                4.0e22,
                "A",
                null,
                Color.WHITE,
                ""
        );

        simulation.ajouterNouvellePlanete(
                2.5e7, 0,
                -5000, 0,
                1.0e7,
                4.0e22,
                "B",
                null,
                Color.LIGHTBLUE,
                ""
        );
    }
    public static void chargerCercle(Simulation simulation) {
        simulation.viderPlanetes();
        simulation.reinitialiserVue("1e-6");

        double masse = 6.0e24;
        double rayon = 6.0e6;
        double distanceCentre = 2.0e8;

        // Vitesse circulaire pour 2 masses égales :
        // v = sqrt(G * m / (4r))
        double v = Math.sqrt((6.67430e-11 * masse) / (4.0 * distanceCentre));

        simulation.ajouterNouvellePlanete(
                -distanceCentre, 0,
                0, -v,
                rayon,
                masse,
                "A",
                null,
                Color.CORNFLOWERBLUE,
                ""
        );

        simulation.ajouterNouvellePlanete(
                distanceCentre, 0,
                0, v,
                rayon,
                masse,
                "B",
                null,
                Color.HOTPINK,
                ""
        );
    }
    public static void chargerTerreLune(Simulation simulation) {
        simulation.viderPlanetes();
        simulation.reinitialiserVue("1e-6");

        // Terre
        simulation.ajouterNouvellePlanete(
                0,
                0,
                0,
                0,
                6.371e6,
                5.9722e24,
                "Terre",
                chargerImage("/planetesSystemeSolaire/terre.png"),
                Color.DEEPSKYBLUE,
                lireDescription("terre.txt")
        );

        // Lune
        simulation.ajouterNouvellePlanete(
                3.844e8,
                0,
                0,
                1022,
                1.7374e6,
                7.342e22,
                "Lune",
                chargerImage("/planetesSystemeSolaire/mercure.png"),
                Color.LIGHTGRAY,
                lireDescription("lune.txt")
        );
    }

    public static void chargerBinaire(Simulation simulation) {
        simulation.viderPlanetes();
        simulation.reinitialiserVue(null);

        double masseEtoile = 2.0e30;
        double distance = 1.0e9;
        double vitesse = 258000;

        simulation.ajouterNouvellePlanete(
                -distance, 0,
                0, vitesse,
                5.0e7, masseEtoile,
                "A",
                null,
                Color.ORANGE,
                ""
        );

        simulation.ajouterNouvellePlanete(
                distance, 0,
                0, -vitesse,
                5.0e7, masseEtoile,
                "B",
                null,
                Color.YELLOW,
                ""
        );
    }

    private static Image chargerImage(String chemin) {
        return new Image(Modeles.class.getResource(chemin).toExternalForm());
    }

    public static void chargerTrouNoir (Simulation simulation){
        simulation.viderPlanetes();
        simulation.reinitialiserVue("5.0e-7");

        Planete trouNoir = simulation.ajouterNouvellePlanete(
                0.0,
                0.0,
                0,
                0,
                2e8,
                3e32,
                "Trou Noir",
                null,
                Color.PURPLE,
                ""
        );
        trouNoir.setTrouNoir(true);

        simulation.ajouterNouvellePlanete(
                1.5e10,
                0,
                0,
                1150000,
                4.0e6,
                5.9e24,
                "Planète",
                null,
                Color.BLUE,
                ""
        );
    }

    private static String lireDescription(String nomFichier) {
        StringBuilder contenu = new StringBuilder();
        String chemin = "/descriptionsAstres/" + nomFichier;

        try (InputStream is = Modeles.class.getResourceAsStream(chemin)) {
            if (is == null) return "Erreur : Fichier introuvable.";

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8))) {
                String ligne;
                while ((ligne = reader.readLine()) != null) {
                    contenu.append(ligne.trim()).append(" ");
                }
            }
        } catch (Exception e) {
            return "Erreur de lecture : " + e.getMessage();
        }

        return contenu.toString().trim();
    }
}