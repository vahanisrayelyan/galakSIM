package ca.qc.bdeb.sim.galak_sim.addons;

import ca.qc.bdeb.sim.galak_sim.astres.Astre;
import ca.qc.bdeb.sim.galak_sim.astres.AstreFantome;
import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Physique {
    private final double G = 6.67430e-11;


    public void effetForceGravitationelle(ArrayList<Planete> planetes) {
        for (Planete p : planetes) {
            p.setAcceleration(Point2D.ZERO);
        }
        appliquerGravite(new ArrayList<>(planetes));
    }

    public List<List<Point2D>> calculerPredictions(ArrayList<Planete> planetes) {
        final int MAX_POINTS = 1000;
        final double DISTANCE_MAX_ECRAN = 2000.0;

        List<AstreFantome> fantomes = new ArrayList<>();
        List<List<Point2D>> trajectoires = new ArrayList<>();
        double[] distCumulee = new double[planetes.size()];
        boolean[] estTermine = new boolean[planetes.size()];

        for (Planete p : planetes) {
            fantomes.add(new AstreFantome(p));
            List<Point2D> chemin = new ArrayList<>();
            chemin.add(p.getPosition());
            trajectoires.add(chemin);
        }

        for (int etape = 0; etape < MAX_POINTS; etape++) {
            for (AstreFantome f : fantomes) f.setAcceleration(Point2D.ZERO);

            appliquerGravite(new ArrayList<>(fantomes));

            boolean encoreQuelquun = false;

            for (int i = 0; i < fantomes.size(); i++) {
                if (estTermine[i]) continue;

                AstreFantome f = fantomes.get(i);
                Point2D avant = f.getPosition();

                double dt = 100000.0;

                f.update(dt);
                Point2D apres = f.getPosition();
                
                distCumulee[i] += avant.distance(apres);
                trajectoires.get(i).add(apres);

                if (distCumulee[i] >= DISTANCE_MAX_ECRAN) {
                    estTermine[i] = true;
                } else {
                    encoreQuelquun = true;
                }
            }

            if (!encoreQuelquun) break;
        }
        return trajectoires;
    }

    private void appliquerGravite(ArrayList<Astre> astres) {
        for (int i = 0; i < astres.size(); i++) {
            for (int j = i + 1; j < astres.size(); j++) {
                Astre a1 = astres.get(i);
                Astre a2 = astres.get(j);

                double dx = (a2.getPosition().getX() - a1.getPosition().getX());
                double dy = (a2.getPosition().getY() - a1.getPosition().getY());
                double r = Math.sqrt(dx * dx + dy * dy);

                if (r < 1) continue;

                double ux = dx / r;
                double uy = dy / r;

                double Fg = (G * a1.getMasse() * a2.getMasse()) / (r * r);

                a1.setAcceleration(new Point2D(
                        a1.getAcceleration().getX() + (Fg * ux) / a1.getMasse(),
                        a1.getAcceleration().getY() + (Fg * uy) / a1.getMasse()
                ));
                a2.setAcceleration(new Point2D(
                        a2.getAcceleration().getX() - (Fg * ux) / a2.getMasse(),
                        a2.getAcceleration().getY() - (Fg * uy) / a2.getMasse()
                ));
            }
        }
    }
}
