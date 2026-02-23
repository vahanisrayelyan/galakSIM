package ca.qc.bdeb.sim.galak_sim.graphics;

import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Simulation {
    private final InterfaceGraphique gui;
    private ArrayList<Planete> planetes = new ArrayList<>();

    public Simulation(InterfaceGraphique gui,double w,double h) {
        this.gui = gui;
    }

    public void ajouterNouvellePlanete() {
        planetes.add(new Planete(0, 0, 20, 20));
    }

    public void update(double deltaTemps) {
gui.update(deltaTemps);
    }
    
    public void draw(GraphicsContext contexte, double width, double height) {
        gui.draw(contexte, width, height);
    }
}
