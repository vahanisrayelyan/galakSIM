package ca.qc.bdeb.sim.galak_sim;

import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import ca.qc.bdeb.sim.galak_sim.graphics.ChampEtoiles;
import ca.qc.bdeb.sim.galak_sim.graphics.Simulation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

public class MainJavaFX extends Application {
    private static Simulation simulation;
    public final static double LARGEUR = 1200;
    public final static double HAUTEUR = 700;

    private double dernierX;
    private double dernierY;
    private boolean cameraEnDeplacement = false;

    @Override
    public void start(Stage stage) {
        var panneau = new StackPane();
        Scene scene = new Scene(panneau, LARGEUR, HAUTEUR);
        Canvas canvas = new Canvas(LARGEUR, HAUTEUR);
        creerInterface(panneau, canvas);

        var contexte = canvas.getGraphicsContext2D();

        canvas.widthProperty().bind(panneau.widthProperty());
        canvas.heightProperty().bind(panneau.heightProperty());

        simulation = new Simulation();

        // Zoom avec molette
        canvas.setOnScroll(e -> {
            double facteur = e.getDeltaY() > 0 ? 1.1 : 0.9;
            simulation.zoomer(facteur, e.getX(), e.getY(), canvas.getWidth(), canvas.getHeight());
        });

        // Déplacement caméra avec bouton secondaire
        canvas.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                cameraEnDeplacement = true;
                dernierX = e.getX();
                dernierY = e.getY();
            }
        });

        canvas.setOnMouseDragged(e -> {
            if (cameraEnDeplacement) {
                double dx = e.getX() - dernierX;
                double dy = e.getY() - dernierY;

                simulation.deplacerCamera(dx, dy);

                dernierX = e.getX();
                dernierY = e.getY();
            }
        });

        canvas.setOnMouseReleased(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                cameraEnDeplacement = false;
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            private long dernierTemps = System.nanoTime();

            @Override
            public void handle(long temps) {
                double deltaTemps = (temps - dernierTemps) * 1e-9;

                contexte.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                simulation.update(deltaTemps);
                simulation.draw(contexte);

                dernierTemps = temps;
            }
        };
        timer.start();

        stage.setMinWidth(1200);
        stage.setMinHeight(700);
        stage.getIcons().add(new Image("logoSansFond.png"));
        stage.setTitle("GalakSIM");
        stage.setScene(scene);
        stage.show();
    }

    private void creerInterface(StackPane panneau, Canvas canvas) {
        // Menu
        VBox menuLateral = new VBox(15);
        menuLateral.setPadding(new Insets(60, 15, 15, 15));
        menuLateral.setMaxWidth(250);
        menuLateral.setStyle("-fx-background-color: rgba(60, 60, 60, 0.85); -fx-border-color: #444; -fx-border-width: 0 0 0 2;");
        menuLateral.setVisible(true);
        VBox listePlanete = new VBox(5);

        // Vitesse
        var texteVitesseX = new Text("Vitesse en x");
        texteVitesseX.setFill(Color.WHITE);
        var sliderVitesseX = new Slider(-100, 100, 0);
        sliderVitesseX.setShowTickMarks(true);
        sliderVitesseX.setShowTickLabels(true);

        var texteVitesseY = new Text("Vitesse en y");
        texteVitesseY.setFill(Color.WHITE);
        var sliderVitesseY = new Slider(-100, 100, 0);
        sliderVitesseY.setShowTickMarks(true);
        sliderVitesseY.setShowTickLabels(true);

        // Taille
        var texteTaille = new Text("Taille");
        texteTaille.setFill(Color.WHITE);
        var sliderTaille = new Slider(10, 100, 50);
        sliderTaille.setShowTickMarks(true);
        sliderTaille.setShowTickLabels(true);

        var texteMasse = new Text("Masse");
        texteMasse.setFill(Color.WHITE);
        var sliderMasse = new Slider(10, 100, 50);
        sliderMasse.setShowTickMarks(true);
        sliderMasse.setShowTickLabels(true);

        canvas.setOnMouseClicked(e -> {
            ajouterPlanete(e, canvas, sliderVitesseX, sliderVitesseY, sliderTaille, sliderMasse, listePlanete);
        });

        var texteAjoutPlanete = new Text("Cliquez gauche pour ajouter une planète\nMolette pour zoomer\nClic droit pour déplacer la vue");
        texteAjoutPlanete.setFill(Color.WHITE);

        Button btnResetVue = new Button("Réinitialiser la vue");
        btnResetVue.setOnAction(e -> simulation.reinitialiserVue());

        var defileurPlanetes = new ScrollPane(listePlanete);
        defileurPlanetes.setFitToWidth(true);
        defileurPlanetes.setPrefHeight(200);
        defileurPlanetes.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        menuLateral.getChildren().addAll(
                texteVitesseX,
                sliderVitesseX,
                texteVitesseY,
                sliderVitesseY,
                texteTaille,
                sliderTaille,
                texteMasse,
                sliderMasse,
                texteAjoutPlanete,
                btnResetVue,
                defileurPlanetes
        );

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

        Pane starLayer = new Pane();
        starLayer.prefWidthProperty().bind(canvas.widthProperty());
        starLayer.prefHeightProperty().bind(canvas.heightProperty());


        ChampEtoiles champEtoiles = new ChampEtoiles(starLayer);
        champEtoiles.demarrer();

        StackPane centre = new StackPane(starLayer, canvas);

        StackPane.setAlignment(menuLateral, Pos.TOP_RIGHT);
        StackPane.setAlignment(btnAfficher, Pos.TOP_RIGHT);
        StackPane.setAlignment(btnMasquer, Pos.TOP_RIGHT);

        Insets marges = new Insets(10);
        StackPane.setMargin(btnAfficher, marges);
        StackPane.setMargin(btnMasquer, marges);

        panneau.getChildren().addAll(centre, menuLateral, btnAfficher, btnMasquer);
    }

    private static void ajouterPlanete(MouseEvent e, Canvas canvas, Slider sliderVitesseX, Slider sliderVitesseY,
                                       Slider sliderTaille, Slider sliderMasse, VBox listePlanete) {
        if (e.getButton() != MouseButton.PRIMARY) {
            return;
        }

        Point2D monde = simulation.ecranVersMonde(
                e.getX(),
                e.getY(),
                canvas.getWidth(),
                canvas.getHeight()
        );

        double x = monde.getX();
        double y = monde.getY();

        var vX = sliderVitesseX.getValue();
        var vY = sliderVitesseY.getValue();
        var taille = sliderTaille.getValue();
        var masse = sliderMasse.getValue();

        var positionLibre = true;
        for (Planete p : simulation.getPlanetes()) {
            double distance = Math.sqrt(
                    Math.pow(x - p.getPosition().getX(), 2) +
                            Math.pow(y - p.getPosition().getY(), 2)
            );

            if (distance < (p.getTaille().getX() / 2) + taille / 2) {
                positionLibre = false;
                break;
            }
        }

        if (positionLibre) {
            Planete nouvelle = simulation.ajouterNouvellePlanete(x, y, vX, vY, taille, masse);

            HBox lignePlanete = new HBox(10);
            lignePlanete.setAlignment(Pos.CENTER_LEFT);

            Text info = new Text("Planète " + (listePlanete.getChildren().size() + 1));
            info.setFill(Color.LIGHTGRAY);

            Button btnSupprimer = new Button("X");
            btnSupprimer.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-font-size: 10;");

            btnSupprimer.setOnAction(ev -> {
                simulation.supprimerPlanete(nouvelle);
                listePlanete.getChildren().remove(lignePlanete);
            });

            lignePlanete.getChildren().addAll(btnSupprimer, info);
            listePlanete.getChildren().add(lignePlanete);
        }
    }
}