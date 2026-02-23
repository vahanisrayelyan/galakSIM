package ca.qc.bdeb.sim.galak_sim.graphics;

import javafx.scene.canvas.GraphicsContext;

public class Simulation {
    private final InterfaceGraphique gui;

    public Simulation(InterfaceGraphique gui) {
        this.gui = gui;
    }

    public void update(double deltaTemps) {

    }
    
    public void draw(GraphicsContext contexte) {
        gui.draw(contexte);
    }
}
