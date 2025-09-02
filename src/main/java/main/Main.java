package main;

import models.User;
import models.Favoris;
import services.UserService;
import services.FavorisService;
import tools.MyDataBase;

import java.sql.SQLException;
import java.time.LocalDate;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        Connection con = MyDataBase.getInstance().getCnx();
        UserService uc = new UserService();
        User newUser = new User();
        newUser.setNom("Troudi");
        newUser.setPrenom("Jihen");
        newUser.setEmail("jihentroudi@example.com");
        newUser.setMdp("password123");
        newUser.setTel(12345678);

        // Charger l'image comme un Blob
        File imageFile = new File("profile.jpg");
        FileInputStream fis = new FileInputStream(imageFile);
        Blob imageBlob = new javax.sql.rowset.serial.SerialBlob(fis.readAllBytes());
        newUser.setImage(imageBlob);

        newUser.setRole("admin");

        // Création de l'utilisateur et ajout à la base de données
        uc.ajouter(newUser);

        FavorisService ccr = new FavorisService();
        Favoris c2 = new Favoris(21,2,11, LocalDate.of(2002, 4, 5));

        // Tester les autres opérations (ajout, suppression, modification de favoris)
        // ccr.ajouterFavoris(c2);
        // ccr.supprimerFavoris(14);
        // ccr.modifierFavoris(c2);
    }
}
