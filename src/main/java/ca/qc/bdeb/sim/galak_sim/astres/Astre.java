package ca.qc.bdeb.sim.galak_sim.astres;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Astre {
    /** Position de l'objet (coin haut-gauche). */
    protected Point2D position;
    /** Vélocité de l'objet. */
    protected Point2D velocite;
    /** Accélération de l'objet. */
    protected Point2D acceleration;
    /** Taille de l'objet : largeur et hauteur. */
    protected Point2D taille;
    /** Image utilisée pour l'affichage. */
    protected Image image;
    /**  Constructeur d'un objet du jeu. */

    public Astre(double x, double y, double largeur, double hauteur) {
        this.position = new Point2D(x, y);
        this.taille = new Point2D(largeur, hauteur);
        this.velocite = Point2D.ZERO;
        this.acceleration = Point2D.ZERO;
    }

    /** Mise à jour de la physique selon le temps. */
    protected void update(double dt) {
        // v = v + a·dt
        velocite = velocite.add(acceleration.multiply(dt));
        // p = p + v·dt
        position = position.add(velocite.multiply(dt));
    }

    /** Dessine l'objet. */
    public void draw(GraphicsContext gc) {
    }
}
