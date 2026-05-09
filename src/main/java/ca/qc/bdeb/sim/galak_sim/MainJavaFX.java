package ca.qc.bdeb.sim.galak_sim;

import ca.qc.bdeb.sim.galak_sim.addons.Input;
import ca.qc.bdeb.sim.galak_sim.addons.Vecteurs;
import ca.qc.bdeb.sim.galak_sim.graphics.Affichage;
import ca.qc.bdeb.sim.galak_sim.graphics.Simulation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainJavaFX extends Application {
    private static Simulation simulation;
    public static final double LARGEUR = 1200;
    public static final double HAUTEUR = 750;
    private Affichage affichage;
    private double dernierX;
    private double dernierY;
    private boolean cameraEnDeplacement = false;
    private VBox listePlaneteUI;
    private int nbPlanetesAvant = 0;
    private final Vecteurs vecteurs = new Vecteurs();
    private Text texteTempsPasse;

    @Override
    public void start(Stage stage) {
        StackPane panneau = new StackPane();
        Scene scene = new Scene(panneau, LARGEUR, HAUTEUR);

        simulation = new Simulation(vecteurs);
        affichage = new Affichage(simulation);

        Input input = new Input();
        input.etatTouches(scene);

        Canvas canvas = new Canvas(LARGEUR, HAUTEUR);
        canvas.widthProperty().bind(panneau.widthProperty());
        canvas.heightProperty().bind(panneau.heightProperty());
        canvas.setCursor(Cursor.HAND);

        affichage.creerInterface(panneau, canvas);
        this.texteTempsPasse = affichage.getTexteTempsPasse();
        this.listePlaneteUI = affichage.getListePlanete();

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

        stage.setOnCloseRequest(e -> affichage.fermerToutesFenetres());

        nbPlanetesAvant = simulation.getPlanetes().size();

        AnimationTimer timer = new AnimationTimer() {
            private long dernierTemps = System.nanoTime();
            private long dernierTempsPrediction = 0;

            @Override
            public void handle(long temps) {
                double deltaTemps = (temps - dernierTemps) * 1e-9 * simulation.getVitesseSimulation();

                if (!simulation.isEnPause()) {
                    simulation.update(deltaTemps);

                    boolean nbPlaneteChangee = simulation.getPlanetes().size() != nbPlanetesAvant;
                    boolean tempsPasse = (temps - dernierTempsPrediction) > 2_000_000_000L;
                    // Après 2 secondes ou s'il y a une nouvelle planète, on fait une nouvelle simulation d'orbite future
                    if (nbPlaneteChangee || tempsPasse) {
                        simulation.calculerPredictions();
                        dernierTempsPrediction = temps;
                    }
                }

                texteTempsPasse.setText(tempsPasse((long) simulation.getTempsAccumule()));

                if (simulation.getPlanetes().size() != nbPlanetesAvant) {
                    affichage.rafraichirListePlanetes(listePlaneteUI, affichage.getCanvasPrincipal());
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
}