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

    public void verificationCollision(ArrayList<Planete> listePlanetes) {
        Set<Planete> aSupprimer = new HashSet<>();

        for (int i = 0; i < listePlanetes.size(); i++) {
            Planete pi = listePlanetes.get(i);

            for (int j = i + 1; j < listePlanetes.size(); j++) {

                Planete pj = listePlanetes.get(j);

                boolean collision = false;

                double rayoni = pi.getTaille().getX() / 2.0;
                double rayonj = pj.getTaille().getX() / 2.0;

                double rayonTotal = rayoni + rayonj;

                Point2D piPosPrec = pi.getPositionPrecedante();
                Point2D pjPosPrec = pj.getPositionPrecedante();

                // Sécurité si pas encore initialisé
                if (piPosPrec == null || pjPosPrec == null) continue;

                // Position relative initiale de chaque planète
                double rx = pjPosPrec.getX() - piPosPrec.getX();
                double ry = pjPosPrec.getY() - piPosPrec.getY();

                // Velocité relative de chaque planète
                double vrx = (pj.getPosition().getX() - pjPosPrec.getX()) - (pi.getPosition().getX() - piPosPrec.getX());
                double vry = (pj.getPosition().getY() - pjPosPrec.getY()) - (pi.getPosition().getY() - piPosPrec.getY());

                // Coefficients du polynôme (calcul mathématique)
                // Formule pour savoir la vitesses relative par rapport l'une à l'autre
                double a = vrx * vrx + vry * vry;
                // Formule pour savoir si les planètes s'approhent ou s'éloignent
                double b = 2 * (rx * vrx + ry * vry);
                // Formule pour trouver la différence en position par rapport à la position initiale
                double c = rx * rx + ry * ry - rayonTotal * rayonTotal;

                double discriminant = b * b - 4 * a * c;

                // Si déjà en collision (vérification)
                if (c <= 0) {
                    collision = true;
                }

                // Si collision pendant le déplacement (vérification)
                else if (discriminant >= 0 && a > 0) {

                    double t = (-b - Math.sqrt(discriminant)) / (2 * a);

                    if (t >= 0 && t <= 1) {
                        collision = true;
                    }
                }

                // Action lors d'une collision
                if (collision) {

                    double centrex = (pi.getPosition().getX() + pj.getPosition().getX()) / 2.0;
                    double centrey = (pi.getPosition().getY() + pj.getPosition().getY()) / 2.0;

                    if (pi.estTrouNoir() && pj.estTrouNoir()){
                        pi.setMasse(pi.getMasse() + pj.getMasse());
                        aSupprimer.add(pj);
                        
                    }
                    else if (pi.estTrouNoir() && !pj.estTrouNoir()) {
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
        // Supprimation des planètes en collison
        listePlanetes.removeIf(aSupprimer::contains);
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