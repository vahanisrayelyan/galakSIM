package ca.qc.bdeb.sim.galak_sim.graphics;

import ca.qc.bdeb.sim.galak_sim.addons.Collision;
import ca.qc.bdeb.sim.galak_sim.addons.Physique;
import ca.qc.bdeb.sim.galak_sim.astres.Orbite;
import ca.qc.bdeb.sim.galak_sim.addons.Vecteurs;
import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private ArrayList<Planete> planetes = new ArrayList<>();
    private Physique physique = new Physique();
    private Collision collision = new Collision();
    private Vecteurs vecteurs;
    private boolean afficherPrediction = false;
    private Camera camera = new Camera();

    public Simulation(Vecteurs vecteurs) {
        this.vecteurs = vecteurs;
    }

    public Planete ajouterNouvellePlanete(double x, double y, double vX, double vY, double taille, double masse, String nom) {
        planetes.add(new Planete(x, y, vX, vY, taille, masse, nom));
        return planetes.getLast();
    }

    public void supprimerPlanete(Planete planete) {
        if (planete == camera.getPlaneteSuivie()) {
            camera.arreterSuivi();
        }
        planetes.remove(planete);
    }

    public void update(double deltaTemps) {
        physique.effetForceGravitationelle(planetes);

        for (Planete p : planetes) {
            p.update(deltaTemps);
        }

        vecteurs.setPlanete(planetes);
        camera.mettreAJourSuivi();

        planetes = collision.verificationCollision(planetes);
        collision.updateExplosions();
    }

    public void draw(GraphicsContext contexte) {
        double largeur = contexte.getCanvas().getWidth();
        double hauteur = contexte.getCanvas().getHeight();

        contexte.clearRect(0, 0, largeur, hauteur);
        contexte.save();

        contexte.translate(largeur / 2.0, hauteur / 2.0);
        contexte.scale(camera.getZoom(), camera.getZoom());
        contexte.translate(camera.getOffsetX(), camera.getOffsetY());

        for (Planete p : planetes) {
            p.draw(contexte, afficherPrediction);
        }
        vecteurs.draw(contexte);

        contexte.restore();
    }

    public void calculerPredictions() {
        if (planetes.isEmpty()) return;

        List<List<Point2D>> trajectoires = physique.calculerPredictions(planetes);

        for (int i = 0; i < planetes.size(); i++) {
            Orbite nouvellePrediction = new Orbite();
            nouvellePrediction.ajouterPointOrbitePrediction(trajectoires.get(i));
            planetes.get(i).setPredictionOrbitePlanete(nouvellePrediction);
        }
    }

    public void deplacerCamera(double dx, double dy) {
        camera.deplacer(dx, dy);
    }

    public void zoomer(double facteurZoom, double sourisX, double sourisY, double largeurCanvas, double hauteurCanvas) {
        camera.zoomer(facteurZoom, sourisX, sourisY, largeurCanvas, hauteurCanvas);
    }

    public Point2D ecranVersMonde(double xEcran, double yEcran, double largeurCanvas, double hauteurCanvas) {
        return camera.ecranVersMonde(xEcran, yEcran, largeurCanvas, hauteurCanvas);
    }

    public void reinitialiserVue() {
        camera.reinitialiser();
    }

    public void centrerSur(Planete p, double valeurZoom) {
        camera.suivrePlanete(p, valeurZoom);
    }

    public void viderPlanetes() {
        planetes.clear();
        camera.reinitialiser();
    }

    public ArrayList<Planete> getPlanetes() {
        return planetes;
    }

    public double getZoom() {
        return camera.getZoom();
    }

    public int getSizeListPlanetes() {
        return planetes.size();
    }

    public void setAfficherPrediction(boolean afficher) {
        this.afficherPrediction = afficher;
    }
}