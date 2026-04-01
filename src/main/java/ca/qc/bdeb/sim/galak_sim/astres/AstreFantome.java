package ca.qc.bdeb.sim.galak_sim.astres;

public class AstreFantome extends Astre {

    public AstreFantome(Planete p) {
        super(
                p.getPosition().getX(),
                p.getPosition().getY(),
                p.getVelocite().getX(),
                p.getVelocite().getY(),
                p.getTaille().getX(),
                p.getMasse()
        );
        this.acceleration = p.getAcceleration();
    }
}