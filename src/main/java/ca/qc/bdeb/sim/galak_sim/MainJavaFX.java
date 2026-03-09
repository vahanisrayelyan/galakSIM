package ca.qc.bdeb.sim.galak_sim;

import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import ca.qc.bdeb.sim.galak_sim.graphics.Simulation;
import ca.qc.bdeb.sim.galak_sim.graphics.StarField;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
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

        // Vitesses
        var texteVitesseX = new Text("Vitesse en x");
        texteVitesseX.setFill(Color.WHITE);
        var saisiVitesseX = new TextField("0");
        saisiVitesseX.setTextFormatter(formatteurNumerique());
        var texteVitesseY = new Text("Vitesse en y");
        texteVitesseY.setFill(Color.WHITE);
        var saisiVitesseY = new TextField("0");
        saisiVitesseY.setTextFormatter(formatteurNumerique());

        // Masse
        var texteMasse = new Text("Masse");
        texteMasse.setFill(Color.WHITE);
        var saisiMasse = new TextField("50");
        saisiMasse.setTextFormatter(formatteurNumeriqueMasse());

        // Position et ajout de la planète
        canvas.setOnMouseClicked(e -> {
            ajouterPlanete(e, saisiVitesseX, saisiVitesseY, saisiMasse, listePlanete);
        });
        var texteAjoutPlanete = new Text("Cliquez sur l'écran pour ajouter une planète");
        texteAjoutPlanete.setFill(Color.WHITE);

        // Liste des planètes
        var defileurPlanetes = new ScrollPane(listePlanete);
        defileurPlanetes.setFitToWidth(true);
        defileurPlanetes.setPrefHeight(200);
        defileurPlanetes.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        menuLateral.getChildren().addAll(
                texteVitesseX,
                saisiVitesseX,
                texteVitesseY,
                saisiVitesseY,
                texteMasse,
                saisiMasse,
                texteAjoutPlanete,
                defileurPlanetes
        );

        // Affichage du menu
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

        // Étoiles
        Pane starLayer = new Pane();
        starLayer.prefWidthProperty().bind(canvas.widthProperty());
        starLayer.prefHeightProperty().bind(canvas.heightProperty());
        StarField starField = new StarField(starLayer, 1500);
        starField.start();
        StackPane centre = new StackPane(starLayer, canvas);

        StackPane.setAlignment(menuLateral, Pos.TOP_RIGHT);
        StackPane.setAlignment(btnAfficher, Pos.TOP_RIGHT);
        StackPane.setAlignment(btnMasquer, Pos.TOP_RIGHT);

        Insets marges = new Insets(10);
        StackPane.setMargin(btnAfficher, marges);
        StackPane.setMargin(btnMasquer, marges);

        panneau.getChildren().addAll(centre, menuLateral, btnAfficher, btnMasquer);
    }

    public static TextFormatter<String> formatteurNumerique() {
        return new TextFormatter<>(change -> {
           if (change.getControlNewText().matches("^-?$|^-?(0|[1-9]\\d*)([.,]\\d*)?$")) {
               return change;
           }
           return null;
        });
    }

    public static TextFormatter<String> formatteurNumeriqueMasse() {
        return new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("^$|^(0|[1-9]\\d*)([.,]\\d*)?$")) {
                return change;
            }
            return null;
        });
    }

    private static void ajouterPlanete(MouseEvent e, TextField saisiVitesseX, TextField saisiVitesseY, TextField saisiMasse, VBox listePlanete) {
        if (e.getButton() != MouseButton.PRIMARY) {
            return;
        }

        // Paramètres récupérés
        double x = e.getX();
        double y = e.getY();
        double vX = saisiVitesseX.getText().isEmpty() || saisiVitesseX.getText().equals("-") ? 0 : Double.parseDouble(saisiVitesseX.getText().replace(",", "."));
        double vY = saisiVitesseY.getText().isEmpty() || saisiVitesseY.getText().equals("-") ? 0 : Double.parseDouble(saisiVitesseY.getText().replace(",", "."));
        double masse = saisiMasse.getText().isEmpty() ? 0 : Double.parseDouble(saisiMasse.getText().replace(",", "."));
        double taille = masse * 0.1;

        // Vérification pour éviter de mettre une planète sur une autre
        var positionLibre = true;
        for (Planete p : simulation.getPlanetes()) {
            double distance = Math.sqrt(Math.pow(x - p.getPosition().getX(), 2) + Math.pow(y - p.getPosition().getY(), 2));
            if (distance < (p.getTaille().getX() / 2) + taille / 2) {
                positionLibre = false;
                break;
            }
        }

        // Gestion des planètes
        if (positionLibre) {
            Planete nouvelle = simulation.ajouterNouvellePlanete(x, y, vX, vY, taille, masse);

            HBox lignePlanete = new HBox(10);
            lignePlanete.setAlignment(Pos.CENTER_LEFT);

            Text info = new Text("Planète " + (listePlanete.getChildren().size() + 1));
            info.setFill(Color.LIGHTGRAY);

            // Suppression de la planète
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