package ca.qc.bdeb.sim.galak_sim.addons;

import javafx.scene.canvas.GraphicsContext;

public class Vecteurs {
    private int choix = 0;

    public void update(double dt, String choixExtrait) {
        switch (choixExtrait) {
            case "Aucun" -> choix = 0;
            case "Force" -> choix = 1;
            case "Vitesse" -> choix = 2;

        }


    }

    public void draw (GraphicsContext gc) {

    }
}
