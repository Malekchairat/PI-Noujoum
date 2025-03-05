package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Feedback;
import models.User;
import services.FeedbackService;
import services.UserService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class FeedbackDashboardController {

    @FXML
    private ListView<Feedback> FBListView;

    @FXML
    private TextArea commentaire;



    @FXML
    private ComboBox<String> filter;

    @FXML
    private ComboBox<Integer> note;

    @FXML
    private TextField searchField;

    @FXML
    private Button update;

    @FXML
    private Button delete;

    private final FeedbackService feedbackService = new FeedbackService();
    private ObservableList<Feedback> feedbackList = FXCollections.observableArrayList();
    UserService us = new UserService();
    private final User logged_in_User = us.recupererParId(2) ;

    @FXML
    public void initialize() {
        loadFeedbacks();
        setupListeners();
        setupNoteComboBox();
        setupListViewCellFactory();
    }

    private void setupListViewCellFactory() {
        FBListView.setCellFactory(lv -> new ListCell<Feedback>() {
            @Override
            protected void updateItem(Feedback feedback, boolean empty) {
                super.updateItem(feedback, empty);
                if (empty || feedback == null) {
                    setText(null);
                    setStyle(""); // Reset style
                } else {
                    // 🎨 Construire l'affichage stylisé
                    setText("📌 " + feedback.getRec().getTitre() +
                            "  ⭐ " + feedback.getNote() + "/5\n" +
                            "💬 " + feedback.getCommentaire());

                    // 🌟 Appliquer des couleurs en fonction de la note
                    if (feedback.getNote() >= 4) {
                        setStyle("-fx-background-color: #D4EDDA; -fx-text-fill: #155724; -fx-padding: 8px; -fx-border-radius: 5px;");
                    } else if (feedback.getNote() == 3) {
                        setStyle("-fx-background-color: #FFF3CD; -fx-text-fill: #856404; -fx-padding: 8px; -fx-border-radius: 5px;");
                    } else {
                        setStyle("-fx-background-color: #F8D7DA; -fx-text-fill: #721C24; -fx-padding: 8px; -fx-border-radius: 5px;");
                    }
                }
            }
        });
    }

    private void loadFeedbacks() {
        //feedbackList.setAll(feedbackService.getByUserId(logged_in_User.getId_user()));
        feedbackList.setAll(feedbackService.getByUserId(logged_in_User.getId_user()));
        FBListView.setItems(feedbackList);
        setupFilters();
        filterFeedbacks();
    }

    @FXML
    private void setupListeners() {
        FBListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFeedbackDetails(newSelection);
            }
        });

        // 🔍 Recherche en temps réel
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterFeedbacks();
        });




        // ⭐ Filtrage par note
        filter.valueProperty().addListener((observable, oldValue, newValue) -> {
            filterFeedbacks();
        });
    }

    private void filterFeedbacks() {
        String searchText = searchField.getText().toLowerCase();
        String selectedNoteFilter = filter.getValue();

        ObservableList<Feedback> filteredList = FXCollections.observableArrayList();

        for (Feedback feedback : feedbackList) {
            boolean matchesSearch = feedback.getRec().getTitre().toLowerCase().contains(searchText)
                    || feedback.getCommentaire().toLowerCase().contains(searchText);



            boolean matchesNote = true; // Par défaut, on accepte tout
            if (selectedNoteFilter != null && !selectedNoteFilter.equals("Toutes")) {
                matchesNote = feedback.getNote() == Integer.parseInt(selectedNoteFilter);
            }

            if (matchesSearch  && matchesNote) {
                filteredList.add(feedback);
            }
        }

        FBListView.setItems(filteredList);
    }

    private boolean checkDateFilter(Feedback feedback, String filterType) {
        Date feedbackDate = feedback.getDateFeedback(); // `Date` venant du feedback
        LocalDate today = LocalDate.now();

        // Convertir `Date` en `LocalDate`
        Instant instant = feedbackDate.toInstant();
        LocalDate feedbackLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        switch (filterType) {
            case "Aujourd'hui":
                return feedbackLocalDate.isEqual(today);
            case "Cette semaine":
                return !feedbackLocalDate.isBefore(today.minusDays(7));
            case "Ce mois":
                return feedbackLocalDate.getMonth() == today.getMonth() && feedbackLocalDate.getYear() == today.getYear();
            default:
                return true; // Accepte toutes les dates si "Toutes" est sélectionné
        }
    }



    private void setupNoteComboBox() {
        note.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5)); // Notes de 1 à 5
    }

    private void fillFeedbackDetails(Feedback feedback) {
        commentaire.setText(feedback.getCommentaire());
        note.setValue(feedback.getNote());
    }

    private void setupFilters() {


        // ⭐ Filtrage des notes (1 à 5 étoiles)
        filter.setItems(FXCollections.observableArrayList("Toutes", "1", "2", "3", "4", "5"));
        filter.setValue("Toutes"); // Valeur par défaut
    }

    @FXML
    void update(ActionEvent event) {
        Feedback selectedFeedback = FBListView.getSelectionModel().getSelectedItem();
        if (selectedFeedback != null) {
            selectedFeedback.setCommentaire(commentaire.getText());
            selectedFeedback.setNote(note.getValue());
            feedbackService.modifier(selectedFeedback, selectedFeedback.getId());
            loadFeedbacks(); // Rafraîchir la liste après mise à jour
        } else {
            showAlert("Erreur", "Veuillez sélectionner un feedback à modifier.");
        }
    }

    @FXML
    void delete(ActionEvent event) {
        Feedback selectedFeedback = FBListView.getSelectionModel().getSelectedItem();
        if (selectedFeedback != null) {
            feedbackService.supprimer(selectedFeedback.getId());
            loadFeedbacks(); // Rafraîchir la liste après suppression
        } else {
            showAlert("Erreur", "Veuillez sélectionner un feedback à supprimer.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
