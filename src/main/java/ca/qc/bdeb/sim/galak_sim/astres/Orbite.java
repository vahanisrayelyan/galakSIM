package ca.qc.bdeb.sim.galak_sim.astres;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


import java.util.ArrayList;

public class Orbite {

    private ArrayList<PointOrbite> orbites;

    public Orbite() {
        orbites = new ArrayList<>();
    }

    public void ajouterPointOrbite(double x, double y) {
        orbites.add(new PointOrbite(x, y));
    }

    public void update(double x, double y) {
        ajouterPointOrbite(x, y);
    }

    public void draw(GraphicsContext contexte, Color color) {
        contexte.setStroke(color);

        for (int j = 1; j < orbites.size(); j++) {
            PointOrbite p = orbites.get(j);
            contexte.strokeLine(p.getX(), p.getY(), orbites.get(orbites.indexOf(p) - 1).getX(), orbites.get(orbites.indexOf(p) - 1).getY());
        }
        if (orbites.size() > 1000) {
            orbites.remove(0);
        }
    }
}

