package ca.qc.bdeb.sim.galak_sim.addons;

import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import javafx.geometry.Point2D;

import java.util.ArrayList;

public class Physique {
        private final double G = 4 * Math.pow(Math.PI,2);


        public void effetForceGravitationelle(double dt,ArrayList<Planete> listePlanetes) {
            for (Planete planeteChoisie : listePlanetes) {

                for (Planete autrePlanete : listePlanetes) {

                    if (autrePlanete != planeteChoisie) {

                        double m1 = planeteChoisie.getMasse();
                        double m2 = autrePlanete.getMasse();

                        double x1 = planeteChoisie.getPosition().getX();
                        double x2 = autrePlanete.getPosition().getX();

                        double y1 =  planeteChoisie.getPosition().getY();
                        double y2 =  autrePlanete.getPosition().getY();

                        ///////////////////////////////////////////////////////////
                        double dx = x2 - x1;
                        double dy = y2 - y1;

                        double r = Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));

                        double ux = dx/r;
                        double uy = dy/r;

                        double Fg = (G * m1 * m2) / Math.pow(r,2);

                        double Fgx = Fg * ux;
                        double Fgy = Fg * uy;

                        double ax1 = Fgx / m1;
                        double ay1 = Fgy / m1;
                        // Fab = -Fba
                        double ax2 = - Fgx / m2;
                        double ay2 = - Fgx / m2;

                        System.out.println(ax2);

                        planeteChoisie.setAcceleration(new Point2D(ax1,ay1));
                        autrePlanete.setAcceleration(new Point2D(ax2,ay2));
                    }
                }
            }
        }



}
