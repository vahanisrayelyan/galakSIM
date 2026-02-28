package ca.qc.bdeb.sim.galak_sim;

import ca.qc.bdeb.sim.galak_sim.graphics.Simulation;
import ca.qc.bdeb.sim.galak_sim.graphics.StarField;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MainJavaFX extends Application {
    private static Simulation simulation;
    public final static double LARGEUR = 1200;
    public final static double HAUTEUR = 700;

    @Override
    public void start(Stage stage) throws IOException {

        Canvas canvas = new Canvas(LARGEUR, HAUTEUR);
        var contexte = canvas.getGraphicsContext2D();

        StackPane racine = configurerInterface(canvas);

        canvas.widthProperty().bind(racine.widthProperty());
        canvas.heightProperty().bind(racine.heightProperty());

        Scene scene = new Scene(racine, LARGEUR, HAUTEUR);

        simulation = new Simulation();

        AnimationTimer timer = new AnimationTimer() {
            private long dernierTemps = System.nanoTime();

            @Override
            public void handle(long temps) {
                double deltaTemps = (temps - dernierTemps) * 1e-9;

                contexte.clearRect(0,0, canvas.getWidth(), canvas.getHeight());

                simulation.update(deltaTemps);
                simulation.draw(contexte);

                dernierTemps = temps;
            }
        };
        timer.start();

        stage.getIcons().add(new Image("logoSansFond.png"));
        stage.setTitle("GalakSIM");
        stage.setScene(scene);
        stage.show();
    }

    private StackPane configurerInterface(Canvas canvas) {
        VBox menuLateral = new VBox(15);
        menuLateral.setPadding(new javafx.geometry.Insets(60, 15, 15, 15));
        menuLateral.setMaxWidth(250);
        menuLateral.setStyle("-fx-background-color: rgba(60, 60, 60, 0.85); -fx-border-color: #444; -fx-border-width: 0 0 0 2;");
        menuLateral.setVisible(true);


        // Option de personnalisation de la planète

        // Position

        // Vitesse
        var sliderVitesseX = new Slider(-100, 100, 0);
        sliderVitesseX.setShowTickMarks(true);
        sliderVitesseX.setShowTickLabels(true);
        var sliderVitesseY = new Slider(-100, 100, 0);
        sliderVitesseY.setShowTickMarks(true);
        sliderVitesseY.setShowTickLabels(true);

        // Taille

        // Ajout
        var btnPlanete = new Button("Ajoutez une planète");
        btnPlanete.setMaxWidth(Double.MAX_VALUE);
        btnPlanete.setOnAction(e -> {
            var vX = sliderVitesseX.getValue();
            var vY = sliderVitesseY.getValue();

            System.out.println(vX + " " + vY);
            simulation.ajouterNouvellePlanete(50, 50, vX, vY, 50);
        });

        menuLateral.getChildren().addAll(sliderVitesseX, sliderVitesseY, btnPlanete);

        //Affichage du menu
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

        //Étoiles
        Pane starLayer = new Pane();
        starLayer.prefWidthProperty().bind(canvas.widthProperty());
        starLayer.prefHeightProperty().bind(canvas.heightProperty());
        StarField starField = new StarField(starLayer, 1500);
        starField.start();
        StackPane centre = new StackPane(starLayer, canvas);

        StackPane racine = new StackPane();
        racine.setStyle("-fx-background-color: black;");

        StackPane.setAlignment(menuLateral, Pos.TOP_RIGHT);
        StackPane.setAlignment(btnAfficher, Pos.TOP_RIGHT);
        StackPane.setAlignment(btnMasquer, Pos.TOP_RIGHT);

        javafx.geometry.Insets marges = new javafx.geometry.Insets(10);
        StackPane.setMargin(btnAfficher, marges);
        StackPane.setMargin(btnMasquer, marges);

        racine.getChildren().addAll(centre, menuLateral, btnAfficher, btnMasquer);

        return racine;
    }
}