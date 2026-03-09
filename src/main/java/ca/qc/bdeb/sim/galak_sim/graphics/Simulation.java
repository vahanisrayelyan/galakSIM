package ca.qc.bdeb.sim.galak_sim.graphics;

import ca.qc.bdeb.sim.galak_sim.addons.Physique;
import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Simulation {

    private ArrayList<Planete> planetes = new ArrayList<>();
    private Physique physique = new Physique();

    public Simulation() {

    }

    public Planete ajouterNouvellePlanete(double x, double y, double vX, double vY, double taille, double masse) {
        planetes.add(new Planete(x, y, vX, vY, taille, masse));
        return planetes.getLast();
    }

    public void supprimerPlanete(Planete planete) {
        planetes.remove(planete);
    }

    public void update(double deltaTemps) {
        physique.effetForceGravitationelle(deltaTemps,planetes);
        for (Planete p : planetes) {
            p.update(deltaTemps);
        }
    }

    public void draw(GraphicsContext contexte) {
        contexte.clearRect(0,0,contexte.getCanvas().getWidth(),contexte.getCanvas().getHeight());
        for (Planete p:planetes){
            p.draw(contexte);
        }
    }

    public ArrayList<Planete> getPlanetes() {
        return planetes;
    }
}
