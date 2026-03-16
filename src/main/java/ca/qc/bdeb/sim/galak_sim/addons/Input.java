package ca.qc.bdeb.sim.galak_sim.addons;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import java.util.HashSet;
import java.util.Set;

public class Input {
    private Set<KeyCode> touchesActives = new HashSet<>();

    public Input() {}

    public void etatTouches(Scene scene) {
        //ajouter lorsque touche est active
        scene.setOnKeyPressed(e->touchesActives.add(e.getCode()));
        //enlever lorsque touche est inactive
        scene.setOnKeyReleased(e->touchesActives.remove(e.getCode()));
    }

    public boolean estActive(KeyCode touche){
        return touchesActives.contains(touche);
    }

}
