package ca.qc.bdeb.sim.galak_sim;

import ca.qc.bdeb.sim.galak_sim.addons.Input;
import ca.qc.bdeb.sim.galak_sim.addons.Presets;
import ca.qc.bdeb.sim.galak_sim.addons.Vecteurs;
import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import ca.qc.bdeb.sim.galak_sim.graphics.ChampEtoiles;
import ca.qc.bdeb.sim.galak_sim.graphics.Simulation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MainJavaFX extends Application {
    private static Simulation simulation;
    public static final double LARGEUR = 1200;
    public static final double HAUTEUR = 700;

    private double dernierX;
    private double dernierY;
    private boolean cameraEnDeplacement = false;
    private boolean pause = false;

    private double vitesseSimulation = 1.0;

    private final Map<Planete, Stage> fenetresOuvertes = new HashMap<>();

    private VBox listePlaneteUI;
    private Canvas canvasPrincipal;
    private int nbPlanetesAvant = 0;

    private final Vecteurs vecteurs = new Vecteurs();

    private double tempsSimulation = 0;
    private Text texteTemps;

    @Override
    public void start(Stage stage) {
        StackPane panneau = new StackPane();
        Scene scene = new Scene(panneau, LARGEUR, HAUTEUR);

        Input input = new Input();
        input.etatTouches(scene);

        Canvas canvas = new Canvas(LARGEUR, HAUTEUR);
        canvas.setCursor(Cursor.HAND);


        simulation = new Simulation(vecteurs);
        creerInterface(panneau, canvas);

        GraphicsContext contexte = canvas.getGraphicsContext2D();

        canvas.widthProperty().bind(panneau.widthProperty());
        canvas.heightProperty().bind(panneau.heightProperty());

        canvas.setOnScroll(e -> {
            double facteur = e.getDeltaY() > 0 ? 1.1 : 0.9;
            simulation.zoomer(facteur, e.getX(), e.getY(), canvas.getWidth(), canvas.getHeight());
        });

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

        stage.setOnCloseRequest(e -> fenetresOuvertes.values().forEach(Stage::close));

        nbPlanetesAvant = simulation.getPlanetes().size();

        AnimationTimer timer = new AnimationTimer() {
            private long dernierTemps = System.nanoTime();

            @Override
            public void handle(long temps) {
                double deltaTemps = (temps - dernierTemps) * 1e-9 * vitesseSimulation;

                if (!pause) {
                    simulation.update(deltaTemps);

                    simulation.calculerPredictions(deltaTemps);
                    tempsSimulation += deltaTemps;
                }
                long totalSecondes = (long) tempsSimulation;
                long annees = totalSecondes / (365 * 24 * 3600);
                long jours  = (totalSecondes % (365 * 24 * 3600)) / (24 * 3600);
                long heures = (totalSecondes % (24 * 3600)) / 3600;
                long minutes = (totalSecondes % 3600) / 60;
                long secondes = totalSecondes % 60;

                texteTemps.setText(String.format("Temps : %d an(s) %d j %02dh %02dm %02ds",
                        annees, jours, heures, minutes, secondes));

                if (simulation.getPlanetes().size() != nbPlanetesAvant) {
                    rafraichirListePlanetes(listePlaneteUI, canvasPrincipal);
                    nbPlanetesAvant = simulation.getPlanetes().size();
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
        VBox menuLateral = new VBox(15);
        menuLateral.setPadding(new Insets(60, 15, 15, 15));
        menuLateral.setMaxWidth(250);
        menuLateral.setStyle("-fx-background-color: rgba(60, 60, 60, 0.85); -fx-border-color: #444; -fx-border-width: 0 0 0 2;");

        VBox boiteSpecs = new VBox(10);
        VBox boitePresets = new VBox(10);

        VBox listePlanete = new VBox(5);
        this.listePlaneteUI = listePlanete;
        this.canvasPrincipal = canvas;
        texteTemps = new Text("Temps : 0.0 s");
        texteTemps.setFill(Color.WHITE);
        texteTemps.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        StackPane.setAlignment(texteTemps, Pos.TOP_LEFT);
        StackPane.setMargin(texteTemps, new Insets(10));

        Text texteNom = new Text("Nom");
        texteNom.setFill(Color.WHITE);
        TextField saisiNom = new TextField();

        Text texteVitesseX = new Text("Vitesse en x");
        texteVitesseX.setFill(Color.WHITE);

        HBox hboxVitesseX = new HBox(10);
        TextField saisiVitesseX = new TextField("0");
        saisiVitesseX.setTextFormatter(formateurNumerique());
        HBox.setHgrow(saisiVitesseX, Priority.ALWAYS);
        saisiVitesseX.setMaxWidth(Double.MAX_VALUE);
        Text unitevx = new Text("m/s");
        unitevx.setFill(Color.WHITE);
        hboxVitesseX.setAlignment(Pos.CENTER_LEFT);
        hboxVitesseX.getChildren().addAll(saisiVitesseX, unitevx);

        Text texteVitesseY = new Text("Vitesse en y");
        texteVitesseY.setFill(Color.WHITE);

        HBox hboxVitesseY = new HBox(10);
        TextField saisiVitesseY = new TextField("0");
        saisiVitesseY.setTextFormatter(formateurNumerique());
        HBox.setHgrow(saisiVitesseY, Priority.ALWAYS);
        saisiVitesseY.setMaxWidth(Double.MAX_VALUE);
        Text unitevy = new Text("m/s");
        unitevy.setFill(Color.WHITE);
        hboxVitesseY.setAlignment(Pos.CENTER_LEFT);
        hboxVitesseY.getChildren().addAll(saisiVitesseY, unitevy);

        Text texteMasse = new Text("Masse");
        texteMasse.setFill(Color.WHITE);

        HBox hboxMasse = new HBox(10);
        TextField saisiMasse = new TextField("5");
        saisiMasse.setTextFormatter(formateurNumeriqueMasse());
        HBox.setHgrow(saisiMasse, Priority.ALWAYS);
        saisiMasse.setMaxWidth(Double.MAX_VALUE);
        Text uniteMasse = new Text("kg");
        uniteMasse.setFill(Color.WHITE);
        hboxMasse.setAlignment(Pos.CENTER_LEFT);
        hboxMasse.getChildren().addAll(saisiMasse, uniteMasse);

        canvas.setOnMouseClicked(e ->
                ajouterPlanete(e, canvas, saisiVitesseX, saisiVitesseY, saisiMasse, saisiNom, listePlanete)
        );

        Text texteAjoutPlanete = new Text("Cliquez gauche pour ajouter une planète\nMolette pour zoomer\nClic droit pour déplacer la vue");
        texteAjoutPlanete.setFill(Color.WHITE);

        Button btnResetVue = new Button("Réinitialiser la vue");
        btnResetVue.setOnAction(e -> simulation.reinitialiserVue());

        Text choixModeVecteurText = new Text("Choix du mode d'affichage des vecteurs:");
        choixModeVecteurText.setFill(Color.WHITE);

        VBox choixVecteursVBox = new VBox();
        choixVecteursVBox.setSpacing(2);

        RadioButton choixPasVecteurs = new RadioButton("Aucun");
        choixPasVecteurs.setTextFill(Color.WHITE);
        choixPasVecteurs.setOnAction(e -> vecteurs.setChoix(0));
        choixVecteursVBox.getChildren().add(choixPasVecteurs);
        choixPasVecteurs.setSelected(true);

        RadioButton choixVecteurVitesse = new RadioButton("Vitesse");
        choixVecteurVitesse.setTextFill(Color.WHITE);
        choixVecteurVitesse.setOnAction(e -> vecteurs.setChoix(1));
        choixVecteursVBox.getChildren().add(choixVecteurVitesse);

        RadioButton choixVecteurAcceleration = new RadioButton("Acceleration");
        choixVecteurAcceleration.setTextFill(Color.WHITE);
        choixVecteurAcceleration.setOnAction(e -> vecteurs.setChoix(2));
        choixVecteursVBox.getChildren().add(choixVecteurAcceleration);

        RadioButton choixVecteurForceGravitationnelle = new RadioButton("Force");
        choixVecteurForceGravitationnelle.setOnAction(e -> vecteurs.setChoix(3));
        choixVecteurForceGravitationnelle.setTextFill(Color.WHITE);
        choixVecteursVBox.getChildren().add(choixVecteurForceGravitationnelle);


        ToggleGroup choixVecteursToggleGroup = new ToggleGroup();
        choixPasVecteurs.setToggleGroup(choixVecteursToggleGroup);
        choixVecteurVitesse.setToggleGroup(choixVecteursToggleGroup);
        choixVecteurAcceleration.setToggleGroup(choixVecteursToggleGroup);
        choixVecteurForceGravitationnelle.setToggleGroup(choixVecteursToggleGroup);


        ScrollPane defileurPlanetes = new ScrollPane(listePlanete);
        defileurPlanetes.setFitToWidth(true);
        VBox.setVgrow(defileurPlanetes, Priority.ALWAYS);
        defileurPlanetes.setMaxHeight(Double.MAX_VALUE);
        defileurPlanetes.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        Text vitesseTexte = new Text("Vitesse de la simulation: x" + vitesseSimulation);
        vitesseTexte.setFill(Color.WHITE);
        vitesseTexte.setTextAlignment(TextAlignment.CENTER);

        HBox modificationTemps = new HBox();
        modificationTemps.setSpacing(10);

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

        modificationTemps.getChildren().addAll(btnMoinsVite, btnPause, btnPlusVite);

        boiteSpecs.getChildren().addAll(
                texteNom,
                saisiNom,
                texteVitesseX,
                hboxVitesseX,
                texteVitesseY,
                hboxVitesseY,
                texteMasse,
                hboxMasse,
                texteAjoutPlanete,
                btnResetVue,
                choixModeVecteurText,
                choixVecteursVBox,
                defileurPlanetes,
                vitesseTexte,
                modificationTemps
        );

        Text titrePresets = new Text("Presets");
        titrePresets.setFill(Color.WHITE);

        Button btnSysteme = new Button("Système solaire");
        btnSysteme.setOnAction(e -> {
            Presets.chargerSystemeSolaire(simulation);
            rafraichirListePlanetes(listePlanete, canvas);
            nbPlanetesAvant = simulation.getPlanetes().size();
        });

        Button btnVide = new Button("Vide");
        btnVide.setOnAction(e -> {
            simulation.viderPlanetes();
            rafraichirListePlanetes(listePlanete, canvas);
            nbPlanetesAvant = simulation.getPlanetes().size();
        });

        Button btnCollision = new Button("Collision");
        btnCollision.setOnAction(e -> {
            simulation.viderPlanetes();
            simulation.ajouterNouvellePlanete(400, 350, 2, 0, 20, 100, "A");
            simulation.ajouterNouvellePlanete(800, 350, -2, 0, 20, 100, "B");
            rafraichirListePlanetes(listePlanete, canvas);
            nbPlanetesAvant = simulation.getPlanetes().size();
        });

        boitePresets.getChildren().addAll(
                titrePresets,
                btnSysteme,
                btnVide,
                btnCollision
        );

        StackPane stackSections = new StackPane(boiteSpecs, boitePresets);

        boiteSpecs.setVisible(true);
        boiteSpecs.setManaged(true);
        boitePresets.setVisible(false);
        boitePresets.setManaged(false);

        Button bSpecs = new Button(" Specs ");
        Button bPresets = new Button(" Presets ");

        String actif = "-fx-background-color: #444444; -fx-text-fill: white; -fx-font-weight: bold;";
        String nonactif = "-fx-background-color: transparent; -fx-text-fill: #888888;";

        bSpecs.setStyle(actif);
        bPresets.setStyle(nonactif);

        HBox contBoutons = new HBox(10, bSpecs, bPresets);
        contBoutons.setStyle("-fx-background-color: #222222; -fx-padding: 5; -fx-background-radius: 8;");

        bSpecs.setOnAction(e -> {
            boiteSpecs.setVisible(true);
            boiteSpecs.setManaged(true);
            boitePresets.setVisible(false);
            boitePresets.setManaged(false);
            bSpecs.setStyle(actif);
            bPresets.setStyle(nonactif);
        });

        bPresets.setOnAction(e -> {
            boitePresets.setVisible(true);
            boitePresets.setManaged(true);
            boiteSpecs.setVisible(false);
            boiteSpecs.setManaged(false);
            bPresets.setStyle(actif);
            bSpecs.setStyle(nonactif);
        });

        menuLateral.getChildren().addAll(contBoutons, stackSections);

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

        nbPlanetesAvant = simulation.getPlanetes().size();

        StackPane.setAlignment(texteTemps, Pos.TOP_LEFT);
        StackPane.setMargin(texteTemps, new Insets(10));

        panneau.getChildren().addAll(centre, menuLateral, btnAfficher, btnMasquer, texteTemps);
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

    private void ajouterPlanete(MouseEvent e, Canvas canvas, TextField saisiVitesseX, TextField saisiVitesseY,
                                TextField saisiMasse, TextField saisiNom, VBox listePlanete) {
        if (e.getButton() != MouseButton.PRIMARY) {
            return;
        }

        Point2D monde = simulation.ecranVersMonde(e.getX(), e.getY(), canvas.getWidth(), canvas.getHeight());
        double x = monde.getX();
        double y = monde.getY();

        double vX = saisiVitesseX.getText().isEmpty() || saisiVitesseX.getText().equals("-")
                ? 0
                : Double.parseDouble(saisiVitesseX.getText().replace(",", ".")) * 10e7;

        double vY = saisiVitesseY.getText().isEmpty() || saisiVitesseY.getText().equals("-")
                ? 0
                : Double.parseDouble(saisiVitesseY.getText().replace(",", ".")) * 10e7;

        double masse = saisiMasse.getText().isEmpty()
                ? 0
                : Double.parseDouble(saisiMasse.getText().replace(",", ".")) * 10e14;

        double taille = 50;

        boolean positionLibre = true;
        for (Planete p : simulation.getPlanetes()) {
            double distance = Math.sqrt(Math.pow(x - p.getPosition().getX(), 2) + Math.pow(y - p.getPosition().getY(), 2));

            if (distance < (p.getTaille().getX() / 2) + taille / 2) {
                positionLibre = false;
                ouvrirFenetreDetails(p, canvas);
                break;
            }
        }

        if (positionLibre) {
            String nomPlanete = saisiNom.getText().isEmpty()
                    ? "Planète " + (simulation.getSizeListPlanetes() + 1)
                    : saisiNom.getText();

            simulation.ajouterNouvellePlanete(x, y, vX, vY, taille, masse, nomPlanete);
            rafraichirListePlanetes(listePlanete, canvas);
            nbPlanetesAvant = simulation.getPlanetes().size();
        }

        saisiNom.clear();
    }

    private void rafraichirListePlanetes(VBox listePlanete, Canvas canvas) {
        listePlanete.getChildren().clear();

        for (Planete p : simulation.getPlanetes()) {
            HBox lignePlanete = new HBox(10);
            lignePlanete.setAlignment(Pos.CENTER_LEFT);

            Text info = new Text(p.getNom());
            info.setFill(Color.LIGHTGRAY);
            info.setOnMouseClicked(ev -> ouvrirFenetreDetails(p, canvas));

            Button btnSupprimer = new Button("X");
            btnSupprimer.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-font-size: 10;");

            btnSupprimer.setOnAction(ev -> {
                simulation.supprimerPlanete(p);
                rafraichirListePlanetes(listePlanete, canvas);
                nbPlanetesAvant = simulation.getPlanetes().size();
            });

            lignePlanete.getChildren().addAll(btnSupprimer, info);
            listePlanete.getChildren().add(lignePlanete);
        }
    }

    private void ouvrirFenetreDetails(Planete p, Canvas canvas) {
        if (fenetresOuvertes.containsKey(p)) {
            fenetresOuvertes.get(p).toFront();
            return;
        }

        Stage fenetreDetails = new Stage();
        fenetreDetails.setTitle(p.getNom());

        fenetresOuvertes.put(p, fenetreDetails);
        fenetreDetails.setOnCloseRequest(e -> fenetresOuvertes.remove(p));

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

        Text txtMasse = new Text();
        txtMasse.setFill(Color.WHITE);

        DecimalFormat df = new DecimalFormat("#.####");

        NumberAxis axeX = new NumberAxis();
        axeX.setLabel(" Temps (s) ");
        axeX.setForceZeroInRange(false);
        axeX.setAutoRanging(false);
        axeX.setTickUnit(10);

        NumberAxis axeY = new NumberAxis();
        axeY.setLabel("Vitesse (m/s) ");

        LineChart<Number, Number> graphVitesse = new LineChart<>(axeX, axeY);
        graphVitesse.setTitle("Évolution de vitesse");
        graphVitesse.setAnimated(false);
        graphVitesse.setCreateSymbols(false);

        XYChart.Series<Number, Number> serieVitesseX = new XYChart.Series<>();
        serieVitesseX.setName(" Vitesse X ");
        XYChart.Series<Number, Number> serieVitesseY = new XYChart.Series<>();
        serieVitesseY.setName(" Vitesse Y ");
        graphVitesse.getData().addAll(serieVitesseX, serieVitesseY);

        NumberAxis axeAccelX = new NumberAxis();
        axeAccelX.setLabel(" Temps (s) ");
        axeAccelX.setForceZeroInRange(false);
        axeAccelX.setAutoRanging(false);
        axeAccelX.setTickUnit(10);

        NumberAxis axeAccelY = new NumberAxis();
        axeAccelY.setLabel(" Accéleration (m/s²) ");

        LineChart<Number, Number> graphAcceleration = new LineChart<>(axeAccelX, axeAccelY);
        graphAcceleration.setTitle(" Évolution de l'accéleration ");
        graphAcceleration.setAnimated(false);
        graphAcceleration.setCreateSymbols(false);

        XYChart.Series<Number, Number> serieAccelX = new XYChart.Series<>();
        serieAccelX.setName(" Accéleration X ");
        XYChart.Series<Number, Number> serieAccelY = new XYChart.Series<>();
        serieAccelY.setName(" Accéleration Y ");
        graphAcceleration.getData().addAll(serieAccelX, serieAccelY);

        long[] tempsDebut = {System.nanoTime()};

        VBox boiteDonnees = new VBox(15);
        boiteDonnees.setPadding(new Insets(15, 0, 15, 0));
        boiteDonnees.getChildren().addAll(txtPos, txtVit, txtAcc, txtMasse);

        VBox boiteGraphs = new VBox(15);
        boiteGraphs.setPadding(new Insets(15, 0, 15, 0));
        boiteGraphs.getChildren().addAll(graphVitesse, graphAcceleration);
        boiteGraphs.setVisible(false);
        boiteGraphs.setManaged(false);

        StackPane stackPane = new StackPane(boiteDonnees, boiteGraphs);

        Button bDonnees = new Button(" Données ");
        Button bGraphs = new Button(" Graphiques ");

        String actif = "-fx-background-color: #444444; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20 8 20; -fx-background-radius: 5;";
        String nonactif = "-fx-background-color: transparent; -fx-text-fill: #888888; -fx-font-weight: bold; -fx-padding: 8 20 8 20; -fx-cursor: hand;";

        bDonnees.setStyle(actif);
        bGraphs.setStyle(nonactif);

        HBox contBoutons = new HBox(10, bDonnees, bGraphs);
        contBoutons.setStyle("-fx-background-color: #222222; -fx-padding: 5; -fx-background-radius: 8;");

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
        header.setPadding(new Insets(0, 0, 15, 0));

        AnimationTimer rafraichisseur = new AnimationTimer() {
            private long dernierTempsGraph = System.nanoTime();

            @Override
            public void handle(long now) {
                if (!simulation.getPlanetes().contains(p)) {
                    this.stop();
                    fenetreDetails.close();
                    fenetresOuvertes.remove(p);
                    return;
                }

                txtPos.setText(
                        "Position X : " + df.format(p.getPosition().getX()) + " unité" +
                                "\nPosition Y : " + df.format(p.getPosition().getY()) + " unité"
                );

                double vitesseAbsolue = Math.sqrt(
                        Math.pow(p.getVelocite().getX(), 2) + Math.pow(p.getVelocite().getY(), 2)
                );
                txtVit.setText(
                        "Vitesse X : " + df.format(p.getVelocite().getX()) + " unité" +
                                "\nVitesse Y : " + df.format(p.getVelocite().getY()) + " unité" +
                                "\nVitesse : " + df.format(vitesseAbsolue) + " unité"
                );

                double accelerationAbsolue = Math.sqrt(
                        Math.pow(p.getAcceleration().getX(), 2) + Math.pow(p.getAcceleration().getY(), 2)
                );
                txtAcc.setText(
                        "Accélération X : " + df.format(p.getAcceleration().getX()) + " unité" +
                                "\nAccélération Y : " + df.format(p.getAcceleration().getY()) + " unité" +
                                "\nAccélération : " + df.format(accelerationAbsolue) + " unité"
                );

                txtMasse.setText("Masse " + df.format(p.getMasse()) + " unité");

                if (!pause) {
                    if ((now - dernierTempsGraph) > 500000000) {
                        double tempsEcoule = ((now - tempsDebut[0]) * 1e-9);

                        double vitesseX = p.getVelocite().getX();
                        double vitesseY = p.getVelocite().getY();
                        double accelX = p.getAcceleration().getX();
                        double accelY = p.getAcceleration().getY();

                        serieVitesseX.getData().add(new XYChart.Data<>(tempsEcoule, vitesseX));
                        serieVitesseY.getData().add(new XYChart.Data<>(tempsEcoule, vitesseY));
                        serieAccelX.getData().add(new XYChart.Data<>(tempsEcoule, accelX));
                        serieAccelY.getData().add(new XYChart.Data<>(tempsEcoule, accelY));

                        double tailleFenetreGraph = 60;
                        axeX.setLowerBound(Math.max(0, tempsEcoule - tailleFenetreGraph));
                        axeX.setUpperBound(Math.max(tailleFenetreGraph, tempsEcoule));
                        axeAccelX.setLowerBound(Math.max(0, tempsEcoule - tailleFenetreGraph));
                        axeAccelX.setUpperBound(Math.max(tailleFenetreGraph, tempsEcoule));

                        if (serieVitesseX.getData().size() > 250) {
                            serieVitesseX.getData().removeFirst();
                            serieVitesseY.getData().removeFirst();
                        }

                        if (serieAccelX.getData().size() > 250) {
                            serieAccelX.getData().removeFirst();
                            serieAccelY.getData().removeFirst();
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
        layout.getChildren().addAll(header, contBoutons, stackPane);

        Scene scene = new Scene(layout, 500, 500);
        scene.getStylesheets().add(getClass().getResource("/styleGraphiques.css").toExternalForm());

        fenetreDetails.setResizable(true);
        fenetreDetails.setScene(scene);
        fenetreDetails.setAlwaysOnTop(true);
        fenetreDetails.show();
    }
}

//