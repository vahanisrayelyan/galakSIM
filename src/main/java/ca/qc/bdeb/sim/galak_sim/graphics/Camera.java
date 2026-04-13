package ca.qc.bdeb.sim.galak_sim.graphics;

import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import javafx.geometry.Point2D;

public class Camera {
    private double zoom = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;
    private Planete planeteSuivie = null;

    public void suivrePlanete(Planete p, double valeurZoom) {
        this.planeteSuivie = p;
        this.zoom = valeurZoom;
    }

    public void arreterSuivi() {
        planeteSuivie = null;
    }

    public void mettreAJourSuivi() {
        if (planeteSuivie != null) {
            this.offsetX = -planeteSuivie.getPosition().getX();
            this.offsetY = -planeteSuivie.getPosition().getY();
        }
    }

    public void deplacer(double dxEcran, double dyEcran) {
        planeteSuivie = null;
        offsetX += dxEcran / zoom;
        offsetY += dyEcran / zoom;
    }

    public void zoomer(double facteurZoom, double sourisX, double sourisY, double largeurCanvas, double hauteurCanvas) {
        double ancienZoom = zoom;
        zoom *= facteurZoom;
        zoom = Math.max(0.1, Math.min(zoom, 20.0));

        double xMondeAvant = (sourisX - largeurCanvas / 2.0) / ancienZoom - offsetX;
        double yMondeAvant = (sourisY - hauteurCanvas / 2.0) / ancienZoom - offsetY;

        double xMondeApres = (sourisX - largeurCanvas / 2.0) / zoom - offsetX;
        double yMondeApres = (sourisY - hauteurCanvas / 2.0) / zoom - offsetY;

        offsetX += (xMondeApres - xMondeAvant);
        offsetY += (yMondeApres - yMondeAvant);
    }

    public void reinitialiser() {
        zoom = 1.0;
        offsetX = 0;
        offsetY = 0;
        planeteSuivie = null;
    }

    public Point2D ecranVersMonde(double xEcran, double yEcran, double largeurCanvas, double hauteurCanvas) {
        double xMonde = (xEcran - largeurCanvas / 2.0) / zoom - offsetX;
        double yMonde = (yEcran - hauteurCanvas / 2.0) / zoom - offsetY;
        return new Point2D(xMonde, yMonde);
    }

    public double getZoom() {
        return zoom;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public Planete getPlaneteSuivie() {
        return planeteSuivie;
    }
}