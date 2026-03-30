package ca.qc.bdeb.sim.galak_sim.addons;

import ca.qc.bdeb.sim.galak_sim.astres.Planete;

import java.util.ArrayList;

public class Collision {

    private ArrayList<Explosion> explosions = new ArrayList<>();

    public ArrayList<Planete> verificationCollision(ArrayList<Planete> listePlanetes) {

        for (int i = 0; i < listePlanetes.size(); i++) {
            for (int j = i + 1; j < listePlanetes.size(); j++) {

                Planete pi = listePlanetes.get(i);
                Planete pj = listePlanetes.get(j);

                double rayoni = pi.getTaille().getX() / 2;
                double rayonj = pj.getTaille().getX() / 2;

                double xi = pi.getPosition().getX();
                double xj = pj.getPosition().getX();

                double yi = pi.getPosition().getY();
                double yj = pj.getPosition().getY();

                double centrex = (xi + xj) / 2;
                double centrey = (yi + yj) / 2;

                double distanceEntre = Math.sqrt(Math.pow(xj - xi, 2) + Math.pow(yj - yi, 2));

                if (distanceEntre <= rayoni + rayonj) {
                    explosions.add(new Explosion(centrex, centrey, 30));

                    listePlanetes.remove(j);
                    listePlanetes.remove(i);
                    i--;
                    break;
                }
            }
        }

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
}