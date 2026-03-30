package ca.qc.bdeb.sim.galak_sim.addons;

import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public class Vecteurs {
    private int choix = 0;
    private double racineVecteurX;
    private double racineVecteurY;
    private double boutVecteurVitesseX;
    private double boutVecteurVitesseY;

    private ArrayList<Planete> planetes;

    public void setPlanete(ArrayList<Planete> planetes) {
        this.planetes = planetes;
    }

    public void setChoix(int choix) {
        this.choix = choix;
    }

    public void draw (GraphicsContext gc) {

        for (Planete p : planetes) {
            if (choix != 0) {
                if (choix == 1) {
                    racineVecteurX = p.getPosition().getX();
                    racineVecteurY = p.getPosition().getY();

                    boutVecteurVitesseX = racineVecteurX + p.getVelocite().getX() * 1e-5;
                    boutVecteurVitesseY = racineVecteurY + p.getVelocite().getY() * 1e-5;

                    gc.setStroke(Color.WHITE);
                    gc.setLineWidth(2);
                }

                else if (choix == 2) {
                    racineVecteurX = p.getPosition().getX();
                    racineVecteurY = p.getPosition().getY();

                    boutVecteurVitesseX = racineVecteurX + p.getAcceleration().getX() * 1e-5;
                    boutVecteurVitesseY = racineVecteurY + p.getAcceleration().getY() * 1e-5;

                    gc.setStroke(Color.RED);
                    gc.setLineWidth(2);
                }

                else if (choix == 3) {
                    racineVecteurX = p.getPosition().getX();
                    racineVecteurY = p.getPosition().getY();

                    boutVecteurVitesseX = racineVecteurX + p.getAcceleration().getX() * 1e-5;
                    boutVecteurVitesseY = racineVecteurY + p.getAcceleration().getY() * 1e-5;

                    gc.setStroke(Color.BLUE);
                    gc.setLineWidth(2);
                }

                gc.strokeLine(racineVecteurX,racineVecteurY,boutVecteurVitesseX,boutVecteurVitesseY);

                double flecheLongueur = 15;
                double angle = Math.atan2(boutVecteurVitesseY - racineVecteurY, boutVecteurVitesseX - racineVecteurX);

                double angle1 = angle - Math.PI / 6;
                double angle2 = angle + Math.PI / 6;

                double xFleche1 = boutVecteurVitesseX - flecheLongueur * Math.cos(angle1);
                double yFleche1 = boutVecteurVitesseY - flecheLongueur * Math.sin(angle1);

                double xFleche2 = boutVecteurVitesseX - flecheLongueur * Math.cos(angle2);
                double yFleche2 = boutVecteurVitesseY - flecheLongueur * Math.sin(angle2);

                gc.strokeLine(boutVecteurVitesseX, boutVecteurVitesseY, xFleche1, yFleche1);
                gc.strokeLine(boutVecteurVitesseX, boutVecteurVitesseY, xFleche2, yFleche2);
            }

        }

    }
}
