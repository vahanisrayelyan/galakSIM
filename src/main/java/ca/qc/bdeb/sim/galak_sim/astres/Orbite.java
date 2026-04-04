package ca.qc.bdeb.sim.galak_sim.astres;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Orbite {

    private ArrayList<Point2D> orbites = new ArrayList<>();
    private ArrayList<Point2D> orbitesPrediction = new ArrayList<>();

    public void ajouterPointOrbite(double x, double y) {
        orbites.add(new Point2D(x, y));
        if (orbites.size() > 1000) orbites.remove(0);
    }

    public void ajouterPointOrbitePrediction(List<Point2D> nouveauxPoints) {
        if (nouveauxPoints != null) {
            this.orbitesPrediction = new ArrayList<>(nouveauxPoints);
        }
    }

    public void update(double x, double y) {
        ajouterPointOrbite(x, y);
    }

    public void draw(GraphicsContext contexte, Color couleur, boolean pointille) {
        contexte.setStroke(couleur);
        contexte.setLineWidth(1.0);
        contexte.setLineDashes(pointille ? 10 : 0);

        for (int j = 1; j < orbites.size(); j++) {
            Point2D prev = orbites.get(j - 1);
            Point2D curr = orbites.get(j);
            contexte.strokeLine(prev.getX(), prev.getY(), curr.getX(), curr.getY());
        }

        if (orbitesPrediction != null && orbitesPrediction.size() > 1) {
            contexte.beginPath();
            contexte.moveTo(orbitesPrediction.get(0).getX(), orbitesPrediction.get(0).getY());
            for (int i = 1; i < orbitesPrediction.size(); i++) {
                contexte.lineTo(orbitesPrediction.get(i).getX(), orbitesPrediction.get(i).getY());
            }
            contexte.stroke();
        }

        contexte.setLineDashes(0);
    }
}