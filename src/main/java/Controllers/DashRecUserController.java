package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Reclamation;
import models.User;
import services.ReclamationService;
import services.UserService;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    @FXML
    private Button feedback;

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
        setupFilters(); // Ajout de cette méthode pour remplir les filtres

    }

    private void setupFilters() {
        // Peupler le filtre des priorités
        filter2.setItems(FXCollections.observableArrayList("All","Basse", "Moyenne", "Haute"));

        // Peupler le filtre des dates
        filter1.setItems(FXCollections.observableArrayList("All","Today", "One week", "One month"));

        // Ajouter les événements pour synchronisation des filtres
        filter1.setOnAction(event -> applyFilters());
        filter2.setOnAction(event -> applyFilters());
    }






    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase();
        String selectedDate = filter1.getValue();
        String selectedPriority = filter2.getValue();

        LocalDate today = LocalDate.now();
        LocalDate oneWeekAgo = today.minus(7, ChronoUnit.DAYS);
        LocalDate oneMonthAgo = today.minus(30, ChronoUnit.DAYS);

        ObservableList<Reclamation> filteredList = FXCollections.observableArrayList();

        for (Reclamation rec : reclamationList) {
            boolean matchesSearch = rec.getTitre().toLowerCase().contains(searchText);

            // Vérification de la priorité (si "All", on accepte toutes les priorités)
            boolean matchesPriority = (selectedPriority == null || selectedPriority.equals("All") ||
                    rec.getPriorite().equalsIgnoreCase(selectedPriority));

            // Conversion Date -> LocalDate
            Date recDateSql = (Date) rec.getDateCreation();
            LocalDate recDate = (recDateSql != null) ? recDateSql.toLocalDate() : null;

            // Vérification de la date (si "All", on accepte toutes les dates)
            boolean matchesDate = true;
            if (recDate != null && selectedDate != null && !selectedDate.equals("All")) {
                switch (selectedDate) {
                    case "Today":
                        matchesDate = recDate.isEqual(today);
                        break;
                    case "One week":
                        matchesDate = recDate.isAfter(oneWeekAgo.minusDays(1)); // Inclut toute la semaine
                        break;
                    case "One month":
                        matchesDate = recDate.isAfter(oneMonthAgo.minusDays(1)); // Inclut tout le mois
                        break;
                }
            }

            if (matchesSearch && matchesPriority && matchesDate) {
                filteredList.add(rec);
            }
        }

        RecListView.setItems(filteredList);
    }



    @FXML
    private void filterByDate(ActionEvent event) {
        applyFilters();
    }

    @FXML
    private void filterByPriority(ActionEvent event) {
        applyFilters();
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

    @FXML
    void openFeedback(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FeedbackDashboard.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Mes FeedBacks");
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();
            refreshReclamationList();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //TODO add the search function here
    //TODO add the filter 1 for the date
    //TODO add the filter 2 for the priorite basse , haute , moyenne
    //TODO make them synchronized
}
