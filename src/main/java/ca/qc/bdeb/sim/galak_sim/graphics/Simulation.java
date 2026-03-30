package ca.qc.bdeb.sim.galak_sim.graphics;

import ca.qc.bdeb.sim.galak_sim.addons.Collision;
import ca.qc.bdeb.sim.galak_sim.addons.Physique;
import ca.qc.bdeb.sim.galak_sim.astres.AstreFantome;
import ca.qc.bdeb.sim.galak_sim.astres.Orbite;
import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import ca.qc.bdeb.sim.galak_sim.astres.PointOrbite;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private ArrayList<Planete> planetes = new ArrayList<>();
    private Physique physique = new Physique();
    private Collision collision = new Collision();

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
        physique.effetForceGravitationelle(planetes);
        for (Planete p : planetes) {
            p.update(deltaTemps);
        }

        if (planeteSuivie != null) {
            // On force l'offset à correspondre à la position de la planète
            this.offsetX = -planeteSuivie.getPosition().getX();
            this.offsetY = -planeteSuivie.getPosition().getY();
        }

        planetes = collision.verificationCollision(planetes);

        calculerPredictions(deltaTemps);
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

    public void calculerPredictions(double deltaTemps) {
        if (planetes.isEmpty()) return;

        final double G = 6.67430e-11; // même valeur que Physique
        int nbPointsAffiche = 300;
        double dtPred = deltaTemps;

        List<AstreFantome> fantomes = new ArrayList<>();
        for (Planete p : planetes) {
            fantomes.add(new AstreFantome(p));
        }

        List<List<PointOrbite>> trajectoires = new ArrayList<>();
        for (int i = 0; i < planetes.size(); i++) {
            trajectoires.add(new ArrayList<>());
        }

        for (int etape = 0; etape < nbPointsAffiche; etape++) {
            // Calcul des accélérations (accumulées correctement)
            double[] ax = new double[fantomes.size()];
            double[] ay = new double[fantomes.size()];

            for (int i = 0; i < fantomes.size(); i++) {
                for (int j = i + 1; j < fantomes.size(); j++) {
                    AstreFantome f1 = fantomes.get(i);
                    AstreFantome f2 = fantomes.get(j);

                    double dx = f2.x - f1.x;
                    double dy = f2.y - f1.y;
                    double distSq = dx * dx + dy * dy;
                    double dist = Math.sqrt(distSq);

                    if (dist > 10) {
                        double force = G / distSq;
                        double fx = force * (dx / dist);
                        double fy = force * (dy / dist);

                        // Fab = -Fba, comme dans Physique
                        ax[i] += fx * f2.masse;
                        ay[i] += fy * f2.masse;
                        ax[j] -= fx * f1.masse;
                        ay[j] -= fy * f1.masse;
                    }
                }
            }

            // Application des accélérations et enregistrement des positions
            for (int i = 0; i < fantomes.size(); i++) {
                AstreFantome f = fantomes.get(i);
                f.vx += (ax[i] / f.masse) * dtPred;
                f.vy += (ay[i] / f.masse) * dtPred;
                f.x += f.vx * dtPred;
                f.y += f.vy * dtPred;
                trajectoires.get(i).add(new PointOrbite(f.x, f.y));
            }
        }

        for (int i = 0; i < planetes.size(); i++) {
            Orbite nouvellePrediction = new Orbite();
            nouvellePrediction.ajouterPointOrbitePrediction(trajectoires.get(i));
            planetes.get(i).setPredictionOrbitePlanete(nouvellePrediction);
        }
    }

    public void deplacerCamera(double dxEcran, double dyEcran) {
        planeteSuivie = null;
        offsetX += dxEcran / zoom;
        offsetY += dyEcran / zoom;
    }

    public void zoomer(double facteurZoom, double sourisX, double sourisY, double largeurCanvas, double hauteurCanvas) {
        double ancienZoom = zoom;
        zoom *= facteurZoom;

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

    public int getSizeListPlanetes() {
        return planetes.size();
    }

    public String dernierNomPlanete() {
        return planetes.getLast().getNom();
    }
}