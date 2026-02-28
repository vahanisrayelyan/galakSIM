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
     * Image utilisée pour l'affichage.
     */
    protected Image image;

    /**
     * Constructeur d'un objet du jeu.
     */

    public Astre(double x, double y, double vX, double vY, double taille) {
        this.position = new Point2D(x, y);
        this.velocite = new Point2D(vX, vY);
        this.taille = new Point2D(taille, taille);
        this.acceleration = Point2D.ZERO;
    }

    /**
     * Mise à jour de la physique selon le temps.
     */
    protected void update(double dt) {
        // v = v + a·dt
        velocite = velocite.add(acceleration.multiply(dt));
        // p = p + v·dt
        position = position.add(velocite.multiply(dt));
    }

    /**
     * Dessine l'objet.
     */
    public void draw(GraphicsContext gc) {
    }

    public Point2D getPosition() {
        return position;
    }

    public Point2D getTaille() {
        return taille;
    }
}
