package ca.qc.bdeb.sim.galak_sim.graphics;

import ca.qc.bdeb.sim.galak_sim.addons.Collision;
import ca.qc.bdeb.sim.galak_sim.addons.Physique;
import ca.qc.bdeb.sim.galak_sim.addons.Vecteurs;
import ca.qc.bdeb.sim.galak_sim.astres.Orbite;
import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private ArrayList<Planete> planetes = new ArrayList<>();
    private final Physique physique = new Physique();
    private final Collision collision = new Collision();
    private final Vecteurs vecteurs;
    private boolean afficherPrediction = false;
    private final Camera camera = new Camera();

    public Simulation(Vecteurs vecteurs) {
        this.vecteurs = vecteurs;
        this.vecteurs.setPlanete(planetes);
    }

    public Planete ajouterNouvellePlanete(double x, double y, double vX, double vY,
                                          double taille, double masse, String nom,
                                          Image image, Color color) {
        Planete nouvellePlanete = new Planete(x, y, vX, vY, taille, masse, nom, image, color);
        planetes.add(nouvellePlanete);
        return nouvellePlanete;
    }

    public void supprimerPlanete(Planete planete) {
        if (planete == camera.getPlaneteSuivie()) {
            camera.arreterSuivi();
        }
        planetes.remove(planete);
    }

    public void update(double deltaTemps) {
        if (planetes.isEmpty()) {
            return;
        }

        double pasMax = 1000.0; // secondes simulées max par sous-étape
        int nbSousEtapes = Math.max(1, (int) Math.ceil(deltaTemps / pasMax));

        if (nbSousEtapes > 1000) {
            nbSousEtapes = 1000;
        }

        double sousDeltaTemps = deltaTemps / nbSousEtapes;

        for (int etape = 0; etape < nbSousEtapes; etape++) {
            physique.effetForceGravitationelle(planetes);

            for (Planete p : planetes) {
                p.update(sousDeltaTemps);
            }

            planetes = collision.verificationCollision(planetes);
            collision.updateExplosions();
        }

        vecteurs.setPlanete(planetes);
        camera.mettreAJourSuivi();
    }

    public void draw(GraphicsContext contexte) {
        double largeur = contexte.getCanvas().getWidth();
        double hauteur = contexte.getCanvas().getHeight();

        contexte.clearRect(0, 0, largeur, hauteur);

        for (Planete p : planetes) {
            p.draw(contexte, camera, largeur, hauteur, afficherPrediction);
        }

        // À corriger aussi dans Vecteurs si cette classe dessine encore
        // en coordonnées monde directes.
        vecteurs.draw(contexte, camera, largeur, hauteur);

        collision.draw(contexte, camera, largeur, hauteur);
    }

    public void calculerPredictions() {
        if (planetes.isEmpty()) return;

        // On envoie le zoom actuel pour adapter la longueur de la ligne
        List<List<Point2D>> trajectoires = physique.calculerPredictions(planetes, camera.getZoom());

        for (int i = 0; i < planetes.size() && i < trajectoires.size(); i++) {
            Orbite nouvellePrediction = new Orbite();
            nouvellePrediction.ajouterPointOrbitePrediction(trajectoires.get(i));
            planetes.get(i).setPredictionOrbitePlanete(nouvellePrediction);
        }
    }

    public void deplacerCamera(double dx, double dy) {
        camera.deplacer(dx, dy);
    }

    public void zoomer(double facteurZoom, double sourisX, double sourisY,
                       double largeurCanvas, double hauteurCanvas) {
        camera.zoomer(facteurZoom, sourisX, sourisY, largeurCanvas, hauteurCanvas);
        vecteurs.setScaleZoom(camera.getZoom());
    }

    public Point2D ecranVersMonde(double xEcran, double yEcran,
                                  double largeurCanvas, double hauteurCanvas) {
        return camera.ecranVersMonde(xEcran, yEcran, largeurCanvas, hauteurCanvas);
    }

    public void reinitialiserVue(String zoom1) {
        camera.reinitialiser(zoom1);
    }

    public void centrerSur(Planete p, double valeurZoom) {
        camera.suivrePlanete(p, valeurZoom);
    }

    public void viderPlanetes() {
        planetes.clear();
        camera.reinitialiser(null);
    }

    public ArrayList<Planete> getPlanetes() {
        return planetes;
    }

    public double getZoom() {
        return camera.getZoom();
    }

    public Camera getCamera() {
        return camera;
    }

    public int getSizeListPlanetes() {
        return planetes.size();
    }

    public void setAfficherPrediction(boolean afficher) {
        this.afficherPrediction = afficher;
    }

    public boolean isAfficherPrediction() {
        return afficherPrediction;
    }
}