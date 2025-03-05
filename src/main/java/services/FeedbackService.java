package services;

import models.Feedback;
import models.Reclamation;
import models.User;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackService implements IService<Feedback> {

    private Connection cnx = MyDataBase.getInstance().getCnx();
    private UserService userService = new UserService();
    private ReclamationService reclamationService = new ReclamationService();


    public FeedbackService() {
    }

    @Override
    public void ajouter(Feedback feedback) {
        String query = "INSERT INTO feedback (reclamation_id, utilisateur_id, note, commentaire, date_feedback) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, feedback.getRec().getId());
            pst.setInt(2, feedback.getRec().getUser().getId_user());
            pst.setInt(3, feedback.getNote());
            pst.setString(4, feedback.getCommentaire());
            pst.setDate(5, new java.sql.Date(feedback.getDateFeedback().getTime()));

            pst.executeUpdate();
            System.out.println("Feedback ajouté avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du feedback: " + e.getMessage());
        }
    }

    @Override
    public void modifier(Feedback feedback, int id) {
        String query = "UPDATE feedback SET reclamation_id=?, utilisateur_id=?, note=?, commentaire=?, date_feedback=? WHERE id=?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, feedback.getRec().getId());
            pst.setInt(2, feedback.getUser().getId_user());
            pst.setInt(3, feedback.getNote());
            pst.setString(4, feedback.getCommentaire());
            pst.setDate(5, new java.sql.Date(feedback.getDateFeedback().getTime()));
            pst.setInt(6, id);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Feedback mis à jour avec succès.");
            } else {
                System.out.println("Aucun feedback trouvé avec l'ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du feedback: " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String query = "DELETE FROM feedback WHERE id=?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Feedback supprimé avec succès.");
            } else {
                System.out.println("Aucun feedback trouvé avec l'ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du feedback: " + e.getMessage());
        }
    }

    @Override
    public List<Feedback> recuperer() {
        List<Feedback> feedbacks = new ArrayList<>();
        String query = "SELECT * FROM feedback";

        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int reclamationId = rs.getInt("reclamation_id");
                int utilisateurId = rs.getInt("utilisateur_id");
                int note = rs.getInt("note");
                String commentaire = rs.getString("commentaire");
                Date dateFeedback = rs.getDate("date_feedback");

                Reclamation rec = reclamationService.recupererParId(reclamationId);
                User user = userService.recupererParId(utilisateurId);

                feedbacks.add(new Feedback(id, rec, user, note, commentaire, dateFeedback));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des feedbacks: " + e.getMessage());
        }
        return feedbacks;
    }

    @Override
    public Feedback recupererParId(int id) {
        String query = "SELECT * FROM feedback WHERE id=?";
        Feedback feedback = null;

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int reclamationId = rs.getInt("reclamation_id");
                int utilisateurId = rs.getInt("utilisateur_id");
                int note = rs.getInt("note");
                String commentaire = rs.getString("commentaire");
                Date dateFeedback = rs.getDate("date_feedback");

                Reclamation rec = reclamationService.recupererParId(reclamationId);
                User user = userService.recupererParId(utilisateurId);

                feedback = new Feedback(id, rec, user, note, commentaire, dateFeedback);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du feedback: " + e.getMessage());
        }
        return feedback;
    }

    public List<Feedback> getByUserId(int userId) {
        List<Feedback> feedbacks = new ArrayList<>();
        String query = "SELECT * FROM feedback WHERE utilisateur_id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int reclamationId = rs.getInt("reclamation_id");
                int note = rs.getInt("note");
                String commentaire = rs.getString("commentaire");
                Date dateFeedback = rs.getDate("date_feedback");

                Reclamation rec = reclamationService.recupererParId(reclamationId);
                User user = userService.recupererParId(userId);

                feedbacks.add(new Feedback(id, rec, user, note, commentaire, dateFeedback));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des feedbacks pour l'utilisateur: " + e.getMessage());
        }
        return feedbacks;
    }



}
