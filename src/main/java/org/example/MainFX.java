package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashRecUser.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashAdminRec.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setTitle("Tableau de Bord Utilisateur - Réclamations");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
