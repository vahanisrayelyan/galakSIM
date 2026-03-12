package ca.qc.bdeb.sim.galak_sim.astres;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;



public class PointOrbite {
    private double x;
    private double y;
    public PointOrbite(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
}
