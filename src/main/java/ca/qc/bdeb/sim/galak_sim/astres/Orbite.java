package ca.qc.bdeb.sim.galak_sim.astres;

import ca.qc.bdeb.sim.galak_sim.graphics.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Orbite {

    private final ArrayList<Point2D> orbites = new ArrayList<>();
    private ArrayList<Point2D> orbitesPrediction = new ArrayList<>();

    public void ajouterPointOrbite(double x, double y) {
        orbites.add(new Point2D(x, y));

        if (orbites.size() > 50000) {
            orbites.remove(0);
        }
    }

    public void ajouterPointOrbitePrediction(List<Point2D> nouveauxPoints) {
        if (nouveauxPoints == null) {
            orbitesPrediction.clear();
        } else {
            orbitesPrediction = new ArrayList<>(nouveauxPoints);
        }
    }

    public void update(double x, double y) {
        ajouterPointOrbite(x, y);
    }

    public void vider() {
        orbites.clear();
        orbitesPrediction.clear();
    }

    public void draw(GraphicsContext contexte,
                     Camera camera,
                     double largeurCanvas,
                     double hauteurCanvas,
                     Color couleur,
                     boolean afficherPointille) {

        if (couleur == null) {
            couleur = Color.WHITE;
        }

        // Orbite passée
        if (orbites.size() > 1) {
            contexte.setStroke(couleur);
            contexte.setLineWidth(1.0);
            contexte.setLineDashes(0);

            for (int j = 1; j < orbites.size(); j++) {
                Point2D prevMonde = orbites.get(j - 1);
                Point2D currMonde = orbites.get(j);

                Point2D prevEcran = camera.mondeVersEcran(
                        prevMonde.getX(), prevMonde.getY(),
                        largeurCanvas, hauteurCanvas
                );
                Point2D currEcran = camera.mondeVersEcran(
                        currMonde.getX(), currMonde.getY(),
                        largeurCanvas, hauteurCanvas
                );

                contexte.strokeLine(
                        prevEcran.getX(), prevEcran.getY(),
                        currEcran.getX(), currEcran.getY()
                );
            }
        }

        // Orbite prédite
        if (afficherPointille && orbitesPrediction != null && orbitesPrediction.size() > 1) {
            contexte.setStroke(couleur.deriveColor(0, 1, 1, 0.5));
            contexte.setLineWidth(1.0);
            contexte.setLineDashes(8);

            Point2D premierPointMonde = orbitesPrediction.get(0);
            Point2D premierPointEcran = camera.mondeVersEcran(
                    premierPointMonde.getX(), premierPointMonde.getY(),
                    largeurCanvas, hauteurCanvas
            );

            contexte.beginPath();
            contexte.moveTo(premierPointEcran.getX(), premierPointEcran.getY());

            for (int i = 1; i < orbitesPrediction.size(); i++) {
                Point2D pointMonde = orbitesPrediction.get(i);
                Point2D pointEcran = camera.mondeVersEcran(
                        pointMonde.getX(), pointMonde.getY(),
                        largeurCanvas, hauteurCanvas
                );

                contexte.lineTo(pointEcran.getX(), pointEcran.getY());
            }

            contexte.stroke();
            contexte.setLineDashes(0);
        }
    }
}