package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> origin/GestionCommande
public class MainFx extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
<<<<<<< HEAD
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        primaryStage.setTitle("login");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }
=======
import java.io.IOException;

public class MainFx extends Application {
>>>>>>> origin/integration-branch
=======
        Parent root = FXMLLoader.load(getClass().getResource("/acceuil.fxml"));
        primaryStage.setTitle("Accueil");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }
>>>>>>> origin/GestionCommande

    public static void main(String[] args) {
        launch(args);
    }
<<<<<<< HEAD
<<<<<<< HEAD
}
=======

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouteruser.fxml"));
            Parent root = loader.load();
            Scene sc = new Scene(root);
            stage.setTitle("Ajouter");
            stage.setScene(sc);
            stage.show();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }


    }
}
>>>>>>> origin/integration-branch
=======
}
>>>>>>> origin/GestionCommande
