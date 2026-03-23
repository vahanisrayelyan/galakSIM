package ca.qc.bdeb.sim.galak_sim.astres;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Astre {
    /**
     * Position de l'objet (coin haut-gauche).
     */
    protected Point2D position;
    /**
     * Vélocité de l'objet.
     */
    protected Point2D velocite;
    /**
     * Accélération de l'objet.
     */
    protected Point2D acceleration;
    /**
     * Taille de l'objet : largeur et hauteur.
     */
    protected Point2D taille;
    /**
     * Masse de l'objet
     */
    protected double masse;
    /**
     * Image utilisée pour l'affichage.
     */
    protected Image image;

    protected double scale = 1e7;

    /**
     * Constructeur d'un objet du jeu.
     */

    public Astre(double x, double y, double vX, double vY, double taille, double masse) {
        this.position = new Point2D(x, y);
        this.velocite = new Point2D(vX, vY);
        this.taille = new Point2D(taille, taille);
        this.masse = masse;
        this.acceleration = Point2D.ZERO;
    }

    /**
     * Mise à jour de la physique selon le temps.
     */
    protected void update(double dt) {
        // v = v + a·dt
        velocite = velocite.add(acceleration.multiply(dt));
        // p = p + v·dt
        double ajoutPositionx = (velocite.getX() * dt) / scale;
        double ajoutPositiony = (velocite.getY() * dt) / scale;

        Point2D ajoutPosition = new Point2D(ajoutPositionx,ajoutPositiony);

        position = position.add(ajoutPosition);
    }

    /**
     * Dessine l'objet.
     */
    public void draw(GraphicsContext gc) {
    }

    public Point2D getPosition() {
        return position;
    }

    public Point2D getVelocite() {return velocite;}

    public Point2D getAcceleration() {return acceleration;}

    public double getMasse() {return masse;}

//    public void setPosition(Point2D position) {this.position = position;}
//
//    public void setVelocite(Point2D velocite) {this.velocite = velocite;}

    public void setAcceleration(Point2D acceleration) {this.acceleration = acceleration;}

    public Point2D getTaille() {
        return taille;
    }
}
