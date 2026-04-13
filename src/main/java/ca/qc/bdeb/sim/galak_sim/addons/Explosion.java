package ca.qc.bdeb.sim.galak_sim.addons;

public class Explosion {
    private double x;
    private double y;
    private double rayon;
    private int duree;

    public Explosion(double x, double y, double rayon) {
        this.x = x;
        this.y = y;
        this.rayon = rayon;
        this.duree = 30; // durée par défaut
    }

    public void update() {
        duree--;
    }

    public boolean estTerminee() {
        return duree <= 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRayon() {
        return rayon;
    }
}