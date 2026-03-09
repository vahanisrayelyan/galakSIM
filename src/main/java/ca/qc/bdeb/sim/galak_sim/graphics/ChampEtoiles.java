package ca.qc.bdeb.sim.galak_sim.graphics;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.List;

public class ChampEtoiles {

    private static class DonneesEtoile {
        Circle forme;
        double phase;
        double vitesse;
        double opaciteBase;
    }

    private final Pane panneauParent;
    private final List<DonneesEtoile> listeEtoiles = new ArrayList<>();

    private AnimationTimer minuteurScintillement;
    private long dernierTempsNano = 0;

    public ChampEtoiles(Pane panneau) {
        this.panneauParent = panneau;
        this.panneauParent.setStyle("-fx-background-color: black;");

        this.panneauParent.widthProperty().addListener((obs, vieux, nouveau) -> regenererEtoiles());
        this.panneauParent.heightProperty().addListener((obs, vieux, nouveau) -> regenererEtoiles());
    }

    public void demarrer() {
        if (minuteurScintillement != null) return;

        minuteurScintillement = new AnimationTimer() {
            @Override
            public void handle(long maintenant) {
                if (dernierTempsNano == 0) dernierTempsNano = maintenant;

                double dt = (maintenant - dernierTempsNano) * 1e-9;
                dernierTempsNano = maintenant;

                mettreAJourScintillement(dt);
            }
        };
        minuteurScintillement.start();
        regenererEtoiles();
    }

    private void regenererEtoiles() {
        double largeur = panneauParent.getWidth();
        double hauteur = panneauParent.getHeight();

        if (largeur <= 0 || hauteur <= 0) return;

        panneauParent.getChildren().clear();
        listeEtoiles.clear();

        double tailleCellule = 50;

        for (double x = 0; x < largeur; x += tailleCellule) {
            for (double y = 0; y < hauteur; y += tailleCellule) {
                DonneesEtoile etoile = creerEtoileAleatoire(x, y, tailleCellule);
                listeEtoiles.add(etoile);
                panneauParent.getChildren().add(etoile.forme);
            }
        }
    }

    private DonneesEtoile creerEtoileAleatoire(double grilleX, double grilleY, double tailleCellule) {
        DonneesEtoile dE = new DonneesEtoile();

        double rayon = 0.5 + Math.random() * 1.5;
        Circle cercle = new Circle(rayon);

        double posX = grilleX + (Math.random() * tailleCellule);
        double posY = grilleY + (Math.random() * tailleCellule);

        cercle.setCenterX(posX);
        cercle.setCenterY(posY);
        cercle.setFill(Color.WHITE);
        cercle.setOpacity(0.5);

        dE.forme = cercle;
        dE.phase = Math.random() * Math.PI * 2.0;
        dE.vitesse = 1.0 + Math.random() * 2.0;
        dE.opaciteBase = 0.2 + Math.random() * 0.4;

        return dE;
    }

    private void mettreAJourScintillement(double dt) {
        for (DonneesEtoile etoile : listeEtoiles) {
            etoile.phase += dt * etoile.vitesse;

            double oscillation = 0.5 + 0.5 * Math.sin(etoile.phase);
            double nouvelleOpacite = etoile.opaciteBase + (oscillation * 0.4);

            if (nouvelleOpacite > 1.0) nouvelleOpacite = 1.0;
            if (nouvelleOpacite < 0.1) nouvelleOpacite = 0.1;

            etoile.forme.setOpacity(nouvelleOpacite);
        }
    }
}