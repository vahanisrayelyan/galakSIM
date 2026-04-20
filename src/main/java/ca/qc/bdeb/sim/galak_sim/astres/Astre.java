package ca.qc.bdeb.sim.galak_sim.astres;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Astre {

    protected Point2D position;
    protected Point2D velocite;
    protected Point2D acceleration;
    protected Point2D taille;
    protected Point2D Fg;
    protected double masse;



    public void setFg(double Fgx, double Fgy) {
        Fg = new Point2D(Fgx,Fgy);
    }

    public Point2D getFg() {
        return Fg;
    }

    public Astre(double x, double y, double vX, double vY, double taille, double masse) {
        this.position = new Point2D(x, y);
        this.velocite = new Point2D(vX, vY);
        this.taille = new Point2D(taille, taille);
        this.masse = masse;
        this.acceleration = Point2D.ZERO;
        this.Fg = Point2D.ZERO;
    }

    public void update(double dt) {
        // Euler simple
        velocite = velocite.add(acceleration.multiply(dt));

        position = position.add(
                velocite.getX() * dt,
                velocite.getY() * dt
        );

    }

    public void draw(GraphicsContext gc) {
    }

    public Point2D getPosition() {
        return position;
    }

    public Point2D getVelocite() {
        return velocite;
    }

    public Point2D getAcceleration() {
        return acceleration;
    }

    public double getMasse() {
        return masse;
    }

    public void setAcceleration(Point2D acceleration) {
        this.acceleration = acceleration;
    }

    public Point2D getTaille() {
        return taille;
    }
}