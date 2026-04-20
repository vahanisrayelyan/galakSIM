package ca.qc.bdeb.sim.galak_sim;

import ca.qc.bdeb.sim.galak_sim.addons.Input;
import ca.qc.bdeb.sim.galak_sim.addons.Modeles;
import ca.qc.bdeb.sim.galak_sim.addons.Vecteurs;
import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import ca.qc.bdeb.sim.galak_sim.graphics.ChampEtoiles;
import ca.qc.bdeb.sim.galak_sim.graphics.FenetreDetails;
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
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class MainJavaFX extends Application {
    private static Simulation simulation;
    public static final double LARGEUR = 1200;
    public static final double HAUTEUR = 750;
    private FenetreDetails fenetreDetails;

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
    private Text texteTempsPasse;

    @Override
    public void start(Stage stage) {
        StackPane panneau = new StackPane();
        Scene scene = new Scene(panneau, LARGEUR, HAUTEUR);

        Input input = new Input();
        input.etatTouches(scene);

        Canvas canvas = new Canvas(LARGEUR, HAUTEUR);
        canvas.widthProperty().bind(panneau.widthProperty());
        canvas.heightProperty().bind(panneau.heightProperty());
        canvas.setCursor(Cursor.HAND);

        simulation = new Simulation(vecteurs);
        creerInterface(panneau, canvas);

        fenetreDetails = new FenetreDetails(simulation, fenetresOuvertes, new boolean[]{pause});

        GraphicsContext contexte = canvas.getGraphicsContext2D();

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
            private long dernierTempsPrediction = 0;

            @Override
            public void handle(long temps) {
                double deltaTemps = (temps - dernierTemps) * 1e-9 * vitesseSimulation;

                if (!pause) {
                    simulation.update(deltaTemps);
                    tempsSimulation += deltaTemps;

                    boolean nbPlaneteChangee = simulation.getPlanetes().size() != nbPlanetesAvant;
                    boolean tempsPasse = (temps - dernierTempsPrediction) > 2_000_000_000L;
                    // Après 2 secondes ou s'il y a une nouvelle planète, on fait une nouvelle simulation d'orbite futur
                    if (nbPlaneteChangee || tempsPasse) {
                        simulation.calculerPredictions();
                        dernierTempsPrediction = temps;
                    }
                }

                texteTempsPasse.setText(tempsPasse((long) tempsSimulation));

                if (simulation.getPlanetes().size() != nbPlanetesAvant) {
                    rafraichirListePlanetes(listePlaneteUI, canvasPrincipal);
                    nbPlanetesAvant = simulation.getPlanetes().size();
                }

                simulation.draw(contexte);
                dernierTemps = temps;
            }
        };
        timer.start();

        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setMinWidth(1200);
        stage.setMinHeight(700);
        stage.getIcons().add(new Image("logoSansFond.png"));
        stage.setTitle("GalakSIM");
        stage.setScene(scene);
        stage.show();
    }

    private String tempsPasse(long totalSecondes) {
        long annees = totalSecondes / (365 * 24 * 3600);
        long jours = (totalSecondes % (365 * 24 * 3600)) / (24 * 3600);
        long heures = (totalSecondes % (24 * 3600)) / 3600;
        long minutes = (totalSecondes % 3600) / 60;
        long secondes = totalSecondes % 60;
        return String.format("Temps : %d an(s) %d j %02dh %02dm %02ds", annees, jours, heures, minutes, secondes);
    }

    private void creerInterface(StackPane panneau, Canvas canvas) {
        VBox boiteParametres = new VBox(10);
        boiteParametres.setMaxWidth(Double.MAX_VALUE);
        boiteParametres.setVisible(true);
        boiteParametres.setManaged(true);

        VBox boiteModeles = new VBox(10);
        boiteModeles.setMaxWidth(Double.MAX_VALUE);
        boiteModeles.setVisible(false);
        boiteModeles.setManaged(false);

        Text texteNom = new Text("Nom");
        texteNom.setFill(Color.WHITE);
        TextField saisiNom = new TextField();
        saisiNom.setTextFormatter(formateurAlphabetique());

        Text texteVitesseX = new Text("Vitesse en x");
        texteVitesseX.setFill(Color.WHITE);
        HBox hboxVitesseX = new HBox(10);
        TextField saisiVitesseX = new TextField("0");
        saisiVitesseX.setTextFormatter(formateurNumerique(true));
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
        saisiVitesseY.setTextFormatter(formateurNumerique(true));
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
        saisiMasse.setTextFormatter(formateurNumerique(false));
        HBox.setHgrow(saisiMasse, Priority.ALWAYS);
        saisiMasse.setMaxWidth(Double.MAX_VALUE);
        Text uniteMasse = new Text("×10^24 kg");
        uniteMasse.setFill(Color.WHITE);
        hboxMasse.setAlignment(Pos.CENTER_LEFT);
        hboxMasse.getChildren().addAll(saisiMasse, uniteMasse);

        VBox listePlanete = new VBox(5);
        this.listePlaneteUI = listePlanete;
        this.canvasPrincipal = canvas;
        texteTempsPasse = new Text("Temps : 0.0 s");
        texteTempsPasse.setFill(Color.WHITE);
        texteTempsPasse.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        canvas.setOnMouseClicked(e ->
                ajouterPlanete(e, canvas, saisiVitesseX, saisiVitesseY, saisiMasse, saisiNom, listePlanete)
        );

        Text texteInformations = new Text("Cliquez gauche pour ajouter une planète\nMolette pour zoomer\nClic droit pour déplacer la vue");
        texteInformations.setFill(Color.WHITE);

        Button btnResetVue = new Button("Réinitialiser la vue");
        btnResetVue.setOnAction(e -> simulation.reinitialiserVue(null));

        VBox choixVecteursVBox = new VBox(2);
        RadioButton choixPasVecteurs = new RadioButton("Aucun");
        choixPasVecteurs.setOnAction(e -> vecteurs.setChoix(0));
        choixPasVecteurs.setSelected(true);
        RadioButton choixVecteurVitesse = new RadioButton("Vitesse");
        choixVecteurVitesse.setOnAction(e -> vecteurs.setChoix(1));
        RadioButton choixVecteurAcceleration = new RadioButton("Acceleration");
        choixVecteurAcceleration.setOnAction(e -> vecteurs.setChoix(2));
        RadioButton choixVecteurForce = new RadioButton("Force");
        choixVecteurForce.setOnAction(e -> vecteurs.setChoix(3));

        ToggleGroup choixVecteursToggleGroup = new ToggleGroup();
        choixPasVecteurs.setToggleGroup(choixVecteursToggleGroup);
        choixVecteurVitesse.setToggleGroup(choixVecteursToggleGroup);
        choixVecteurAcceleration.setToggleGroup(choixVecteursToggleGroup);
        choixVecteurForce.setToggleGroup(choixVecteursToggleGroup);

        choixVecteursVBox.getChildren().addAll(
                choixPasVecteurs, choixVecteurVitesse, choixVecteurAcceleration, choixVecteurForce
        );

        CheckBox choixPrediction = new CheckBox("Prédiction");
        choixPrediction.setSelected(false);
        choixPrediction.setOnAction(e -> simulation.setAfficherPrediction(choixPrediction.isSelected()));

        ScrollPane defileurPlanetes = new ScrollPane(listePlanete);
        defileurPlanetes.setFitToWidth(true);
        defileurPlanetes.setMaxHeight(150);
        defileurPlanetes.setMinHeight(150);
        defileurPlanetes.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        defileurPlanetes.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        Text vitesseTexte = new Text("Vitesse de la simulation: " + vitesseSimulation);
        vitesseTexte.setFill(Color.WHITE);
        vitesseTexte.setWrappingWidth(210);

        HBox modificationTemps = new HBox(10);

        Button btnPause = new Button("⏸");
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
        double[] paliers = {
                1,           // 1 seconde/s
                60,          // 1 minute/s
                3600,        // 1 heure/s
                86400,       // 1 jour/s
                604800,      // 1 semaine/s
                2592000,     // 1 mois/s
                31536000,    // 1 an/s
                315360000    // 10 ans/s (limite)
        };

        String[] nomspaliers = {
                "×1 s/s",
                "×1 min/s",
                "×1 h/s",
                "×1 jour/s",
                "×1 semaine/s",
                "×1 mois/s",
                "×1 an/s",
                "×10 ans/s"
        };

        int[] indexPalier = {0};
        vitesseSimulation = paliers[0];
        vitesseTexte.setText("Vitesse de la simulation: " + nomspaliers[0]);

        btnPlusVite.setOnAction(e -> {
            if (indexPalier[0] < paliers.length - 1) {
                indexPalier[0]++;
                vitesseSimulation = paliers[indexPalier[0]];
                vitesseTexte.setText("Vitesse de la simulation: " + nomspaliers[indexPalier[0]]);
            }
        });

        btnMoinsVite.setOnAction(e -> {
            if (indexPalier[0] > 0) {
                indexPalier[0]--;
                vitesseSimulation = paliers[indexPalier[0]];
                vitesseTexte.setText("Vitesse de la simulation: " + nomspaliers[indexPalier[0]]);
            }
        });

        Button btnTempsZero = new Button("Temps à 0");
        btnTempsZero.setOnAction(e -> {
            tempsSimulation = 0;
        });

        modificationTemps.getChildren().addAll(btnMoinsVite, btnPause, btnPlusVite, btnTempsZero);

        boiteParametres.getChildren().addAll(
                creerSection("Ajouter une planète", texteNom, saisiNom, texteVitesseX, hboxVitesseX, texteVitesseY, hboxVitesseY, texteMasse, hboxMasse),
                texteInformations,
                btnResetVue,
                creerSection("Affichage", choixVecteursVBox, choixPrediction),
                creerSection("Planètes", defileurPlanetes),
                creerSection("Temps", vitesseTexte, modificationTemps)
        );

        Text titreModeles = new Text("Modeles");
        titreModeles.setFill(Color.WHITE);

        Button btnSysteme = new Button("Système solaire");
        btnSysteme.setOnAction(e -> {
            Modeles.chargerSystemeSolaire(simulation);
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
            Modeles.chargerCollision(simulation);
            rafraichirListePlanetes(listePlanete, canvas);
            nbPlanetesAvant = simulation.getPlanetes().size();
        });
        Button btnBinaire = new Button("Orbite binaire");
        btnBinaire.setOnAction(e -> {
            Modeles.chargerPlanetesBinaires(simulation);
            rafraichirListePlanetes(listePlanete, canvas);
            nbPlanetesAvant = simulation.getPlanetes().size();
        });
        Button btnTerreLune = new Button("Terre - Lune");
        btnTerreLune.setOnAction(e -> {
            Modeles.chargerTerreLune(simulation);
            rafraichirListePlanetes(listePlanete, canvas);
            nbPlanetesAvant = simulation.getPlanetes().size();
        });

        boiteModeles.getChildren().addAll(titreModeles, btnSysteme, btnVide, btnCollision);

        VBox sectionsMenu = new VBox(boiteParametres, boiteModeles);
        sectionsMenu.setFillWidth(true);
        sectionsMenu.setMaxWidth(Double.MAX_VALUE);
        sectionsMenu.setPrefHeight(canvas.getHeight());
        VBox.setVgrow(sectionsMenu, Priority.ALWAYS);

        Button btnParametres = new Button(" Paramètres ");
        Button btnModeles = new Button(" Modèles ");

        String actif = "-fx-background-color: #444444; -fx-text-fill: white; -fx-font-weight: bold;";
        String nonactif = "-fx-background-color: transparent; -fx-text-fill: #888888;";

        btnParametres.setStyle(actif);
        btnModeles.setStyle(nonactif);

        btnParametres.setOnAction(e -> {
            boiteParametres.setVisible(true);
            boiteParametres.setManaged(true);
            boiteModeles.setVisible(false);
            boiteModeles.setManaged(false);
            btnParametres.setStyle(actif);
            btnModeles.setStyle(nonactif);
        });

        btnModeles.setOnAction(e -> {
            boiteParametres.setVisible(false);
            boiteParametres.setManaged(false);
            boiteModeles.setVisible(true);
            boiteModeles.setManaged(true);
            btnParametres.setStyle(nonactif);
            btnModeles.setStyle(actif);
        });

        HBox choixSections = new HBox(10, btnParametres, btnModeles);
        choixSections.setStyle("-fx-background-color: #222222; -fx-padding: 5; -fx-background-radius: 8;");

        VBox hautMenu = new VBox(10);
        hautMenu.setPadding(new Insets(15));
        hautMenu.setStyle("-fx-background-color: rgba(30, 30, 30, 0.75);");
        hautMenu.getChildren().add(choixSections);

        // Contenu défilable
        VBox contenuMenu = new VBox(15);
        contenuMenu.setFillWidth(true);
        contenuMenu.setMaxWidth(Double.MAX_VALUE);
        contenuMenu.setPadding(new Insets(15));
        contenuMenu.setStyle("-fx-background-color: rgba(30, 30, 30, 0.75);");
        contenuMenu.getChildren().add(sectionsMenu);

        ScrollPane scrollContenu = new ScrollPane(contenuMenu);
        scrollContenu.setFitToWidth(true);
        scrollContenu.setFitToHeight(true);
        scrollContenu.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollContenu.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollContenu.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scrollContenu, Priority.ALWAYS);

        // Menu complet = haut fixe + contenu défilable
        BorderPane menuComplet = new BorderPane();
        menuComplet.setTop(hautMenu);
        menuComplet.setCenter(scrollContenu);
        menuComplet.setMaxWidth(250);
        menuComplet.setMinWidth(250);
        menuComplet.setMaxHeight(Double.MAX_VALUE);
        menuComplet.setStyle("-fx-background-color: rgba(30, 30, 30, 0.75); -fx-border-color: #444; -fx-border-width: 0 0 0 2;");
        menuComplet.prefHeightProperty().bind(panneau.heightProperty());

        Button btnMenu = new Button("☰");
        btnMenu.setOnAction(e -> {
            boolean menuVisible = menuComplet.isVisible();
            menuComplet.setVisible(!menuVisible);
            StackPane.setMargin(btnMenu, new Insets(10, menuVisible ? 10 : 260, 0, 0));
        });

        Pane espace = new Pane();
        espace.prefWidthProperty().bind(canvas.widthProperty());
        espace.prefHeightProperty().bind(canvas.heightProperty());

        ChampEtoiles champEtoiles = new ChampEtoiles(espace);
        champEtoiles.demarrer();

        StackPane centre = new StackPane(espace, canvas);

        StackPane.setAlignment(menuComplet, Pos.TOP_RIGHT);
        StackPane.setAlignment(btnMenu, Pos.TOP_RIGHT);
        StackPane.setMargin(btnMenu, new Insets(10, 260, 0, 0));

        StackPane.setAlignment(texteTempsPasse, Pos.TOP_LEFT);
        StackPane.setMargin(texteTempsPasse, new Insets(10));

        nbPlanetesAvant = simulation.getPlanetes().size();

        panneau.getChildren().addAll(centre, menuComplet, btnMenu, texteTempsPasse);
    }

    public static TextFormatter<String> formateurNumerique(boolean accepterNegatif) {
        String regex = accepterNegatif
                ? "^-?$|^-?(0|[1-9]\\d*)([.,]\\d*)?$"
                : "^$|^(0|[1-9]\\d*)([.,]\\d*)?$";

        return new TextFormatter<>(change ->
                change.getControlNewText().matches(regex) ? change : null
        );
    }

    public static TextFormatter<String> formateurAlphabetique() {
        return new TextFormatter<>(change -> {
            if (change.getControlNewText().length() <= 20) {
                return change;
            }
            return null;
        });
    }

    private void ajouterPlanete(MouseEvent e, Canvas canvas, TextField saisiVitesseX, TextField saisiVitesseY, TextField saisiMasse, TextField saisiNom, VBox listePlanete) {
        if (e.getButton() != MouseButton.PRIMARY) {
            return;
        }

        Point2D monde = simulation.ecranVersMonde(e.getX(), e.getY(), canvas.getWidth(), canvas.getHeight());
        double x = monde.getX();
        double y = monde.getY();

        double vX = saisiVitesseX.getText().isEmpty() || saisiVitesseX.getText().equals("-")
                ? 0
                : Double.parseDouble(saisiVitesseX.getText().replace(",", "."));

        double vY = saisiVitesseY.getText().isEmpty() || saisiVitesseY.getText().equals("-")
                ? 0
                : Double.parseDouble(saisiVitesseY.getText().replace(",", "."));

        double masse = saisiMasse.getText().isEmpty()
                ? 0
                : Double.parseDouble(saisiMasse.getText().replace(",", ".")) * 10e24;

        double taille = 6.0e6;

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
            Image image = null;
            Color color = null;
            String nomPlanete = saisiNom.getText().isEmpty()
                    ? "Planète " + (simulation.getSizeListPlanetes() + 1)
                    : saisiNom.getText();

            simulation.ajouterNouvellePlanete(x, y, vX, vY, taille, masse, nomPlanete,image,color);
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

    private VBox creerSection(String titre, javafx.scene.Node... contenu) {
        VBox section = new VBox(5);

        Button btnTitre = new Button("▾ " + titre);
        btnTitre.setMaxWidth(Double.MAX_VALUE);
        btnTitre.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: CENTER-LEFT;");

        VBox corps = new VBox(8);
        corps.getChildren().addAll(contenu);
        corps.setPadding(new Insets(5, 0, 5, 5));

        btnTitre.setOnAction(e -> {
            boolean visible = corps.isVisible();
            corps.setVisible(!visible);
            corps.setManaged(!visible);
            btnTitre.setText((visible ? "▸ " : "▾ ") + titre);
        });

        section.getChildren().addAll(btnTitre, corps);
        return section;
    }

    private void ouvrirFenetreDetails(Planete p, Canvas canvas) {
        fenetreDetails.ouvrir(p, canvas);
    }
}