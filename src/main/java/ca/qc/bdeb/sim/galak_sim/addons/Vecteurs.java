package ca.qc.bdeb.sim.galak_sim.addons;

import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import ca.qc.bdeb.sim.galak_sim.graphics.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Vecteurs {
    private int choix = 0;
    private ArrayList<Planete> planetes;

    public void setPlanete(ArrayList<Planete> planetes) {
        this.planetes = planetes;
    }

    public void setChoix(int choix) {
        this.choix = choix;
    }

    public void draw(GraphicsContext gc, Camera camera, double largeurCanvas, double hauteurCanvas) {
        if (planetes == null || planetes.isEmpty() || choix == 0) {
            return;
        }

        for (Planete p : planetes) {
            double vecteurX;
            double vecteurY;
            double facteurAffichage;

            if (choix == 1) {
                vecteurX = p.getVelocite().getX();
                vecteurY = p.getVelocite().getY();
                facteurAffichage = 2e4;
                gc.setStroke(Color.WHITE);
            } else if (choix == 2) {
                vecteurX = p.getAcceleration().getX();
                vecteurY = p.getAcceleration().getY();
                facteurAffichage = 5e8;
                gc.setStroke(Color.RED);
            } else {
                // Force = m * a
                vecteurX = p.getAcceleration().getX() * p.getMasse();
                vecteurY = p.getAcceleration().getY() * p.getMasse();
                facteurAffichage = 1e-17;
                gc.setStroke(Color.BLUE);
            }

            Point2D racineMonde = p.getPosition();
            Point2D boutMonde = new Point2D(
                    racineMonde.getX() + vecteurX * facteurAffichage,
                    racineMonde.getY() + vecteurY * facteurAffichage
            );

            Point2D racineEcran = camera.mondeVersEcran(
                    racineMonde.getX(), racineMonde.getY(),
                    largeurCanvas, hauteurCanvas
            );
            Point2D boutEcran = camera.mondeVersEcran(
                    boutMonde.getX(), boutMonde.getY(),
                    largeurCanvas, hauteurCanvas
            );

            gc.setLineWidth(2);
            gc.strokeLine(
                    racineEcran.getX(), racineEcran.getY(),
                    boutEcran.getX(), boutEcran.getY()
            );

            double dx = boutEcran.getX() - racineEcran.getX();
            double dy = boutEcran.getY() - racineEcran.getY();
            double longueur = Math.hypot(dx, dy);

            if (longueur < 2) {
                continue;
            }

            double angle = Math.atan2(dy, dx);
            double flecheLongueur = 10;

            double angle1 = angle - Math.PI / 6;
            double angle2 = angle + Math.PI / 6;

            double xFleche1 = boutEcran.getX() - flecheLongueur * Math.cos(angle1);
            double yFleche1 = boutEcran.getY() - flecheLongueur * Math.sin(angle1);

            double xFleche2 = boutEcran.getX() - flecheLongueur * Math.cos(angle2);
            double yFleche2 = boutEcran.getY() - flecheLongueur * Math.sin(angle2);

            gc.strokeLine(boutEcran.getX(), boutEcran.getY(), xFleche1, yFleche1);
            gc.strokeLine(boutEcran.getX(), boutEcran.getY(), xFleche2, yFleche2);
        }
    }
}