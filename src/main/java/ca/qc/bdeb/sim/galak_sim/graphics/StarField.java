package ca.qc.bdeb.sim.galak_sim.graphics;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;


public class StarField {

    private static class Star {
        Circle node;
        double phase;
        double speed;
        double baseOpacity;
    }

    private final Pane layer;
    private final List<Star> stars = new ArrayList<>();

    private final double pixelsParEtoile;

    private boolean removeWhenShrinking = true;

    private AnimationTimer twinkleTimer;
    private long lastNs = 0;

    public StarField(Pane layer) {
        this(layer, 2500.0);
    }

    public StarField(Pane layer, double pixelsParEtoile) {
        this.layer = layer;
        this.pixelsParEtoile = Math.max(200.0, pixelsParEtoile);


        this.layer.setStyle("-fx-background-color: black;");

        this.layer.widthProperty().addListener((obs, o, n) -> adjustToSize());
        this.layer.heightProperty().addListener((obs, o, n) -> adjustToSize());
    }


    public void setRemoveWhenShrinking(boolean value) {
        this.removeWhenShrinking = value;
    }


    public void start() {
        if (twinkleTimer != null) return;

        twinkleTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastNs == 0) lastNs = now;
                double dt = (now - lastNs) * 1e-9;
                lastNs = now;

                updateTwinkle(dt);
            }
        };
        twinkleTimer.start();


        adjustToSize();
    }


    public void stop() {
        if (twinkleTimer != null) {
            twinkleTimer.stop();
            twinkleTimer = null;
        }
        lastNs = 0;
    }


    private void adjustToSize() {
        double w = layer.getWidth();
        double h = layer.getHeight();
        if (w <= 0 || h <= 0) return;

        int target = (int) ((w * h) / pixelsParEtoile);


        while (stars.size() < target) {
            Star s = createRandomStar(w, h);
            stars.add(s);
            layer.getChildren().add(s.node);
        }


        if (removeWhenShrinking) {
            while (stars.size() > target && !stars.isEmpty()) {
                Star s = stars.remove(stars.size() - 1);
                layer.getChildren().remove(s.node);
            }
        }
    }

    private Star createRandomStar(double w, double h) {
        Star s = new Star();

        double size = 0.5 + Math.random() * 1.8;

        Circle c = new Circle(size);
        c.setCenterX(Math.random() * w);
        c.setCenterY(Math.random() * h);


        c.setFill(Color.WHITE);
        c.setOpacity(0.8);

        s.node = c;


        s.phase = Math.random() * Math.PI * 2.0;
        s.speed = 1.5 + Math.random() * 2.5;      // vitesse du sinus
        s.baseOpacity = 0.25 + Math.random() * 0.45; // base (étoiles plus ou moins visibles)

        return s;
    }


    private void updateTwinkle(double dt) {

        adjustToSize();

        for (Star s : stars) {
            s.phase += dt * s.speed;


            double t = 0.5 + 0.5 * Math.sin(s.phase);


            double opacity = clamp(s.baseOpacity + t * 0.6, 0.05, 1.0);
            s.node.setOpacity(opacity);
        }
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
