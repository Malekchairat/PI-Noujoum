package services;

import models.Reclamation;
import models.User;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements IService<Reclamation> {

    private Connection cnx = MyDataBase.getInstance().getCnx();

    public ReclamationService() {}

    @Override
    public void ajouter(Reclamation reclamation) {
        String query = "INSERT INTO reclamation (user_id, titre, description, date_creation, statut, priorite, answer) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, reclamation.getUser().getId_user());
            pst.setString(2, reclamation.getTitre());
            pst.setString(3, reclamation.getDescription());
            pst.setDate(4, new java.sql.Date(reclamation.getDateCreation().getTime()));
            pst.setString(5, reclamation.getStatut());
            pst.setString(6, reclamation.getPriorite());
            pst.setString(7, reclamation.getAnswer());
            pst.executeUpdate();
            System.out.println("Réclamation ajoutée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur d'ajout de la réclamation : " + e.getMessage());
        }
    }

    @Override
    public void modifier(Reclamation reclamation, int id) {
        String query = "UPDATE reclamation SET user_id = ?, titre = ?, description = ?, statut = ?, priorite = ?, answer = ? WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, reclamation.getUser().getId_user());
            pst.setString(2, reclamation.getTitre());
            pst.setString(3, reclamation.getDescription());
            pst.setString(4, reclamation.getStatut());
            pst.setString(5, reclamation.getPriorite());
            pst.setString(6, reclamation.getAnswer());
            pst.setInt(7, id);
            pst.executeUpdate();
            System.out.println("Réclamation mise à jour avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur de mise à jour de la réclamation : " + e.getMessage());
        }
    }

    public void updateWithAnswer(int id, String answer) {
        String query = "UPDATE reclamation SET answer = ? WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, answer);
            pst.setInt(2, id);
            pst.executeUpdate();
            System.out.println("Réponse ajoutée avec succès à la réclamation !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réponse : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String query = "DELETE FROM reclamation WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Réclamation supprimée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur de suppression de la réclamation : " + e.getMessage());
        }
    }

    @Override
    public List<Reclamation> recuperer() {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamation";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            UserService us = new UserService();
            while (rs.next()) {
                Reclamation r = new Reclamation(
                        rs.getInt("id"),
                        us.recupererParId(rs.getInt("user_id")),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getDate("date_creation"),
                        rs.getString("statut"),
                        rs.getString("priorite"),
                        rs.getString("answer")
                );
                reclamations.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Erreur de récupération des réclamations : " + e.getMessage());
        }
        return reclamations;
    }

    @Override
    public Reclamation recupererParId(int id) {
        String query = "SELECT * FROM reclamation WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                UserService us = new UserService();
                return new Reclamation(
                        rs.getInt("id"),
                        us.recupererParId(rs.getInt("user_id")),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getDate("date_creation"),
                        rs.getString("statut"),
                        rs.getString("priorite"),
                        rs.getString("answer")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur de récupération de la réclamation : " + e.getMessage());
        }
        return null;
    }

    public List<Reclamation> getByUserId(int userId) {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamation WHERE user_id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            UserService us = new UserService();
            while (rs.next()) {
                Reclamation r = new Reclamation(
                        rs.getInt("id"),
                        us.recupererParId(rs.getInt("user_id")),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getDate("date_creation"),
                        rs.getString("statut"),
                        rs.getString("priorite"),
                        rs.getString("answer")
                );
                reclamations.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réclamations pour l'utilisateur : " + e.getMessage());
        }
        return reclamations;
    }
}
