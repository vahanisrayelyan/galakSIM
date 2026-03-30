package ca.qc.bdeb.sim.galak_sim.addons;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Explosion {
    private double x;
    private double y;
    private int duree;

    private static final Image imageExplosion =
            new Image(Explosion.class.getResourceAsStream("/explosion.png"));

    public Explosion(double x, double y, int duree) {
        this.x = x;
        this.y = y;
        this.duree = duree;
    }

    public void update() {
        duree--;
    }

    public boolean estTerminee() {
        return duree <= 0;
    }

    public void draw(GraphicsContext contexte) {
        contexte.drawImage(
                imageExplosion,
                x - imageExplosion.getWidth() / 2,
                y - imageExplosion.getHeight() / 2
        );
    }
}