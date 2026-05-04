package ca.qc.bdeb.sim.galak_sim.graphics;

import ca.qc.bdeb.sim.galak_sim.MainJavaFX;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Scale {
    private Camera cameraUtilisée;

    public Scale(Camera cameraUtilisée) {
        this.cameraUtilisée = cameraUtilisée;
    }

    public void draw (GraphicsContext context) {
        double grandeurPixels = 300;
        double distanceRéelle = grandeurPixels / cameraUtilisée.getZoom();

        double[] belleValeurs = {1,2,5,10};

        double exponant = Math.pow(10, Math.floor(Math.log10(distanceRéelle)));
        double meilleureValeur = 1;

        for (double val: belleValeurs) {
            double choixMeilleurevaleure = val * exponant;
            if (choixMeilleurevaleure <= distanceRéelle) {
                meilleureValeur = choixMeilleurevaleure;
            }
        }

        double minPx = 100;
        double maxPx = 300;

        while (meilleureValeur * cameraUtilisée.getZoom() < minPx) {
            meilleureValeur *= 2;
        }

        while (meilleureValeur * cameraUtilisée.getZoom() > maxPx) {
            meilleureValeur /= 2;
        }

        double scaleGrandeurPixels = meilleureValeur * cameraUtilisée.getZoom();


        double x = 40;
        double y = context.getCanvas().getHeight() - 20;

        context.setLineWidth(2);
        context.setFill(Color.WHITE);
        context.setStroke(Color.WHITE);

        context.strokeLine(x, y, x + scaleGrandeurPixels, y);
        context.fillText(formatDistance(meilleureValeur), x, y - 5);

        context.strokeLine(x, y, x, y - 5);
        context.strokeLine(x+scaleGrandeurPixels/2,y,x + scaleGrandeurPixels/2, y-5);
        context.strokeLine(x + scaleGrandeurPixels, y, x + scaleGrandeurPixels, y - 5);

        context.fillText(
                formatDistance(distanceRéelle),
                x + scaleGrandeurPixels - 50,
                y - 5
        );

    }

    private String formatDistance(double distance) {
        if (distance >= 1e11) return String.format("%.2f AU", distance / 1.496e11);
        if (distance >= 1e9) return String.format("%.2f Gm", distance / 1e9);
        if (distance >= 1e6) return String.format("%.2f Mm", distance / 1e6);
        if (distance >= 1e3) return String.format("%.2f km", distance / 1e3);
        return String.format("%.2f m", distance);
    }
 }
