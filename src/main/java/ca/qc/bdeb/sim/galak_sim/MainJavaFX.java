package ca.qc.bdeb.sim.galak_sim;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainJavaFX extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 1200, 700);
        var imageExo = new Image("TrainExoRobuste.jpg");
        var vueImageExo = new ImageView(imageExo);
        vueImageExo.setX(450);
        vueImageExo.setY(275);
        borderPane.getChildren().add(vueImageExo);

        stage.setTitle("GalakSIM");
        stage.setScene(scene);
        stage.show();
    }
}
