package ca.qc.bdeb.sim.galak_sim;

import ca.qc.bdeb.sim.galak_sim.graphics.InterfaceGraphique;
import ca.qc.bdeb.sim.galak_sim.graphics.Simulation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainJavaFX extends Application {
    private static Simulation simulation;
    public final static double LARGEUR = 1200;
    public final static double HAUTEUR = 700;

    @Override
    public void start(Stage stage) throws IOException {

        Canvas canvas = new Canvas(LARGEUR, HAUTEUR);
        var contexte = canvas.getGraphicsContext2D();

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: black;");
        borderPane.setCenter(canvas);

        Scene scene = new Scene(borderPane, LARGEUR, HAUTEUR);

        InterfaceGraphique gui = new InterfaceGraphique();
        simulation = new Simulation(gui);

        AnimationTimer timer = new AnimationTimer() {
            private long dernierTemps = System.nanoTime();

            @Override
            public void handle(long temps) {
                double deltaTemps = (temps - dernierTemps) * 1e-9;

                simulation.update(deltaTemps);
                simulation.draw(contexte);

                dernierTemps = temps;
            }
        };
        timer.start();

        stage.getIcons().add(new Image("logo.jpg"));
        stage.setTitle("GalakSIM");
        stage.setScene(scene);
        stage.show();
    }
}