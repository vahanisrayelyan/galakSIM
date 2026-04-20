package ca.qc.bdeb.sim.galak_sim.addons;

import ca.qc.bdeb.sim.galak_sim.graphics.Simulation;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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
                "Le Soleil est l’étoile de type naine jaune du Système solaire, qui se situe dans le bras " +
                        "d'Orion, à environ 8 kpc (∼26 100 al) du centre galactique de la galaxie Voie lactée. Il " +
                        "orbite autour du centre galactique en une année galactique de 225 à 250 millions d'années. " +
                        "Autour de lui gravitent de nombreux objets, dont la Terre (à la vitesse de 30 km/s), sept " +
                        "autres planètes, au moins cinq planètes naines, de très nombreux astéroïdes et comètes " +
                        "(notamment dans la ceinture d'astéroïdes et la ceinture de Kuiper). Le Soleil représente à " +
                        "lui seul environ 99,854 % de la masse du système planétaire ainsi constitué, Jupiter " +
                        "représentant plus des deux tiers du reste." + "\n" + "https://fr.wikipedia.org/wiki/Soleil"
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
                "Mercure est la planète la plus proche du Soleil et la moins massive du Système " +
                        "solaire[N 1]. Son éloignement du Soleil est compris entre 0,31 et 0,47 unité astronomique " +
                        "(soit 46 et 70 millions de kilomètres), ce qui correspond à une excentricité orbitale de " +
                        "0,2 — plus de douze fois supérieure à celle de la Terre, et de loin la plus élevée pour une " +
                        "planète du Système solaire. Elle est visible à l'œil nu depuis la Terre avec une taille " +
                        "apparente de 4,5 à 13 secondes d'arc, et une magnitude apparente de 5,7 à −2,3 ; son " +
                        "observation est toutefois rendue difficile par son élongation toujours inférieure à 28,3° " +
                        "qui la noie le plus souvent dans l'éclat du soleil. En pratique, cette proximité avec le " +
                        "soleil implique qu'elle ne peut être vue que près de l'horizon occidental après le coucher " +
                        "du soleil ou près de l'horizon oriental avant le lever du soleil, en général au crépuscule. " +
                        "Mercure a la particularité d'être en résonance spin-orbite 3:2, sa période de révolution " +
                        "(~88 jours) valant exactement 1,5 fois sa période de rotation (~59 jours), et donc la moitié " +
                        "d'un jour solaire (~176 jours). Ainsi, relativement aux étoiles fixes, elle tourne sur son " +
                        "axe exactement trois fois toutes les deux révolutions autour du Soleil." + "\n" +
                        "https://fr.wikipedia.org/wiki/Mercure_(plan%C3%A8te)"
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
                "Vénus est la deuxième planète du Système solaire par ordre d'éloignement au Soleil, et la " +
                        "sixième plus grosse aussi bien par la masse que par le diamètre. Vénus orbite autour du " +
                        "Soleil tous les 224,7 jours terrestres. Avec une période de rotation de 243 jours " +
                        "terrestres, il lui faut plus de temps pour tourner autour de son axe que toute autre planète " +
                        "du Système solaire. Comme Uranus, elle possède une rotation rétrograde et tourne dans le " +
                        "sens opposé à celui des autres planètes : le Soleil s'y lève à l'ouest et se couche à l'est. " +
                        "Vénus possède l'orbite la plus circulaire des planètes du Système solaire avec une " +
                        "excentricité orbitale presque nulle et, du fait de sa lente rotation, est quasiment sphérique " +
                        "(aplatissement considéré comme nul). Elle ne possède pas de satellite naturel." + "\n" +
                        "https://fr.wikipedia.org/wiki/V%C3%A9nus_(plan%C3%A8te)"
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
                "La Terre est la troisième planète par ordre d'éloignement au Soleil et la cinquième plus " +
                        "grande du Système solaire aussi bien par la masse que par le diamètre. Par ailleurs, elle est " +
                        "le seul objet céleste connu pour abriter la vie. Elle orbite autour du Soleil en 365,256 jours " +
                        "solaires — une année sidérale — et réalise une rotation sur elle-même relativement au Soleil " +
                        "en un jour sidéral (environ 23 h 56 min 4 s), soit un peu moins que son jour solaire de 24 " +
                        "heures du fait de ce déplacement autour du Soleil[a]. L'axe de rotation de la Terre possède " +
                        "une inclinaison d'environ 23°, ce qui cause l'apparition des saisons." + "\n" +
                        "https://fr.wikipedia.org/wiki/Terre"
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
                "Mars (prononcé en français : /maʁs/) est la quatrième planète du Système solaire par ordre " +
                        "croissant de la distance au Soleil et la deuxième par ordre croissant de la taille et de la " +
                        "masse. Son éloignement au Soleil est compris entre 1,381 et 1,666 au (206,6 à 249,2 millions " +
                        "de kilomètres), elle a une période orbitale de 669,58 jours martiens (686,71 jours ou 1,88 " +
                        "année terrestre)." + "\n" + "https://fr.wikipedia.org/wiki/Mars_(plan%C3%A8te)"
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
                "Jupiter est la cinquième planète du Système solaire par ordre d'éloignement au Soleil, et " +
                        "la plus grande par la taille et la masse devant Saturne, qui est comme elle une planète " +
                        "géante gazeuse. Elle est même plus volumineuse que toutes les autres planètes réunies, par " +
                        "son rayon moyen de 69 911 km, qui vaut environ onze fois celui de la Terre ; sa masse de " +
                        "1,898 × 1027 kg est elle 318 fois plus grande. Elle décrit une orbite à 779 millions " +
                        "de kilomètres du Soleil (5,2 unités astronomiques) en moyenne, sur une période de révolution " +
                        "d'un peu moins de 12 ans. La masse jovienne est par ailleurs une unité utilisée pour " +
                        "exprimer la masse d'objets substellaires tels que les naines brunes." + "\n" +
                        "https://fr.wikipedia.org/wiki/Jupiter_(plan%C3%A8te)"
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
                "Saturne est la sixième planète du Système solaire par ordre d'éloignement au Soleil, et la " +
                        "deuxième plus grande par la taille et la masse après Jupiter. C'est une planète géante " +
                        "gazeuse comme Jupiter. Son rayon moyen de 58 232 km est environ neuf fois et demi celui de " +
                        "la Terre et sa masse de 5,684 6 × 1026 kg est 95 fois plus grande. Elle orbite en moyenne " +
                        "à environ 1,4 milliard de kilomètres du Soleil (9,5 ua), sa période de révolution valant un " +
                        "peu moins de 30 années terrestres tandis que sa période de rotation est estimée à 10 h 33 " +
                        "min." + "\n" + "https://fr.wikipedia.org/wiki/Saturne_(plan%C3%A8te)"
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
                "Uranus est la septième planète du Système solaire par ordre d'éloignement du Soleil. " +
                        "Elle orbite autour de celui-ci à une distance d'environ 19,2 unités astronomiques " +
                        "(2,87 milliards de kilomètres), avec une période de révolution de 84,05 années terrestres. " +
                        "Il s'agit de la quatrième planète la plus massive du Système solaire et de la troisième plus " +
                        "grande par la taille." + "\n" + "https://fr.wikipedia.org/wiki/Uranus_(plan%C3%A8te)"
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
                "Neptune est la huitième planète par ordre d'éloignement du Soleil et la plus éloignée de " +
                        "l'étoile dans le Système solaire qui soit connue[N 2]. Elle orbite autour du Soleil à une " +
                        "distance d'environ 30,1 au (4,5 milliards de kilomètres), avec une excentricité orbitale de " +
                        "0,00859 (moitié moindre que celle de la Terre) et une période de révolution de 164,79 ans. " +
                        "C'est la troisième planète la plus massive, la quatrième plus grande par la taille — un peu " +
                        "plus massive mais un peu plus petite qu'Uranus — et la planète géante la plus dense du " +
                        "Système solaire." + "\n" + "https://fr.wikipedia.org/wiki/Neptune_(plan%C3%A8te)"
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
                "La Terre est la troisième planète par ordre d'éloignement au Soleil et la cinquième plus " +
                        "grande du Système solaire aussi bien par la masse que par le diamètre. Par ailleurs, elle est " +
                        "le seul objet céleste connu pour abriter la vie. Elle orbite autour du Soleil en 365,256 jours " +
                        "solaires — une année sidérale — et réalise une rotation sur elle-même relativement au Soleil " +
                        "en un jour sidéral (environ 23 h 56 min 4 s), soit un peu moins que son jour solaire de 24 " +
                        "heures du fait de ce déplacement autour du Soleil[a]. L'axe de rotation de la Terre possède " +
                        "une inclinaison d'environ 23°, ce qui cause l'apparition des saisons." + "\n" +
                        "https://fr.wikipedia.org/wiki/Terre"
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
                "La Lune[a], ou Terre I[b], est l'unique satellite naturel permanent de la planète Terre. " +
                        "Il s'agit du cinquième plus grand satellite naturel du Système solaire, et du plus grand des " +
                        "satellites planétaires par rapport à la taille de la planète autour de laquelle il orbite. " +
                        "C'est le deuxième satellite le plus dense du Système solaire après Io, un satellite de " +
                        "Jupiter[c]." + "\n" + "https://fr.wikipedia.org/wiki/Lune"
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
}