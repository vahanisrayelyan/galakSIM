package ca.qc.bdeb.sim.galak_sim.astres;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


import java.util.ArrayList;
import java.util.List;

public class Orbite {

    private ArrayList<PointOrbite> orbites;
    private ArrayList<PointOrbite> orbitesPrediction;

    public Orbite() {
        orbites = new ArrayList<>();
        orbitesPrediction = new ArrayList<>();
    }

    public void ajouterPointOrbite(double x, double y) {
        orbites.add(new PointOrbite(x, y));
        if (orbites.size() > 1000) {
            orbites.remove(0);
        }
    }

    public void ajouterPointOrbitePrediction(List<PointOrbite> nouveauxPoints) {
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

        if (pointille) contexte.setLineDashes(10);
        else contexte.setLineDashes(0);

        for (int j = 1; j < orbites.size(); j++) {
            PointOrbite p = orbites.get(j);
            PointOrbite prev = orbites.get(j-1);
            contexte.strokeLine(prev.getX(), prev.getY(), p.getX(), p.getY());
        }

        if (orbitesPrediction != null && orbitesPrediction.size() > 1) {
            contexte.beginPath();
            PointOrbite first = orbitesPrediction.get(0);
            contexte.moveTo(first.getX(), first.getY());

            for (int i = 1; i < orbitesPrediction.size(); i++) {
                PointOrbite p = orbitesPrediction.get(i);
                contexte.lineTo(p.getX(), p.getY());
            }
            contexte.stroke();
        }

        contexte.setLineDashes(0);
    }
}

