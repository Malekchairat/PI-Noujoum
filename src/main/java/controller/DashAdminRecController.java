package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Reclamation;
import services.ReclamationService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DashAdminRecController {

    @FXML
    private ListView<Reclamation> RecListView;

    @FXML
    private ListView<Reclamation> RecTreatedListView;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private Button feedback;





    @FXML
    private PieChart pieChart;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchField;

    @FXML
    private Button stats;

    private ReclamationService reclamationService = new ReclamationService();
    private ObservableList<Reclamation> untreatedRecs;
    private ObservableList<Reclamation> treatedRecs;

    @FXML
    public void initialize() {
        setupListViews();
        setupSearch();
        loadStats();
    }

    private void loadStats() {
        // Pie Chart
        long untreatedCount = untreatedRecs.size();
        long treatedCount = treatedRecs.size();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Untreated", untreatedCount),
                new PieChart.Data("Treated", treatedCount)
        );
        pieChart.setData(pieChartData);

        // Bar Chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Reclamation Status");
        series.getData().add(new XYChart.Data<>("Untreated", untreatedCount));
        series.getData().add(new XYChart.Data<>("Treated", treatedCount));
        barChart.getData().add(series);
    }
    private void setupListViews() {
        refreshListView();
        // Set custom cell factory for ListView to use RecCellAdmin.fxml
        RecListView.setCellFactory(param -> new ListCell<Reclamation>() {
            @Override
            protected void updateItem(Reclamation rec, boolean empty) {
                super.updateItem(rec, empty);
                if (empty || rec == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/RecCellAdmin.fxml"));
                        Parent root = loader.load();
                        RecCellAdminController controller = loader.getController();
                        controller.setRec(rec, DashAdminRecController.this);
                        setGraphic(root);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        RecTreatedListView.setCellFactory(param -> new ListCell<Reclamation>() {
            @Override
            protected void updateItem(Reclamation rec, boolean empty) {
                super.updateItem(rec, empty);
                if (empty || rec == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/RecCellAdmin.fxml"));
                        Parent root = loader.load();
                        RecCellAdminController controller = loader.getController();
                        controller.setRec(rec, DashAdminRecController.this);
                        setGraphic(root);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void refreshListView() {
        List<Reclamation> allRecs = reclamationService.recuperer();

        untreatedRecs = FXCollections.observableArrayList(
                allRecs.stream().filter(rec -> rec.getStatut().equals("Traitée") ).collect(Collectors.toList())
        );

        treatedRecs = FXCollections.observableArrayList(
                allRecs.stream().filter(rec -> rec.getStatut().equals("En attente")).collect(Collectors.toList())
        );

        RecListView.setItems(untreatedRecs);
        RecTreatedListView.setItems(treatedRecs);
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterReclamations(newValue);
        });
    }

    private void filterReclamations(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            RecListView.setItems(untreatedRecs);
            RecTreatedListView.setItems(treatedRecs);
            return;
        }

        ObservableList<Reclamation> filteredUntreated = untreatedRecs.filtered(rec ->
                rec.getTitre().toLowerCase().contains(keyword.toLowerCase()) ||
                        rec.getDescription().toLowerCase().contains(keyword.toLowerCase()));

        ObservableList<Reclamation> filteredTreated = treatedRecs.filtered(rec ->
                rec.getTitre().toLowerCase().contains(keyword.toLowerCase()) ||
                        rec.getDescription().toLowerCase().contains(keyword.toLowerCase()));

        RecListView.setItems(filteredUntreated);
        RecTreatedListView.setItems(filteredTreated);
    }





    @FXML
    void openFeedback(ActionEvent event) {
        openWindow("/DashFeedBack.fxml", "Feedback Management");
    }

    @FXML
    void openStat(ActionEvent event) {
        openWindow("/StatsAdmin.fxml", "Admin Statistics");
    }

    private void openWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
