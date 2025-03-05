package services;

import models.User;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User> {

    private Connection cnx = MyDataBase.getInstance().getCnx();

    public UserService() {}

    @Override
    public void ajouter(User user) {
        String query = "INSERT INTO user (nom, prenom, email, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, user.getNom());
            pst.setString(2, user.getPrenom());
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getRole());
            pst.executeUpdate();
            System.out.println("Utilisateur ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur d'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public void modifier(User user, int id) {
        String query = "UPDATE user SET nom = ?, prenom = ?, email = ?, role = ? WHERE id_user = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, user.getNom());
            pst.setString(2, user.getPrenom());
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getRole());
            pst.setInt(5, id);
            pst.executeUpdate();
            System.out.println("Utilisateur mis à jour avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur de mise à jour de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String query = "DELETE FROM user WHERE id_user = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Utilisateur supprimé avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur de suppression de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public List<User> recuperer() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                User u = new User(
                        rs.getInt("id_user"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("role")
                );
                users.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Erreur de récupération des utilisateurs : " + e.getMessage());
        }
        return users;
    }

    @Override
    public User recupererParId(int id) {
        String query = "SELECT * FROM user WHERE id_user = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id_user"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur de récupération de l'utilisateur : " + e.getMessage());
        }
        return null;
    }
}
