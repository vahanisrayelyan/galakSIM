package ca.qc.bdeb.sim.galak_sim.astres;

import ca.qc.bdeb.sim.galak_sim.graphics.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class Planete extends Astre {

    private Image image;
    private String nom;
    private Orbite orbitePlanete;
    private Orbite predictionOrbitePlanete;
    private Color couleurOrbite;
    private String description;

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

    public Planete(double x, double y, double vX, double vY, double taille, double masse,
                   String nom, Image photo, Color couleurOrbite, String description) {
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

        this.description = description != null ? description : "";
    }

    @Override
    public void update(double deltaTemps) {
        super.update(deltaTemps);
        orbitePlanete.update(position.getX(), position.getY());
    }

    public void draw(GraphicsContext contexte, Camera camera,
                     double largeurCanvas, double hauteurCanvas,
                     boolean afficherPrediction) {

        Point2D posEcran = camera.mondeVersEcran(
                position.getX(),
                position.getY(),
                largeurCanvas,
                hauteurCanvas
        );

        // Rayon physique projeté à l'écran
        double rayonPhysique = taille.getX();
        double rayonEcran = rayonPhysique * camera.getZoom();

        // Taille minimale pour voir la planète
        double rayonAffichageMin = 4.0;
        if ("Soleil".equalsIgnoreCase(nom)) {
            rayonAffichageMin = 8.0;
        }

        double rayonPlaneteAffiche = Math.max(rayonEcran, rayonAffichageMin);

        // Cercle de repère visible de loin
        double seuilCercleRepere = 6.0;
        double rayonCercleRepere = 10.0;

        double largeurAffichage = rayonPlaneteAffiche * 2;
        double hauteurAffichage = rayonPlaneteAffiche * 2;

        // Marge hors écran
        double marge = 200;
        if (posEcran.getX() < -marge || posEcran.getX() > largeurCanvas + marge
                || posEcran.getY() < -marge || posEcran.getY() > hauteurCanvas + marge) {

            orbitePlanete.draw(contexte, camera, largeurCanvas, hauteurCanvas, couleurOrbite, false);
            if (afficherPrediction) {
                predictionOrbitePlanete.draw(contexte, camera, largeurCanvas, hauteurCanvas, couleurOrbite, true);
            }
            return;
        }

        // Dessin de la planète
        if (estTrouNoir()) {
            contexte.setFill(Color.BLACK);
            contexte.fillOval(
                    posEcran.getX() - rayonPlaneteAffiche,
                    posEcran.getY() - rayonPlaneteAffiche,
                    largeurAffichage,
                    hauteurAffichage
            );
            contexte.setStroke(Color.rgb(138, 43, 226, 0.8));
            contexte.setLineWidth(4);
            contexte.strokeOval(
                    posEcran.getX() - rayonPlaneteAffiche - 4,
                    posEcran.getY() - rayonPlaneteAffiche - 4,
                    largeurAffichage + 8,
                    hauteurAffichage + 8
            );
        } else if (image != null) {
            contexte.drawImage(
                    image,
                    posEcran.getX() - rayonPlaneteAffiche,
                    posEcran.getY() - rayonPlaneteAffiche,
                    largeurAffichage,
                    hauteurAffichage
            );
        } else {
            contexte.setFill(Color.GRAY);
            contexte.fillOval(
                    posEcran.getX() - rayonPlaneteAffiche,
                    posEcran.getY() - rayonPlaneteAffiche,
                    largeurAffichage,
                    hauteurAffichage
            );
        }

        // Cercle de repère de la même couleur que l’orbite
        if (rayonEcran < seuilCercleRepere) {
            Color couleurRepere = (couleurOrbite != null ? couleurOrbite : Color.WHITE)
                    .deriveColor(0, 1, 1, 0.75);

            contexte.setStroke(couleurRepere);
            contexte.setLineWidth(1.5);
            contexte.strokeOval(
                    posEcran.getX() - rayonCercleRepere,
                    posEcran.getY() - rayonCercleRepere,
                    rayonCercleRepere * 2,
                    rayonCercleRepere * 2
            );

            // Petit point central pour mieux repérer la planète
            contexte.setFill(couleurRepere);
            contexte.fillOval(
                    posEcran.getX() - 1.5,
                    posEcran.getY() - 1.5,
                    3,
                    3
            );
        }

        // Nom de la planète
        contexte.setFill(Color.WHITE);
        contexte.setTextAlign(TextAlignment.CENTER);
        contexte.fillText(
                nom,
                posEcran.getX(),
                posEcran.getY() - Math.max(rayonPlaneteAffiche, rayonCercleRepere) - 6
        );

        // Orbites
        orbitePlanete.draw(contexte, camera, largeurCanvas, hauteurCanvas, couleurOrbite, false);

        if (afficherPrediction) {
            predictionOrbitePlanete.draw(contexte, camera, largeurCanvas, hauteurCanvas, couleurOrbite, true);
        }
    }

    private static Image imageAleatoire() {
        int index = (int) (Math.random() * IMAGES.length);
        return new Image(Planete.class.getResourceAsStream(IMAGES[index]));
    }

    private Color couleurAleatoire() {
        return Color.rgb(
                (int) (Math.random() * 256),
                (int) (Math.random() * 256),
                (int) (Math.random() * 256)
        );
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public Point2D getPosition() {
        return new Point2D(position.getX(), position.getY());
    }

    public Orbite getPredictionOrbitePlanete() {
        return predictionOrbitePlanete;
    }
    public void setPredictionOrbitePlanete(Orbite predictionOrbitePlanete) {
        this.predictionOrbitePlanete = predictionOrbitePlanete;
    }

    public Color getCouleurOrbite() {
        return couleurOrbite;
    }
    public void setCouleurOrbite(Color couleurOrbite) {
        this.couleurOrbite = couleurOrbite;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}