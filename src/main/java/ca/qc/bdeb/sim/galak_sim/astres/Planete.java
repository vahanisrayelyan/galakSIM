package ca.qc.bdeb.sim.galak_sim.astres;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Planete extends Astre {

    private Image image;
    private String nom;
    private Orbite orbitePlanete;
    private Orbite predictionOrbitePlanete;
    private Color couleurOrbite;

    private static final String[] IMAGES = {
            "/planetesAleatoires/planete1.png",
            "/planetesAleatoires/planete2.png",
            "/planetesAleatoires/planete3.png",
            "/planetesAleatoires/planete4.png",
            "/planetesAleatoires/planete5.png",
            "/planetesAleatoires/planete6.png",
            "/planetesAleatoires/planete7.png",
            "/planetesAleatoires/planete8.png",
    };

    public Planete(double x, double y, double vX, double vY, double taille, double masse, String nom, Image photo, Color couleurOrbite) {
        super(x, y, vX, vY, taille, masse);
        if (photo == null) {
            this.image = imageAleatoire();
        } else {
            this.image = photo;
        }
        this.nom = nom;
        this.orbitePlanete = new Orbite();
        this.predictionOrbitePlanete = new Orbite();
        if (couleurOrbite == null) {
            this.couleurOrbite = couleurAleatoire();
        } else {
            this.couleurOrbite = couleurOrbite;
        }
    }

    public void update(double deltaTemps) {

        super.update(deltaTemps);
        orbitePlanete.update(position.getX(), position.getY());
    }

    public void draw(GraphicsContext contexte, boolean afficherPrediction) {

        double w = taille.getX();
        double h = taille.getY();

        contexte.drawImage(
                image,
                position.getX() - w / 2,
                position.getY() - h / 2,
                w,
                h
        );

        contexte.setFill(Color.WHITE);
        contexte.fillText(nom, position.getX() - w / 2, position.getY() - 3 * h / 4);

        orbitePlanete.draw(contexte, couleurOrbite, false);
        if (afficherPrediction) {
            predictionOrbitePlanete.draw(contexte, couleurOrbite, true);
        }
    }

    private static Image imageAleatoire() {
        int index = (int) (Math.random() * IMAGES.length);
        return new Image(Planete.class.getResourceAsStream(IMAGES[index]));
    }

    private Color couleurAleatoire() {
        return Color.rgb((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Point2D getPosition() {
        return new Point2D(position.getX(), position.getY());
    }

    public Orbite getPredictionOrbitePlanete() {
        return predictionOrbitePlanete;
    }

    public void setPredictionOrbitePlanete(Orbite predictionOrbitePlanete) {
        this.predictionOrbitePlanete = predictionOrbitePlanete;
    }
}
