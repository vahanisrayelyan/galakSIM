package ca.qc.bdeb.sim.galak_sim.addons;

import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import javafx.geometry.Point2D;

import java.util.ArrayList;

public class Physique {
    private final double G = 6.67430e-11;

    public void effetForceGravitationelle(ArrayList<Planete> listePlanetes) {
        for (Planete planete : listePlanetes) {
            planete.setAcceleration(new Point2D(0, 0));
        }

        for (int i = 0; i < listePlanetes.size(); i++) {

            for (int j = i + 1; j < listePlanetes.size(); j++) {

                Planete pi = listePlanetes.get(i);
                Planete pj = listePlanetes.get(j);

                double m1 = pi.getMasse();
                double m2 = pj.getMasse();

                double x1 = pi.getPosition().getX();
                double x2 = pj.getPosition().getX();

                double y1 = pi.getPosition().getY();
                double y2 = pj.getPosition().getY();

                double dx = x2 - x1;
                double dy = y2 - y1;

                double r = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

                double ux = dx / r;
                double uy = dy / r;

                double Fg = (G * m1 * m2) / Math.pow(r, 2);

                double Fgx = Fg * ux;
                double Fgy = Fg * uy;

                double ax1 = Fgx / m1;
                double ay1 = Fgy / m1;
                // Fab = -Fba
                double ax2 = -Fgx / m2;
                double ay2 = -Fgy / m2;

//                pi.setAcceleration(new Point2D(ax1, ay1));
//                pj.setAcceleration(new Point2D(ax2, ay2));

                pi.setAcceleration(new Point2D(
                        pi.getAcceleration().getX() + ax1,
                        pi.getAcceleration().getY() + ay1
                ));
                pj.setAcceleration(new Point2D(
                        pj.getAcceleration().getX() + ax2,
                        pj.getAcceleration().getY() + ay2
                ));
            }
        }
    }
}
