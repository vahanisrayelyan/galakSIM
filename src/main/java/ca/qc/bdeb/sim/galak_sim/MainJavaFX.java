package ca.qc.bdeb.sim.galak_sim;

import ca.qc.bdeb.sim.galak_sim.graphics.InterfaceGraphique;
import ca.qc.bdeb.sim.galak_sim.graphics.Simulation;
import ca.qc.bdeb.sim.galak_sim.graphics.StarField;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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

        VBox menuLateral = new VBox(15);
        menuLateral.setPadding(new javafx.geometry.Insets(60, 15, 15, 15));
        menuLateral.setMaxWidth(250);
        menuLateral.setStyle("-fx-background-color: rgba(60, 60, 60, 0.85); -fx-border-color: #444; -fx-border-width: 0 0 0 2;");
        menuLateral.setVisible(true);

        var btnPlanete = new Button("Ajoutez une planète");
        btnPlanete.setMaxWidth(Double.MAX_VALUE);
        menuLateral.getChildren().add(btnPlanete);

        btnPlanete.setOnAction(e -> {
            simulation.ajouterNouvellePlanete();
        });

        Button btnAfficher = new Button("☰");
        btnAfficher.setVisible(false);
        Button btnMasquer = new Button("☰");

        btnMasquer.setOnAction(e -> {
            menuLateral.setVisible(false);
            btnMasquer.setVisible(false);
            btnAfficher.setVisible(true);
        });

        btnAfficher.setOnAction(e -> {
            menuLateral.setVisible(true);
            btnMasquer.setVisible(true);
            btnAfficher.setVisible(false);
        });

        StackPane racine = new StackPane();
        racine.setStyle("-fx-background-color: black;");

        StackPane.setAlignment(menuLateral, Pos.TOP_RIGHT);
        StackPane.setAlignment(btnAfficher, Pos.TOP_RIGHT);
        StackPane.setAlignment(btnMasquer, Pos.TOP_RIGHT);

        Pane starLayer = new Pane();
        StarField starField = new StarField(starLayer, 2500.0);
        starField.start();

        StackPane centre = new StackPane(starLayer, canvas);
        racine.getChildren().addAll(centre, menuLateral, btnAfficher, btnMasquer);

        javafx.geometry.Insets marges = new javafx.geometry.Insets(10);
        StackPane.setMargin(btnAfficher, marges);
        StackPane.setMargin(btnMasquer, marges);

        Scene scene = new Scene(racine, LARGEUR, HAUTEUR);

        InterfaceGraphique gui = new InterfaceGraphique();
        simulation = new Simulation();

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