package ca.qc.bdeb.sim.galak_sim.astres;

import javafx.geometry.Point2D;

public class AstreFantome extends Astre {
    // On crée des variables locales pour ne pas toucher aux vraies planètes
    private Point2D pos;
    private Point2D vel;
    private Point2D acc;
    private final double scale = 1e9;

    public AstreFantome(Planete p) {
        super(p.getPosition().getX(), p.getPosition().getY(),
                p.getVelocite().getX(), p.getVelocite().getY(),
                p.getTaille().getX(), p.getMasse());

        this.pos = p.getPosition();
        this.vel = p.getVelocite(); // Récupère la vitesse (essentiel pour le mode libre)
        this.acc = Point2D.ZERO;    // On part à zéro, Physique va la calculer
    }

    // CETTE MÉTHODE EST CRUCIALE
    public void update(double dt) {
        // v = v + a * dt
        this.vel = this.vel.add(this.acc.multiply(dt));

        // p = p + (v * dt) / scale (conversion mètres -> pixels)
        double dx = (this.vel.getX() * dt) / scale;
        double dy = (this.vel.getY() * dt) / scale;
        this.pos = this.pos.add(dx, dy);
    }

    @Override public Point2D getPosition() { return pos; }
    @Override public Point2D getAcceleration() { return acc; }
    @Override public void setAcceleration(Point2D acc) { this.acc = acc; }
}