package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.Favoris;
import services.FavorisService;

import java.time.LocalDate;
import javafx.stage.Stage;

public class modifierfavoriscontroller {

    @FXML
    private TextField idProduitField;

    @FXML
    private TextField idUserField;

    @FXML
    private DatePicker datePicker;

    private Favoris favoris;
    private final FavorisService favorisService = new FavorisService();

    public void initData(Favoris favori) {
        this.favoris = favori;
        idProduitField.setText(String.valueOf(favori.getIdProduit()));
        idUserField.setText(String.valueOf(favori.getIdUser()));
        datePicker.setValue(favori.getDate());
    }

    @FXML
    private void saveChanges() {
        try {
            favoris.setIdProduit(Integer.parseInt(idProduitField.getText()));
            favoris.setIdUser(Integer.parseInt(idUserField.getText()));
            favoris.setDate(datePicker.getValue());

            favorisService.modifierFavoris(favoris);
            System.out.println("Favori modifié avec succès !");

            // Fermer la fenêtre actuelle après modification
            ((Stage) idProduitField.getScene().getWindow()).close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la modification : " + e.getMessage());
        }
    }

}
