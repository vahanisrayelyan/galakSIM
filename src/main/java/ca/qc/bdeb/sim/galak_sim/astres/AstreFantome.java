package ca.qc.bdeb.sim.galak_sim.astres;

import javafx.geometry.Point2D;

public class AstreFantome extends Astre {
    private Point2D pos;
    private Point2D vel;
    private Point2D acc;

    public AstreFantome(Planete p) {
        super(p.getPosition().getX(), p.getPosition().getY(),
                p.getVelocite().getX(), p.getVelocite().getY(),
                p.getTaille().getX(), p.getMasse());

        this.pos = p.getPosition();
        this.vel = p.getVelocite();
        this.acc = Point2D.ZERO;
    }

    public void update(double dt) {
        this.vel = this.vel.add(this.acc.multiply(dt));
        this.pos = this.pos.add(this.vel.multiply(dt));
    }

    @Override
    public Point2D getPosition() {
        return pos;
    }

    @Override
    public Point2D getAcceleration() {
        return acc;
    }

    @Override
    public void setAcceleration(Point2D acc) {
        this.acc = acc;
    }
}