package ca.qc.bdeb.sim.galak_sim.graphics;

import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Simulation {
    private ArrayList<Planete> planetes = new ArrayList<>();

    public Simulation() {
        planetes.add(new Planete(0, 0, 20, 20));
    }

    public void update(double deltaTemps) {

    }
    
    public void draw(GraphicsContext contexte) {

    }
}
