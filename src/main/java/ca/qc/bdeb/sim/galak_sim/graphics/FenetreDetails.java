package ca.qc.bdeb.sim.galak_sim.graphics;

import ca.qc.bdeb.sim.galak_sim.astres.Planete;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.Map;

public class FenetreDetails {

    private final Simulation simulation;
    private final Map<Planete, Stage> fenetresOuvertes;
    private final boolean[] pause;

    public FenetreDetails(Simulation simulation, Map<Planete, Stage> fenetresOuvertes, boolean[] pause) {
        this.simulation = simulation;
        this.fenetresOuvertes = fenetresOuvertes;
        this.pause = pause;
    }

    public void ouvrir(Planete p, Canvas canvas) {
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

        Text txtPos = new Text();
        txtPos.setFill(Color.WHITE);

        Text txtVit = new Text();
        txtVit.setFill(Color.WHITE);

        Text txtAcc = new Text();
        txtAcc.setFill(Color.WHITE);

        Text txtMasse = new Text();
        txtMasse.setFill(Color.WHITE);

        DecimalFormat df = new DecimalFormat("#.####");

        // Graphique vitesse
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

        // Graphique accélération
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

        String actif = "-fx-background-color: #444444; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20 8 20; -fx-background-radius: 5;";
        String nonactif = "-fx-background-color: transparent; -fx-text-fill: #888888; -fx-font-weight: bold; -fx-padding: 8 20 8 20; -fx-cursor: hand;";

        Button bDonnees = new Button(" Données ");
        Button bGraphs = new Button(" Graphiques ");
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

        Text titre = new Text("DONNÉES TÉLÉMÉTRIQUES");
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

                if (!pause[0]) {
                    if ((now - dernierTempsGraph) > 500_000_000) {
                        double tempsEcoule = (now - tempsDebut[0]) * 1e-9;

                        serieVitesseX.getData().add(new XYChart.Data<>(tempsEcoule, p.getVelocite().getX()));
                        serieVitesseY.getData().add(new XYChart.Data<>(tempsEcoule, p.getVelocite().getY()));
                        serieAccelX.getData().add(new XYChart.Data<>(tempsEcoule, p.getAcceleration().getX()));
                        serieAccelY.getData().add(new XYChart.Data<>(tempsEcoule, p.getAcceleration().getY()));

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
                    tempsDebut[0] += tempsEnPause;
                    dernierTempsGraph = now;
                }
            }
        };
        rafraichisseur.start();

        layout.getChildren().addAll(header, contBoutons, stackPane);

        Scene scene = new Scene(layout, 500, 500);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        fenetreDetails.setResizable(true);
        fenetreDetails.setScene(scene);
        fenetreDetails.setAlwaysOnTop(true);
        fenetreDetails.show();
    }
}