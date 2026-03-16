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

        contexte.save();

        // Centre de l'écran + déplacement caméra + zoom
        contexte.translate(largeur / 2.0, hauteur / 2.0);
        contexte.scale(zoom, zoom);
        contexte.translate(offsetX, offsetY);

        for (Planete p : planetes) {
            p.draw(contexte);
        }

        contexte.restore();
    }

    public void deplacerCamera(double dxEcran, double dyEcran) {
        planeteSuivie = null;
        offsetX += dxEcran / zoom;
        offsetY += dyEcran / zoom;
    }

    public void zoomer(double facteur, double sourisX, double sourisY, double largeurCanvas, double hauteurCanvas) {
        double ancienZoom = zoom;
        zoom *= facteur;

        zoom = Math.max(0.1, Math.min(zoom, 20.0));

        double facteurReel = zoom / ancienZoom;

        // Ajuste l'offset pour que le point sous la souris reste fixe
        double xMondeAvant = (sourisX - largeurCanvas / 2.0) / ancienZoom - offsetX;
        double yMondeAvant = (sourisY - hauteurCanvas / 2.0) / ancienZoom - offsetY;

        double xMondeApres = (sourisX - largeurCanvas / 2.0) / zoom - offsetX;
        double yMondeApres = (sourisY - hauteurCanvas / 2.0) / zoom - offsetY;

        offsetX += (xMondeApres - xMondeAvant);
        offsetY += (yMondeApres - yMondeAvant);
    }

    public Point2D ecranVersMonde(double xEcran, double yEcran, double largeurCanvas, double hauteurCanvas) {
        double xMonde = (xEcran - largeurCanvas / 2.0) / zoom - offsetX;
        double yMonde = (yEcran - hauteurCanvas / 2.0) / zoom - offsetY;
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
}