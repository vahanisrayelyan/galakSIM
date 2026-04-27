package ca.qc.bdeb.sim.galak_sim.addons;

import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import ca.qc.bdeb.sim.galak_sim.graphics.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Collision {

    private final ArrayList<Explosion> explosions = new ArrayList<>();

    public ArrayList<Planete> verificationCollision(ArrayList<Planete> listePlanetes) {
        Set<Planete> aSupprimer = new HashSet<>();

        for (int i = 0; i < listePlanetes.size(); i++) {
            for (int j = i + 1; j < listePlanetes.size(); j++) {

                Planete pi = listePlanetes.get(i);
                Planete pj = listePlanetes.get(j);

                double rayoni = pi.getTaille().getX() / 2.0;
                double rayonj = pj.getTaille().getX() / 2.0;

                double xi = pi.getPosition().getX();
                double yi = pi.getPosition().getY();

                double xj = pj.getPosition().getX();
                double yj = pj.getPosition().getY();

                double dx = xj - xi;
                double dy = yj - yi;
                double distanceEntre = Math.hypot(dx, dy);

                if (distanceEntre <= rayoni + rayonj) {
                    double centrex = (xi + xj) / 2.0;
                    double centrey = (yi + yj) / 2.0;

                    if(pi.estTrouNoir() && !pj.estTrouNoir()) {
                        pi.setMasse(pi.getMasse() + pj.getMasse());
                        aSupprimer.add(pj);
                    }
                    else if (pj.estTrouNoir() && !pi.estTrouNoir()){
                        pj.setMasse(pj.getMasse() + pi.getMasse());
                        aSupprimer.add(pi);
                    }
                    else {
                        double rayonExplosion = Math.max(1.0e7, Math.max(rayoni, rayonj) * 0.25);
                        explosions.add(new Explosion(centrex, centrey, rayonExplosion));

                        aSupprimer.add(pi);
                        aSupprimer.add(pj);
                    }

                }
            }
        }

        listePlanetes.removeIf(aSupprimer::contains);
        return listePlanetes;
    }

    public ArrayList<Explosion> getExplosions() {
        return explosions;
    }

    public void updateExplosions() {
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).update();
            if (explosions.get(i).estTerminee()) {
                explosions.remove(i);
                i--;
            }
        }
    }

    public void draw(GraphicsContext gc, Camera camera, double largeurCanvas, double hauteurCanvas) {
        for (Explosion explosion : explosions) {
            Point2D posEcran = camera.mondeVersEcran(
                    explosion.getX(),
                    explosion.getY(),
                    largeurCanvas,
                    hauteurCanvas
            );

            double rayonEcran = explosion.getRayon() * camera.getZoom();
            rayonEcran = Math.max(6, rayonEcran);

            gc.setFill(Color.ORANGE.deriveColor(0, 1, 1, 0.7));
            gc.fillOval(
                    posEcran.getX() - rayonEcran,
                    posEcran.getY() - rayonEcran,
                    rayonEcran * 2,
                    rayonEcran * 2
            );
        }
    }
}