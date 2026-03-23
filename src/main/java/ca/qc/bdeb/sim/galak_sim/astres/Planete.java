package ca.qc.bdeb.sim.galak_sim.astres;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Planete extends Astre {

    private Image image;
    private String nom;
    private Orbite orbitePlanete;
    private Color couleurOrbite;

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
        this.orbitePlanete = new Orbite();
        this.couleurOrbite = couleurAleatoire();
    }

    public void update(double deltaTemps) {

        super.update(deltaTemps);
        orbitePlanete.update(position.getX(), position.getY());
    }

    public void draw(GraphicsContext contexte,
                     double xEcran, double yEcran,
                     double zoom,
                     double largeur, double hauteur,
                     double offsetX, double offsetY,
                     double echelleAffichage) {

        orbitePlanete.draw(contexte, couleurOrbite, largeur, hauteur, offsetX, offsetY, zoom, echelleAffichage);

        double w = taille.getX() * zoom;
        double h = taille.getY() * zoom;

        w = Math.max(5, Math.min(w, 300));
        h = Math.max(5, Math.min(h, 300));

        contexte.drawImage(
                image,
                xEcran - w / 2,
                yEcran - h / 2,
                w,
                h
        );

        contexte.setFill(Color.WHITE);
        contexte.fillText(nom, xEcran - w / 2, yEcran - 3 * h / 4);
    }

    private static Image imageAleatoire() {
        int index = (int)(Math.random() * IMAGES.length);
        return new Image(Planete.class.getResourceAsStream(IMAGES[index]));
    }
    private Color couleurAleatoire() {
        return Color.rgb((int) (Math.random()*256), (int) (Math.random()*256), (int) (Math.random()*256));
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
}
