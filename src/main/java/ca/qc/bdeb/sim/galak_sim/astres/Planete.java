package ca.qc.bdeb.sim.galak_sim.astres;

import ca.qc.bdeb.sim.galak_sim.MainJavaFX;
import ca.qc.bdeb.sim.galak_sim.graphics.Simulation;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Planete extends Astre{
    private Color color;

    public Planete(double x, double y, double vX, double vY, double taille) {
        super(x,y,vX, vY, taille);
        this.color = couleurAleatoire();
    }

    public void update(double deltaTemps) {
        super.update(deltaTemps);
    }

    public void draw(GraphicsContext contexte) {
        contexte.setFill(color);

        double w = taille.getX();
        double h = taille.getY();

        contexte.fillOval(position.getX() - w/2, position.getY() - h/2, w, h);
    }
    public static Color couleurAleatoire() {
        return Color.color(Math.random(), Math.random(), Math.random());
    }
}
