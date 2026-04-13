package ca.qc.bdeb.sim.galak_sim.astres;

import javafx.geometry.Point2D;

public class AstreFantome extends Astre {
    private Point2D pos;
    private Point2D vel;
    private Point2D acc;

    public AstreFantome(Planete p) {
        // On appelle le constructeur parent avec les données réelles en mètres
        super(p.getPosition().getX(), p.getPosition().getY(),
                p.getVelocite().getX(), p.getVelocite().getY(),
                p.getTaille().getX(), p.getMasse());

        this.pos = p.getPosition();
        this.vel = p.getVelocite();
        this.acc = Point2D.ZERO;
    }

    public void update(double dt) {
        // 1. v = v + a * dt (Tout en mètres et secondes)
        this.vel = this.vel.add(this.acc.multiply(dt));

        // 2. p = p + v * dt (SANS DIVISION par scale)
        // On reste en mètres, la caméra gérera l'affichage plus tard
        this.pos = this.pos.add(this.vel.multiply(dt));
    }

    @Override public Point2D getPosition() { return pos; }
    @Override public Point2D getAcceleration() { return acc; }
    @Override public void setAcceleration(Point2D acc) { this.acc = acc; }
}