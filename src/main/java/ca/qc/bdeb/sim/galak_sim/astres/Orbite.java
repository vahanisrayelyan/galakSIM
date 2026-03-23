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

    public void draw(GraphicsContext contexte, Color color,
                     double largeur, double hauteur,
                     double offsetX, double offsetY,
                     double zoom, double echelleAffichage) {

        contexte.setStroke(color);

        for (int i = 1; i < orbites.size(); i++) {
            PointOrbite precedent = orbites.get(i - 1);
            PointOrbite courant = orbites.get(i);

            double x1 = largeur / 2.0 + (precedent.getX() + offsetX) * zoom * echelleAffichage;
            double y1 = hauteur / 2.0 + (precedent.getY() + offsetY) * zoom * echelleAffichage;

            double x2 = largeur / 2.0 + (courant.getX() + offsetX) * zoom * echelleAffichage;
            double y2 = hauteur / 2.0 + (courant.getY() + offsetY) * zoom * echelleAffichage;

            contexte.strokeLine(x1, y1, x2, y2);
        }

        if (orbites.size() > 1000) {
            orbites.remove(0);
        }
    }
}

