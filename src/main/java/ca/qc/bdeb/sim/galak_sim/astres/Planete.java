package ca.qc.bdeb.sim.galak_sim.astres;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Planete extends Astre {

    private Image image;
    private String nom;

    private static final String[] IMAGES = {
            "/planetes/planete1.png",
            "/planetes/planete2.png",
            "/planetes/planete3.png",
            "/planetes/planete4.png",
            "/planetes/planete5.png",
            "/planetes/planete6.png",
            "/planetes/planete7.png",
            "/planetes/planete8.png",
    };

    public Planete(double x, double y, double vX, double vY, double taille, double masse, String nom) {
        super(x, y, vX, vY, taille, masse);
        this.image = imageAleatoire();
        this.nom = nom;
    }

    public void update(double deltaTemps) {
        super.update(deltaTemps);
    }

    public void draw(GraphicsContext contexte) {

        double w = taille.getX();
        double h = taille.getY();

        contexte.drawImage(
                image,
                position.getX() - w/2,
                position.getY() - h/2,
                w,
                h
        );
        contexte.setFill(Color.WHITE);
        contexte.fillText(nom, position.getX()-w/2, position.getY()-3*h/4);
    }

    private static Image imageAleatoire() {
        int index = (int)(Math.random() * IMAGES.length);
        return new Image(Planete.class.getResourceAsStream(IMAGES[index]));
    }
}
