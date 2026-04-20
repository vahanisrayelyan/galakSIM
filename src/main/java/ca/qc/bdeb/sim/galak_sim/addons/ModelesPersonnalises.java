package ca.qc.bdeb.sim.galak_sim.addons;

import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import ca.qc.bdeb.sim.galak_sim.graphics.Simulation;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelesPersonnalises {

    private static final String DOSSIER = "modeles_perso";

    public static void sauvegarderModele(Simulation simulation, String nomModele) throws IOException {
        File dossier = new File(DOSSIER);
        if (!dossier.exists()) {
            dossier.mkdirs();
        }

        File fichier = new File(dossier, nomModele + ".txt");

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fichier), StandardCharsets.UTF_8))) {

            for (Planete p : simulation.getPlanetes()) {
                Color c = p.getCouleurOrbite();

                String couleur = c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "," + c.getOpacity();

                writer.write(
                        nettoyer(p.getNom()) + ";" +
                                p.getPosition().getX() + ";" +
                                p.getPosition().getY() + ";" +
                                p.getVelocite().getX() + ";" +
                                p.getVelocite().getY() + ";" +
                                p.getTaille().getX() + ";" +
                                p.getMasse() + ";" +
                                couleur
                );
                writer.newLine();
            }
        }
    }

    public static void chargerModele(Simulation simulation, String nomModele) throws IOException {
        File fichier = new File(DOSSIER, nomModele + ".txt");

        if (!fichier.exists()) {
            throw new FileNotFoundException("Le modèle n'existe pas : " + fichier.getAbsolutePath());
        }

        simulation.viderPlanetes();
        simulation.reinitialiserVue(null);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fichier), StandardCharsets.UTF_8))) {

            String ligne;
            while ((ligne = reader.readLine()) != null) {
                if (ligne.isBlank()) {
                    continue;
                }

                String[] parties = ligne.split(";");
                if (parties.length < 8) {
                    continue;
                }

                String nom = parties[0];
                double x = Double.parseDouble(parties[1]);
                double y = Double.parseDouble(parties[2]);
                double vX = Double.parseDouble(parties[3]);
                double vY = Double.parseDouble(parties[4]);
                double taille = Double.parseDouble(parties[5]);
                double masse = Double.parseDouble(parties[6]);

                String[] rgba = parties[7].split(",");
                Color couleur = Color.WHITE;
                if (rgba.length == 4) {
                    couleur = new Color(
                            Double.parseDouble(rgba[0]),
                            Double.parseDouble(rgba[1]),
                            Double.parseDouble(rgba[2]),
                            Double.parseDouble(rgba[3])
                    );
                }

                simulation.ajouterNouvellePlanete(
                        x, y,
                        vX, vY,
                        taille, masse,
                        nom,
                        null,
                        couleur
                );
            }
        }
    }

    private static String nettoyer(String texte) {
        return texte.replace(";", ",").trim();
    }
    public static void supprimerTousLesModeles() {
        File dossier = new File("modeles_perso");

        if (!dossier.exists()) {
            return;
        }

        for (File fichier : dossier.listFiles()) {
            if (fichier.isFile()) {
                fichier.delete();
            }
        }

        dossier.delete(); // supprime le dossier lui-même
    }
    public static boolean modeleExiste(String nomModele) {
        File fichier = new File(DOSSIER, nomModele + ".txt");
        return fichier.exists();
    }
    public static List<String> listerModeles() {
        File dossier = new File(DOSSIER);
        List<String> nomsModeles = new ArrayList<>();

        if (!dossier.exists() || !dossier.isDirectory()) {
            return nomsModeles;
        }

        File[] fichiers = dossier.listFiles((dir, nom) -> nom.toLowerCase().endsWith(".txt"));
        if (fichiers == null) {
            return nomsModeles;
        }

        Arrays.sort(fichiers, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));

        for (File fichier : fichiers) {
            String nom = fichier.getName();
            nomsModeles.add(nom.substring(0, nom.length() - 4)); // enlève .txt
        }

        return nomsModeles;
    }

    public static void supprimerModele(String nomModele) throws IOException {
        File fichier = new File(DOSSIER, nomModele + ".txt");

        if (!fichier.exists()) {
            throw new FileNotFoundException("Le modèle n'existe pas : " + fichier.getAbsolutePath());
        }

        if (!fichier.delete()) {
            throw new IOException("Impossible de supprimer le modèle : " + nomModele);
        }
    }
}