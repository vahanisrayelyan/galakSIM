package ca.qc.bdeb.sim.galak_sim;

import ca.qc.bdeb.sim.galak_sim.graphics.InterfaceGraphique;
import ca.qc.bdeb.sim.galak_sim.graphics.Simulation;
import ca.qc.bdeb.sim.galak_sim.graphics.StarField;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

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
        Pane starLayer = new Pane();
        StarField starField = new StarField(starLayer, 2500.0);
        starField.start();

        StackPane centre = new StackPane(starLayer, canvas);
        borderPane.setCenter(centre);


        Scene scene = new Scene(borderPane, LARGEUR, HAUTEUR);

        InterfaceGraphique gui = new InterfaceGraphique();
        simulation = new Simulation(gui,LARGEUR,HAUTEUR);

        AnimationTimer timer = new AnimationTimer() {
            private long dernierTemps = System.nanoTime();

            @Override
            public void handle(long temps) {
                double deltaTemps = (temps - dernierTemps) * 1e-9;

                simulation.update(deltaTemps);
                simulation.draw(contexte, canvas.getWidth(), canvas.getHeight());

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