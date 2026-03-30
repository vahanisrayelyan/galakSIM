package ca.qc.bdeb.sim.galak_sim.astres;

public class AstreFantome {
    public double x;
    public double y;
    public double vx;
    public double vy;
    public double ax;
    public double ay;
    public double masse;

    public AstreFantome(Planete p) {
        this.x = p.getPosition().getX();
        this.y = p.getPosition().getY();
        this.vx = p.getVelocite().getX();
        this.vy = p.getVelocite().getY();
        this.ax = p.getAcceleration().getX();
        this.ay = p.getAcceleration().getY();
        this.masse = p.getMasse();
    }
}