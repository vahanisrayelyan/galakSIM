package ca.qc.bdeb.sim.galak_sim.astres;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


import java.util.ArrayList;

public class Orbite {

    private ArrayList<PointOrbite> point;

    public Orbite() {
        point = new ArrayList<>();
    }

    public void ajouterPointOrbite(double x, double y) {
        point.add(new PointOrbite(x, y));
        if (point.size() > 1000) {
            point.remove(0);
        }
    }

    public void ajouterPointOrbitePrediction() {

    }

    public void update(double x, double y) {
        ajouterPointOrbite(x, y);
    }

    public void draw(GraphicsContext contexte, Color color) {
        contexte.setStroke(color);

        for (int j = 1; j < point.size(); j++) {
            PointOrbite p = point.get(j);
            contexte.strokeLine(p.getX(), p.getY(), point.get(point.indexOf(p) - 1).getX(), point.get(point.indexOf(p) - 1).getY());
        }
    }
}

