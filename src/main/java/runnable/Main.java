package runnable;

import backend.Methods.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static Lagrange lagrange = new Lagrange();
    public static NewtonPolynomialWithNotEquidistantPoints newtonPolynomialWithNotEquidistantPoints = new NewtonPolynomialWithNotEquidistantPoints();
    public static NewtonPolynomialWithEquidistantPointsForwards newtonPolynomialWithEquidistantPointsForwards = new NewtonPolynomialWithEquidistantPointsForwards();
    public static NewtonPolynomialWithEquidistantPointsBackwards newtonPolynomialWithEquidistantPointsBackwards = new NewtonPolynomialWithEquidistantPointsBackwards();


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(runnable.Main.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("application");

        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
