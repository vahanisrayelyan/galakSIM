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
            p.setFg(0,0);
            p.setAcceleration(Point2D.ZERO);
        }
        appliquerGravite(new ArrayList<>(planetes));
    }

    public List<List<Point2D>> calculerPredictions(ArrayList<Planete> planetes, double zoomActuel) {
        final int MAX_POINTS = 1500;

        final double LIMITE_VISUELLE_PIXELS = 1500;

        List<AstreFantome> fantomes = new ArrayList<>();
        List<List<Point2D>> trajectoires = new ArrayList<>();
        double[] distanceVisuelleCumulee = new double[planetes.size()];
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

                AstreFantome fantome = fantomes.get(i);
                Point2D avant = fantome.getPosition();

                double dt = 100000.0;

                fantome.update(dt);
                Point2D apres = fantome.getPosition();

                double deplacementMetres = avant.distance(apres);
                double deplacementPixels = deplacementMetres * zoomActuel;

                distanceVisuelleCumulee[i] += deplacementPixels;
                trajectoires.get(i).add(apres);

                if (distanceVisuelleCumulee[i] >= LIMITE_VISUELLE_PIXELS) {
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

                if (r <= 0) continue;

                double ux = dx / r;
                double uy = dy / r;

                double Fg = (G * a1.getMasse() * a2.getMasse()) / (r * r);
                double Fgx = Fg * ux;
                double Fgy = Fg * uy;

                a1.setFg(a1.getFg().getX() + Fgx, a1.getFg().getY() + Fgy);
                a2.setFg(a2.getFg().getX() - Fgx, a2.getFg().getY() - Fgy);

                a1.setAcceleration(new Point2D(
                        a1.getAcceleration().getX() + Fgx / a1.getMasse(),
                        a1.getAcceleration().getY() + Fgy / a1.getMasse()
                ));
                a2.setAcceleration(new Point2D(
                        a2.getAcceleration().getX() - Fgx / a2.getMasse(),
                        a2.getAcceleration().getY() - Fgy / a2.getMasse()
                ));
            }
        }
    }
}
