package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Reclamation;
import models.User;
import services.ReclamationService;
import services.UserService;

import java.io.IOException;
import java.util.List;

public class DashRecUserController {

    public Button add;
    @FXML
    private ListView<Reclamation> RecListView;

    @FXML
    private Button addbtn;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private PieChart pieChart;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Reclamation> RecTreatedListView;

    @FXML
    private ComboBox<String> filter1;

    @FXML
    private ComboBox<String> filter2;

    private ReclamationService reclamationService = new ReclamationService();
    private ObservableList<Reclamation> reclamationList = FXCollections.observableArrayList();
    private ObservableList<Reclamation> treatedReclamationList = FXCollections.observableArrayList();

    private UserService us = new UserService();
    private final User logged_in_User = us.recupererParId(2);

    @FXML
    public void initialize() {
        loadReclamations();
        setupListViewCellFactory();
        setupSearch();
        updateStats();
    }

    private void loadReclamations() {
        List<Reclamation> reclamations = reclamationService.getByUserId(logged_in_User.getId_user());
        //System.out.println(reclamations);
        reclamationList.clear();
        treatedReclamationList.clear();

        for (Reclamation rec : reclamations) {
            if (rec.getAnswer() == null || rec.getAnswer().isEmpty()) {
                reclamationList.add(rec);
                System.out.println(rec);
            } else {
                treatedReclamationList.add(rec);
                System.out.println(rec);
            }
        }

        RecListView.setItems(reclamationList);
        RecTreatedListView.setItems(treatedReclamationList);
    }

    private void setupListViewCellFactory() {
        Callback<ListView<Reclamation>, ListCell<Reclamation>> cellFactory = listView -> new ListCell<>() {
            @Override
            protected void updateItem(Reclamation reclamation, boolean empty) {
                super.updateItem(reclamation, empty);
                if (empty || reclamation == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/RecCell.fxml"));
                    try {
                        setGraphic(loader.load());
                        RecCellController controller = loader.getController();
                        controller.setReclamation(reclamation, listView);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        RecListView.setCellFactory(cellFactory);
        RecTreatedListView.setCellFactory(cellFactory);
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchText = newValue.toLowerCase();
            ObservableList<Reclamation> filteredList = FXCollections.observableArrayList();
            for (Reclamation rec : reclamationList) {
                if (rec.getTitre().toLowerCase().contains(searchText)) {
                    filteredList.add(rec);
                }
            }
            RecListView.setItems(filteredList);
        });
    }

    private void updateStats() {
        updatePieChart();
        updateBarChart();
    }

    private void updatePieChart() {
        pieChart.getData().clear();

        int basse = 0, moyenne = 0, haute = 0;
        for (Reclamation rec : reclamationList) {
            switch (rec.getPriorite()) {
                case "Basse": basse++; break;
                case "Moyenne": moyenne++; break;
                case "Haute": haute++; break;
            }
        }

        if (basse > 0) pieChart.getData().add(new PieChart.Data("Basse", basse));
        if (moyenne > 0) pieChart.getData().add(new PieChart.Data("Moyenne", moyenne));
        if (haute > 0) pieChart.getData().add(new PieChart.Data("Haute", haute));
    }

    private void updateBarChart() {
        barChart.getData().clear();

        int enAttente = 0, traitee = 0;
        for (Reclamation rec : reclamationList) {
            if (rec.getAnswer() == null || rec.getAnswer().isEmpty()) {
                enAttente++;
            } else {
                traitee++;
            }
        }

        barChart.getData().add(new BarChart.Series<>("Statut", FXCollections.observableArrayList(
                new BarChart.Data<>("En attente", enAttente),
                new BarChart.Data<>("Traitée", traitee)
        )));
    }

    @FXML
    public void add(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddRec.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ajouter une Réclamation");
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();
            refreshReclamationList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshReclamationList() {
        loadReclamations();
        updateStats();
    }
}
