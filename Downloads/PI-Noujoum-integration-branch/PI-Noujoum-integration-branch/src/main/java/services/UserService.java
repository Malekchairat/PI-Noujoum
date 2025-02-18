package services;

import models.User;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private Connection connection;

    public UserService() {
        connection = MyDataBase.getInstance().getCnx();
    }

    public void ajouterUser(User user) {
        String sql = "INSERT INTO users (nom, prenom, email, mdp, tel, role, image) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getNom());
            preparedStatement.setString(2, user.getPrenom());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getMdp());
            preparedStatement.setInt(5, user.getTel());
            preparedStatement.setString(6, user.getRole());
            preparedStatement.setBlob(7, user.getImage());

            preparedStatement.executeUpdate();
            System.out.println("✅ Utilisateur ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    public void modifierUser(User user) {
        String sql = "UPDATE users SET nom=?, prenom=?, email=?, mdp=?, tel=?, role=?, image=? WHERE id_user=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getNom());
            preparedStatement.setString(2, user.getPrenom());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getMdp());
            preparedStatement.setInt(5, user.getTel());
            preparedStatement.setString(6, user.getRole());
            preparedStatement.setBlob(7, user.getImage());
            preparedStatement.setInt(8, user.getId());

            preparedStatement.executeUpdate();
            System.out.println("✅ Utilisateur modifié avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la modification : " + e.getMessage());
        }
    }

    public void supprimerUser(int id) {
        String sql = "DELETE FROM users WHERE id_user=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("✅ Utilisateur supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {  // ✅ Correction ici
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id_user"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("email"),
                        resultSet.getString("mdp"),
                        resultSet.getInt("tel"),
                        resultSet.getString("role"),
                        resultSet.getBlob("image")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }

        return users;
    }
}
