package ca.qc.bdeb.sim.galak_sim.addons;

import ca.qc.bdeb.sim.galak_sim.astres.Planete;

import java.util.ArrayList;

public class Collision {
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

                double distanceEntre = Math.sqrt(Math.pow(xj - xi, 2) + Math.pow(yj - yi, 2));

                if (distanceEntre  - (rayoni + rayonj) <= 0) {
                    listePlanetes.remove(pi);
                    listePlanetes.remove(pj);
                }
            }
        }
        return listePlanetes;
    }
}