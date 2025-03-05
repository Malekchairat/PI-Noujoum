package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import models.Feedback;
import services.FeedbackService;

import java.util.List;
import java.util.stream.Collectors;

public class DashFeedBackController {

    @FXML private ListView<Feedback> FBListView;
    @FXML private BarChart<String, Number> barChart;
    @FXML private PieChart pieChart;
    @FXML private ComboBox<String> dateFilter;
    @FXML private ComboBox<String> filter;
    @FXML private TextField searchField;


    private final FeedbackService feedbackService = new FeedbackService();
    private ObservableList<Feedback> feedbackList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadFeedbackList();
        setupSearch();
        setupFilters();


    }

    private void loadFeedbackList() {
        feedbackList.setAll(feedbackService.recuperer());
        FBListView.setItems(feedbackList);
        FBListView.setCellFactory(param -> new ListCell<Feedback>() {
            @Override
            protected void updateItem(Feedback feedback, boolean empty) {
                super.updateItem(feedback, empty);
                if (empty || feedback == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(feedback.getRec().getTitre() + " - " + feedback.getNote() + "  Stars" +"  The User comment : "+feedback.getCommentaire() +"  "+feedback.getDateFeedback());
                    Button deleteButton = new Button("Delete");
                    deleteButton.setOnAction(event -> deleteFeedback(feedback));
                    setGraphic(deleteButton);
                }
            }
        });
        updateStats();
    }

    private void deleteFeedback(Feedback feedback) {
        feedbackService.supprimer(feedback.getId());
        feedbackList.remove(feedback);
        updateStats();
    }

    private void deleteSelectedFeedback() {
        Feedback selectedFeedback = FBListView.getSelectionModel().getSelectedItem();
        if (selectedFeedback != null) {
            deleteFeedback(selectedFeedback);
        } else {
            showAlert("No Feedback Selected", "Please select a feedback to delete.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterFeedbackList());
    }

    private void setupFilters() {
        filter.getItems().addAll("All", "1 Star", "2 Stars", "3 Stars", "4 Stars", "5 Stars");
        dateFilter.getItems().addAll("All", "Last 7 days", "Last 30 days");

        filter.setOnAction(event -> filterFeedbackList());
        dateFilter.setOnAction(event -> filterFeedbackList());
    }

    private void filterFeedbackList() {
        String searchText = searchField.getText().toLowerCase();
        String selectedRating = filter.getValue();
        String selectedDate = dateFilter.getValue();

        List<Feedback> filteredList = feedbackService.recuperer().stream()
                .filter(feedback -> feedback.getRec().getTitre().toLowerCase().contains(searchText))
                .filter(feedback -> filterByRating(feedback, selectedRating))
                .filter(feedback -> filterByDate(feedback, selectedDate))
                .collect(Collectors.toList());

        feedbackList.setAll(filteredList);
        updateStats();
    }

    private boolean filterByRating(Feedback feedback, String rating) {
        if (rating == null || rating.equals("All")) return true;
        int selectedStars = Integer.parseInt(rating.split(" ")[0]);
        return feedback.getNote() == selectedStars;
    }

    private boolean filterByDate(Feedback feedback, String dateFilter) {
        if (dateFilter == null || dateFilter.equals("All")) return true;
        long now = System.currentTimeMillis();
        long feedbackTime = feedback.getDateFeedback().getTime();
        if (dateFilter.equals("Last 7 days")) {
            return feedbackTime >= now - (7L * 24 * 60 * 60 * 1000);
        } else if (dateFilter.equals("Last 30 days")) {
            return feedbackTime >= now - (30L * 24 * 60 * 60 * 1000);
        }
        return true;
    }

    private void updateStats() {
        // Update PieChart
        pieChart.getData().clear();
        long total = feedbackList.size();
        if (total == 0) return;

        long[] ratingsCount = new long[5];
        feedbackList.forEach(fb -> ratingsCount[fb.getNote() - 1]++);

        for (int i = 0; i < ratingsCount.length; i++) {
            if (ratingsCount[i] > 0) {
                pieChart.getData().add(new PieChart.Data((i + 1) + " Stars", ratingsCount[i]));
            }
        }

        // Update BarChart
        barChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Feedback Ratings");

        for (int i = 0; i < ratingsCount.length; i++) {
            if (ratingsCount[i] > 0) {
                series.getData().add(new XYChart.Data<>((i + 1) + " Stars", ratingsCount[i]));
            }
        }

        barChart.getData().add(series);
    }
}
