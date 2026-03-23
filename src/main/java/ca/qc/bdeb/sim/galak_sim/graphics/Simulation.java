package ca.qc.bdeb.sim.galak_sim.graphics;

import ca.qc.bdeb.sim.galak_sim.addons.Physique;
import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Simulation {

    private ArrayList<Planete> planetes = new ArrayList<>();
    private Physique physique = new Physique();

    private double zoom = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;
    private final double ECHELLE_AFFICHAGE = 1e-2;

    private Planete planeteSuivie = null;

    public Simulation() {
    }

    public Planete ajouterNouvellePlanete(double x, double y, double vX, double vY, double taille, double masse, String nom) {
        planetes.add(new Planete(x, y, vX, vY, taille, masse, nom));
        return planetes.getLast();
    }

    public void supprimerPlanete(Planete planete) {
        if (planete == planeteSuivie) {
            planeteSuivie = null;
        }
        planetes.remove(planete);
    }

    public void update(double deltaTemps) {
        physique.effetForceGravitationelle(deltaTemps, planetes);
        for (Planete p : planetes) {
            p.update(deltaTemps);
        }

        if (planeteSuivie != null) {
            // On force l'offset à correspondre à la position de la planète
            this.offsetX = -planeteSuivie.getPosition().getX();
            this.offsetY = -planeteSuivie.getPosition().getY();
        }
    }

    public void draw(GraphicsContext contexte) {
        double largeur = contexte.getCanvas().getWidth();
        double hauteur = contexte.getCanvas().getHeight();

        contexte.clearRect(0, 0, largeur, hauteur);

        for (Planete p : planetes) {
            double xEcran = largeur / 2.0 + (p.getPosition().getX() + offsetX) * zoom * ECHELLE_AFFICHAGE;
            double yEcran = hauteur / 2.0 + (p.getPosition().getY() + offsetY) * zoom * ECHELLE_AFFICHAGE;

            p.draw(contexte, xEcran, yEcran, zoom,
                    largeur, hauteur, offsetX, offsetY, ECHELLE_AFFICHAGE);
        }
    }

    public void deplacerCamera(double dxEcran, double dyEcran) {
        planeteSuivie = null;

        double facteur = zoom * ECHELLE_AFFICHAGE;

        offsetX += dxEcran / facteur;
        offsetY += dyEcran / facteur;
    }

    public void zoomer(double facteurZoom, double sourisX, double sourisY, double largeurCanvas, double hauteurCanvas) {
        double ancienZoom = zoom;
        zoom *= facteurZoom;

        zoom = Math.max(0.1, Math.min(zoom, 20.0));

        double ancienFacteur = ancienZoom * ECHELLE_AFFICHAGE;
        double nouveauFacteur = zoom * ECHELLE_AFFICHAGE;

        double xMondeAvant = (sourisX - largeurCanvas / 2.0) / ancienFacteur - offsetX;
        double yMondeAvant = (sourisY - hauteurCanvas / 2.0) / ancienFacteur - offsetY;

        double xMondeApres = (sourisX - largeurCanvas / 2.0) / nouveauFacteur - offsetX;
        double yMondeApres = (sourisY - hauteurCanvas / 2.0) / nouveauFacteur - offsetY;

        offsetX += (xMondeApres - xMondeAvant);
        offsetY += (yMondeApres - yMondeAvant);
    }

    public Point2D ecranVersMonde(double xEcran, double yEcran, double largeurCanvas, double hauteurCanvas) {
        double facteur = zoom * ECHELLE_AFFICHAGE;

        double xMonde = (xEcran - largeurCanvas / 2.0) / facteur - offsetX;
        double yMonde = (yEcran - hauteurCanvas / 2.0) / facteur - offsetY;

        return new Point2D(xMonde, yMonde);
    }

    public void reinitialiserVue() {
        zoom = 1.0;
        offsetX = 0;
        offsetY = 0;
    }

    public void centrerSur(Planete p, double valeurZoom) {
        this.planeteSuivie = p;
        this.zoom = valeurZoom;
    }

    public ArrayList<Planete> getPlanetes() {
        return planetes;
    }

    public double getZoom() {
        return zoom;
    }
    public int getSizeListPlanetes(){
        return planetes.size();
    }
    public String dernierNomPlanete() {
        return planetes.getLast().getNom();
    }
}