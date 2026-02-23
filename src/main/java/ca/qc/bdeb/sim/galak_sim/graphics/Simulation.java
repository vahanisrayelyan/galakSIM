package ca.qc.bdeb.sim.galak_sim.graphics;

import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Simulation {

    private ArrayList<Planete> planetes = new ArrayList<>();

    public Simulation() {

    }

    public void ajouterNouvellePlanete() {
        planetes.add(new Planete());
        System.out.println("Planete ajoutée");
    }

    public void update(double deltaTemps) {
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
}
