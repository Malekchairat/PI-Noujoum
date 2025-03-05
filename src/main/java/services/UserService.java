package services;

<<<<<<< HEAD
import models.User;
<<<<<<< HEAD
=======
import javafx.scene.control.TextField;
import models.User;
>>>>>>> origin/GestionCommande
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
import javax.sql.rowset.serial.SerialBlob;
=======
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tools.MyDataBase;
>>>>>>> origin/integration-branch
=======
>>>>>>> origin/GestionCommande

public class UserService implements IService<User> {

    private final Connection cnx;

    public UserService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> origin/GestionCommande
    public void ajouter(User t) {
        String req = "INSERT INTO user (nom, prenom, email, mdp, tel, image, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(req)) {
            stmt.setString(1, t.getNom());
            stmt.setString(2, t.getPrenom());
            stmt.setString(3, t.getEmail());
            stmt.setString(4, t.getMdp());
            stmt.setInt(5, t.getTel());

<<<<<<< HEAD
            if (t.getImage() != null) {
                stmt.setBlob(6, t.getImage());
            } else {
                stmt.setNull(6, Types.BLOB);
=======
            // Handle Blob (image)
            if (t.getImage() != null) {
                stmt.setBlob(6, t.getImage());
            } else {
                stmt.setNull(6, Types.BLOB); // Set null if image is not available
>>>>>>> origin/GestionCommande
            }

            stmt.setString(7, t.getRole());

            int result = stmt.executeUpdate();
            System.out.println(result + " enregistrement ajouté.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

<<<<<<< HEAD
    public boolean existEmail(String email) throws SQLException {
        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

=======
>>>>>>> origin/GestionCommande
    @Override
    public void modifier(User t) {
        String req = "UPDATE user SET nom=?, prenom=?, email=?, mdp=?, tel=?, image=?, role=? WHERE id_user=?";
        try (PreparedStatement stmt = cnx.prepareStatement(req)) {
            stmt.setString(1, t.getNom());
            stmt.setString(2, t.getPrenom());
            stmt.setString(3, t.getEmail());
            stmt.setString(4, t.getMdp());
            stmt.setInt(5, t.getTel());
<<<<<<< HEAD
            stmt.setBlob(6, t.getImage());
=======
            stmt.setBlob(6, t.getImage()); // Store the Blob image
>>>>>>> origin/GestionCommande
            stmt.setString(7, t.getRole());
            stmt.setInt(8, t.getId());

            stmt.executeUpdate();
            System.out.println("Utilisateur modifié avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM user WHERE id_user=?";
        try (PreparedStatement stmt = cnx.prepareStatement(req)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Utilisateur supprimé avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public List<User> recuperer() {
        List<User> users = new ArrayList<>();
        String req = "SELECT * FROM user";
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
<<<<<<< HEAD
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
=======
    public void ajouter(User t) throws SQLException {
        String req = "INSERT INTO user (nom, prenom, email, mdp, tel, image, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = cnx.prepareStatement(req);
        stmt.setString(1, t.getNom());
        stmt.setString(2, t.getPrenom());
        stmt.setString(3, t.getEmail());
        stmt.setString(4, t.getMdp());
        stmt.setInt(5, t.getTel());
        stmt.setString(6, String.valueOf(t.getImage()));
        stmt.setString(7, t.getRole());

        int result = stmt.executeUpdate();
        System.out.println(result + " enregistrement ajouté.");
    }

    public boolean existemail(String email) throws SQLException {
        String query = "SELECT * FROM user WHERE email = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs.next(); // Retourne vrai si un utilisateur existe avec cet email
    }

    @Override
    public void modifier(User t) throws SQLException {
        String req = "UPDATE user SET nom=?, prenom=?, email=?, mdp=?, tel=?, image=?, role=? WHERE id_user=?";
        PreparedStatement stmt = cnx.prepareStatement(req);
        stmt.setString(1, t.getNom());
        stmt.setString(2, t.getPrenom());
        stmt.setString(3, t.getEmail());
        stmt.setString(4, t.getMdp());
        stmt.setInt(5, t.getTel());
        stmt.setString(6, String.valueOf(t.getImage()));
        stmt.setString(7, t.getRole());
        stmt.setInt(8, t.getId());

        stmt.executeUpdate();
        System.out.println("Utilisateur modifié avec succès.");
    }

    @Override
    public void supprimer(User t) throws SQLException {
        String req = "DELETE FROM user WHERE id_user=?";
        PreparedStatement stmt = cnx.prepareStatement(req);
        stmt.setInt(1, t.getId());
        stmt.executeUpdate();
        System.out.println("Utilisateur supprimé avec succès.");
    }

    public List<User> rechercherParNom(String nom) throws SQLException {
        List<User> users = new ArrayList<>();
        String req = "SELECT * FROM user WHERE (role='Artiste' OR role='simple utilisateur') " +
                "AND (nom LIKE ? OR email LIKE ? OR prenom LIKE ?)";
        PreparedStatement stm = cnx.prepareStatement(req);
        String searchPattern = "%" + nom + "%";
        stm.setString(1, searchPattern);
        stm.setString(2, searchPattern);
        stm.setString(3, searchPattern);
        ResultSet rs = stm.executeQuery();

        while (rs.next()) {
            users.add(mapUser(rs));
>>>>>>> origin/integration-branch
=======
                User user = mapUser(rs);
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
>>>>>>> origin/GestionCommande
        }
        return users;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> origin/GestionCommande
    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id_user"));
        user.setNom(rs.getString("nom"));
        user.setPrenom(rs.getString("prenom"));
        user.setEmail(rs.getString("email"));
        user.setMdp(rs.getString("mdp"));
        user.setTel(rs.getInt("tel"));
        user.setRole(rs.getString("role"));
<<<<<<< HEAD
        user.setImage(rs.getBlob("image"));
        return user;
=======
    public void modifMDP(String email, String mdp) throws SQLException {
        String req = "UPDATE user SET mdp=? WHERE email=?";
        PreparedStatement stmt = cnx.prepareStatement(req);
        stmt.setString(1, mdp);
        stmt.setString(2, email);
        stmt.executeUpdate();
        System.out.println("Mot de passe mis à jour avec succès.");
    }

    @Override
    public List<User> recuperer() throws SQLException {
        List<User> users = new ArrayList<>();
        String req = "SELECT * FROM user";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(req);

        while (rs.next()) {
            users.add(mapUser(rs));
        }
        return users;
    }

    public String getNom(int id) throws SQLException {
        String req = "SELECT nom FROM user WHERE id_user = ?";
        PreparedStatement st = cnx.prepareStatement(req);
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            return rs.getString("nom");
        }
        return "";
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User p = new User();
        p.setId(rs.getInt("id_user"));
        p.setTel(rs.getInt("tel"));
        p.setEmail(rs.getString("email"));
        p.setNom(rs.getString("nom"));
        p.setPrenom(rs.getString("prenom"));
        p.setRole(rs.getString("role"));
        //p.setImage(rs.getString("image"));
        p.setMdp(rs.getString("mdp"));
        return p;
>>>>>>> origin/integration-branch
    }
=======
        user.setImage(rs.getBlob("image")); // Retrieve image as Blob
        return user;
    }

    // Method to fetch user image
    public Blob getUserImage(int userId) {
        Blob image = null;
        String query = "SELECT image FROM user WHERE id_user = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                image = rs.getBlob("image");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return image;
    }

    // Function to get a user by ID
    public User getById(int id) {
        User utilisateur = null;
        String req = "SELECT * FROM user WHERE id_user = ?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(req)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    utilisateur = new User();
                    utilisateur.setId(resultSet.getInt("id_user"));
                    utilisateur.setNom(resultSet.getString("nom"));
                    utilisateur.setPrenom(resultSet.getString("prenom"));
                    utilisateur.setEmail(resultSet.getString("email"));
                    utilisateur.setMdp(resultSet.getString("mdp"));
                    utilisateur.setTel(resultSet.getInt("tel"));
                    utilisateur.setRole(resultSet.getString("role"));
                    utilisateur.setImage(resultSet.getBlob("image"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;
    }




>>>>>>> origin/GestionCommande
}
