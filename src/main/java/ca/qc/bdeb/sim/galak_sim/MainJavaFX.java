package ca.qc.bdeb.sim.galak_sim;

import ca.qc.bdeb.sim.galak_sim.addons.Input;
import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import ca.qc.bdeb.sim.galak_sim.graphics.ChampEtoiles;
import ca.qc.bdeb.sim.galak_sim.graphics.Simulation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MainJavaFX extends Application {
    private static Simulation simulation;
    public final static double LARGEUR = 1200;
    public final static double HAUTEUR = 700;

    private double dernierX;
    private double dernierY;
    private boolean cameraEnDeplacement = false;
    private boolean pause = false;
    private double vitesseSimulation = 1.0;

    private final Map<Planete, Stage> fenetresOuvertes = new HashMap<>();

    @Override
    public void start(Stage stage) {
        var panneau = new StackPane();
        Scene scene = new Scene(panneau, LARGEUR, HAUTEUR);
        Input input = new Input();
        input.etatTouches(scene);
        Canvas canvas = new Canvas(LARGEUR, HAUTEUR);
        canvas.setCursor(Cursor.HAND);
        simulation = new Simulation();
        creerInterface(panneau, canvas);

        var contexte = canvas.getGraphicsContext2D();

        canvas.widthProperty().bind(panneau.widthProperty());
        canvas.heightProperty().bind(panneau.heightProperty());



        // Zoom avec molette
        canvas.setOnScroll(e -> {
            double facteur = e.getDeltaY() > 0 ? 1.1 : 0.9;
            simulation.zoomer(facteur, e.getX(), e.getY(), canvas.getWidth(), canvas.getHeight());
        });

        // Déplacement caméra avec bouton secondaire
        canvas.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                canvas.setCursor(Cursor.CLOSED_HAND);
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
                canvas.setCursor(Cursor.HAND);
                cameraEnDeplacement = false;
            }
        });

        stage.setOnCloseRequest(e -> {
            fenetresOuvertes.values().forEach(Stage::close);
        });

        AnimationTimer timer = new AnimationTimer() {

            private long dernierTemps = System.nanoTime();
            @Override
            public void handle(long temps) {
                double deltaTemps = (temps - dernierTemps) * 1e-9 * vitesseSimulation;
                contexte.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                if(!pause) {
                    simulation.update(deltaTemps);
                }

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

        //Nom
        var texteNom = new Text("Nom");
        texteNom.setFill(Color.WHITE);
        var saisiNom = new TextField();

        // Vitesses
        var texteVitesseX = new Text("Vitesse en x");
        texteVitesseX.setFill(Color.WHITE);
        var saisiVitesseX = new TextField("0");
        saisiVitesseX.setTextFormatter(formateurNumerique());
        var texteVitesseY = new Text("Vitesse en y");
        texteVitesseY.setFill(Color.WHITE);
        var saisiVitesseY = new TextField("0");
        saisiVitesseY.setTextFormatter(formateurNumerique());

        var texteMasse = new Text("Masse");
        texteMasse.setFill(Color.WHITE);
        var saisiMasse = new TextField("5");
        saisiMasse.setTextFormatter(formateurNumeriqueMasse());

        canvas.setOnMouseClicked(e -> {
            ajouterPlanete(e, canvas, saisiVitesseX, saisiVitesseY, saisiMasse, saisiNom, listePlanete);
        });

        var texteAjoutPlanete = new Text("Cliquez gauche pour ajouter une planète\nMolette pour zoomer\nClic droit pour déplacer la vue");
        texteAjoutPlanete.setFill(Color.WHITE);

        Button btnResetVue = new Button("Réinitialiser la vue");
        btnResetVue.setOnAction(e -> simulation.reinitialiserVue());

        var defileurPlanetes = new ScrollPane(listePlanete);
        defileurPlanetes.setFitToWidth(true);
        defileurPlanetes.setPrefHeight(200);
        defileurPlanetes.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        Text vitesseTexte = new Text("Vitesse de la simulation: ×"+ vitesseSimulation);
        vitesseTexte.setFill(Color.WHITE);
        vitesseTexte.setTextAlignment(TextAlignment.CENTER);

        HBox modificationTemps = new HBox();

        Button btnPause = new Button("⏸");
        btnPause.setAlignment(Pos.CENTER);
        btnPause.setOnAction(e -> {
            if (pause) {
                pause = false;
                btnPause.setText("⏸");
            } else {
                pause = true;
                btnPause.setText("▶");

            }
        });

        Button btnPlusVite = new Button("⏩");
        Button btnMoinsVite = new Button("⏪");

        btnPlusVite.setOnAction(e -> {
            vitesseSimulation *= 2;
            vitesseTexte.setText("Vitesse de la simulation: ×" + vitesseSimulation);
        });

        btnMoinsVite.setOnAction(e -> {
            vitesseSimulation /= 2;
            vitesseTexte.setText("Vitesse de la simulation: ×" + vitesseSimulation);
        });

        modificationTemps.getChildren().addAll(
                btnMoinsVite,
                btnPause,
                btnPlusVite
        );
        modificationTemps.setAlignment(Pos.CENTER);
        modificationTemps.setSpacing(10);

        Region espaceur = new Region();
        VBox.setVgrow(espaceur, Priority.ALWAYS);

        menuLateral.getChildren().addAll(
                texteNom,
                saisiNom,
                texteVitesseX,
                saisiVitesseX,
                texteVitesseY,
                saisiVitesseY,
                texteMasse,
                saisiMasse,
                texteAjoutPlanete,
                btnResetVue,
                defileurPlanetes,
                espaceur,
                vitesseTexte,
                modificationTemps
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

    public static TextFormatter<String> formateurNumerique() {
        return new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("^-?$|^-?(0|[1-9]\\d*)([.,]\\d*)?$")) {
                return change;
            }
            return null;
        });
    }

    public static TextFormatter<String> formateurNumeriqueMasse() {
        return new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("^$|^(0|[1-9]\\d*)([.,]\\d*)?$")) {
                return change;
            }
            return null;
        });
    }

    private void ajouterPlanete(MouseEvent e, Canvas canvas, TextField saisiVitesseX, TextField saisiVitesseY, TextField saisiMasse, TextField nom, VBox listePlanete) {
        if (e.getButton() != MouseButton.PRIMARY) {
            return;
        }

        // Paramètres récupérés
        Point2D monde = simulation.ecranVersMonde(e.getX(), e.getY(), canvas.getWidth(), canvas.getHeight());
        double x = monde.getX();
        double y = monde.getY();

        double vX = saisiVitesseX.getText().isEmpty() || saisiVitesseX.getText().equals("-") ? 0 : Double.parseDouble(saisiVitesseX.getText().replace(",", "."));
        double vY = saisiVitesseY.getText().isEmpty() || saisiVitesseY.getText().equals("-") ? 0 : Double.parseDouble(saisiVitesseY.getText().replace(",", "."));
        double masse = saisiMasse.getText().isEmpty() ? 0 : Double.parseDouble(saisiMasse.getText().replace(",", ".")) * 10e10;
        double taille = 50;



        var positionLibre = true;
        for (Planete p : simulation.getPlanetes()) {
            double distance = Math.sqrt(Math.pow(x - p.getPosition().getX(), 2) + Math.pow(y - p.getPosition().getY(), 2));

            if (distance < (p.getTaille().getX() / 2) + taille / 2) {
                positionLibre = false;
                ouvrirFenetreDetails(p, canvas);
                break;
            }
        }

        if (positionLibre) {
            String nomPlanete = nom.getText().isEmpty()
                    ? "Planète " + (simulation.getSizeListPlanetes() + 1)
                    : nom.getText();

            Planete nouvelle = simulation.ajouterNouvellePlanete(x, y, vX, vY, taille, masse, nomPlanete);

            HBox lignePlanete = new HBox(10);
            lignePlanete.setAlignment(Pos.CENTER_LEFT);

            Text info = new Text(simulation.dernierNomPlanete());
            info.setFill(Color.LIGHTGRAY);
            info.setOnMouseClicked(ev -> {
                ouvrirFenetreDetails(nouvelle, canvas);
            });

            Button btnSupprimer = new Button("X");
            btnSupprimer.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-font-size: 10;");

            btnSupprimer.setOnAction(ev -> {
                simulation.supprimerPlanete(nouvelle);
                listePlanete.getChildren().remove(lignePlanete);
            });

            lignePlanete.getChildren().addAll(btnSupprimer, info);
            listePlanete.getChildren().add(lignePlanete);
        }
        nom.clear();
    }

    private void ouvrirFenetreDetails(Planete p, Canvas canvas) {
        if (fenetresOuvertes.containsKey(p)) {
            fenetresOuvertes.get(p).toFront();
            return;
        }

        Stage fenetreDetails = new Stage();
        fenetreDetails.setTitle(p.getNom());

        fenetresOuvertes.put(p, fenetreDetails);
        fenetreDetails.setOnCloseRequest(e -> {
            fenetresOuvertes.remove(p);
        });

        simulation.centrerSur(p, 1.0);

        double centreCanvasX = canvas.localToScreen(canvas.getBoundsInLocal()).getMinX() + (canvas.getWidth() / 2);
        double centreCanvasY = canvas.localToScreen(canvas.getBoundsInLocal()).getMinY() + (canvas.getHeight() / 2);

        double decalageAleatoire = fenetresOuvertes.size() * 10;
        fenetreDetails.setX(centreCanvasX - 350 - decalageAleatoire);
        fenetreDetails.setY(centreCanvasY - 125 + decalageAleatoire);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #1a1a1a;");

        Text titre = new Text("DONNÉES TÉLÉMÉTRIQUES");
        titre.setFill(Color.WHITE);
        titre.setStyle("-fx-font-weight: bold;");

        Text txtPos = new Text();
        txtPos.setFill(Color.WHITE);
        Text txtVit = new Text();
        txtVit.setFill(Color.WHITE);
        Text txtAcc = new Text();
        txtAcc.setFill(Color.WHITE);
        Text txtmasse = new Text();
        txtmasse.setFill(Color.WHITE);

        DecimalFormat df = new DecimalFormat("#.####");

        // Creation Graphique

        //Axes
        NumberAxis axeX = new NumberAxis();
        axeX.setLabel(" Temps (s) ");
        axeX.setForceZeroInRange(false);
        axeX.setAutoRanging(false);
        axeX.setTickUnit(10);

        NumberAxis axeY = new NumberAxis();
        axeY.setLabel("Vitesse (m/s) ");

        //Graphique
        LineChart<Number,Number> graphVitesse = new LineChart<>(axeX,axeY);
        graphVitesse.setTitle("Évolution de vitesse");
        graphVitesse.setAnimated(false);
        graphVitesse.setCreateSymbols(false);

        //Lignes sur graphique pour vitesse X et Y
        XYChart.Series<Number,Number> serieVitesseX = new XYChart.Series<>();
        serieVitesseX.setName(" Vitesse X ");
        XYChart.Series<Number, Number> serieVitesseY = new XYChart.Series<>();
        serieVitesseY.setName(" Vitesse Y ");
        graphVitesse.getData().addAll(serieVitesseX,serieVitesseY);

        //Temps inital pour graphique
        long[] tempsDebut ={System.nanoTime()};

      // Oragnisation de la fenetre

        VBox boiteDonnees = new VBox(15);
        boiteDonnees.setPadding(new Insets(15,0,15,0));
        boiteDonnees.getChildren().addAll(txtPos, txtVit, txtAcc, txtmasse);

        VBox boiteGraphs = new VBox(15);
        boiteGraphs.setPadding(new Insets(15,0,15,0));
        boiteGraphs.getChildren().addAll(graphVitesse);
        boiteGraphs.setVisible(false);
        boiteGraphs.setManaged(false);

        //Creation du conteneur
        StackPane stackPane = new StackPane(boiteDonnees, boiteGraphs);

        //Boutons pour choisir vue
        Button bDonnees = new Button(" Données ");
        Button bGraphs = new Button(" Graphiques ");
        //Style des boutons
        String actif = "-fx-background-color: #444444; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20 8 20; -fx-background-radius: 5;";
        String nonactif = "-fx-background-color: transparent; -fx-text-fill: #888888; -fx-font-weight: bold; -fx-padding: 8 20 8 20; -fx-cursor: hand;";
        bDonnees.setStyle(actif);
        bGraphs.setStyle(nonactif);
        HBox contBoutons = new HBox(10, bDonnees, bGraphs);
        contBoutons.setStyle("-fx-background-color: #222222; -fx-padding: 5; -fx-background-radius: 8;");

        // Changement de vue
        bDonnees.setOnAction(e -> {
            boiteDonnees.setVisible(true);
            boiteDonnees.setManaged(true);
            boiteGraphs.setVisible(false);
            boiteGraphs.setManaged(false);
            bDonnees.setStyle(actif);
            bGraphs.setStyle(nonactif);
        });

        bGraphs.setOnAction(e -> {
            boiteGraphs.setVisible(true);
            boiteGraphs.setManaged(true);
            boiteDonnees.setVisible(false);
            boiteDonnees.setManaged(false);
            bGraphs.setStyle(actif);
            bDonnees.setStyle(nonactif);
        });

        titre.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-fill: white;");
        VBox header = new VBox(titre);
        header.setPadding(new Insets(0,0,15,0));

        // L'AnimationTimer spécifique à cette fenêtre
        AnimationTimer rafraichisseur = new AnimationTimer() {

            private long dernierTempsGraph = System.nanoTime(); //tracker pour graphique

            @Override
            public void handle(long now) {
                //Logique pour les donnees affiches
                // Si la planète a été supprimée
                if (!simulation.getPlanetes().contains(p)) {
                    this.stop();
                    fenetreDetails.close();
                    fenetresOuvertes.remove(p);
                    return;
                }
                txtPos.setText("Position X : " + df.format(p.getPosition().getX()) + " unité" +
                        "\nPosition Y : " + df.format(p.getPosition().getY()) + " unité");
                double vitesseAbsolue = Math.sqrt(Math.pow(p.getVelocite().getX(), 2) + Math.pow(p.getVelocite().getY(), 2));
                txtVit.setText("Vitesse X : " + df.format(p.getVelocite().getX()) + " unité" +
                        "\nVitesse Y : " + df.format(p.getVelocite().getY()) + " unité" +
                        "\nVitesse : " + df.format(vitesseAbsolue) + " unité");
                double accelerationAbsolue = Math.sqrt(Math.pow(p.getAcceleration().getX(), 2) + Math.pow(p.getAcceleration().getY(), 2));
                txtAcc.setText("Accélération X : " + df.format(p.getAcceleration().getX()) + " unité" +
                        "\nAccélération Y : " + df.format(p.getAcceleration().getY()) + " unité" +
                        "\nAccélération : " + df.format(accelerationAbsolue) + " unité");
                txtmasse.setText("Masse " + df.format(p.getMasse()) + " unité");

                //Logique pour graphique
                if (!pause) {
                    if ((now - dernierTempsGraph) > 500000000) {
                        //Temps
                        double tempsEcoule = ((now - tempsDebut[0]) * 1e-9);
                        //Chercher donnees
                        double vitesseX = p.getVelocite().getX();
                        double vitesseY = p.getVelocite().getY();
                        //Ajouter sur graphique
                        serieVitesseX.getData().add(new XYChart.Data<>(tempsEcoule, vitesseX));
                        serieVitesseY.getData().add(new XYChart.Data<>(tempsEcoule, vitesseY));

                        //Taille de graphique
                        double tailleFenetreGraph = 60;
                        axeX.setLowerBound(Math.max(0, tempsEcoule - tailleFenetreGraph));
                        axeX.setUpperBound(Math.max(tailleFenetreGraph, tempsEcoule));

                        //Si on a trop de points
                        if (serieVitesseX.getData().size() > 250) {
                            serieVitesseX.getData().removeFirst();
                            serieVitesseY.getData().removeFirst();

                        }
                        dernierTempsGraph = now;
                    }
                } else {
                    long tempsEnPause = now - dernierTempsGraph;
                    tempsDebut[0] = tempsDebut[0] + tempsEnPause;
                    dernierTempsGraph = now;
                }
            }
        };
        rafraichisseur.start();

        layout.getChildren().clear();
        layout.getChildren().addAll(header,contBoutons,stackPane);
        Scene scene = new Scene(layout, 450, 550);
        scene.getStylesheets().add(getClass().getResource("/styleGraphiques.css").toExternalForm());
        fenetreDetails.setResizable(false);
        fenetreDetails.setScene(scene);
        fenetreDetails.setAlwaysOnTop(true);
        fenetreDetails.show();
    }
}