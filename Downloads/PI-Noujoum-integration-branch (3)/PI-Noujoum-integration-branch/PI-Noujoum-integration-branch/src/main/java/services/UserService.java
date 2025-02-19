package services;

import models.User;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User> {

    private final Connection cnx;

    public UserService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouter(User t) {
        String req = "INSERT INTO user (nom, prenom, email, mdp, tel, image, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(req)) {
            stmt.setString(1, t.getNom());
            stmt.setString(2, t.getPrenom());
            stmt.setString(3, t.getEmail());
            stmt.setString(4, t.getMdp());
            stmt.setInt(5, t.getTel());
            stmt.setString(6, String.valueOf(t.getImage()));
            stmt.setString(7, t.getRole());

            int result = stmt.executeUpdate();
            System.out.println(result + " enregistrement ajouté.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public void modifier(User t) {
        String req = "UPDATE user SET nom=?, prenom=?, email=?, mdp=?, tel=?, image=?, role=? WHERE id_user=?";
        try (PreparedStatement stmt = cnx.prepareStatement(req)) {
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
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return users;
    }

    public boolean existemail(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Retourne vrai si un utilisateur existe avec cet email
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'email : " + e.getMessage());
        }
        return false;
    }

    public void modifMDP(String email, String mdp) {
        String req = "UPDATE user SET mdp=? WHERE email=?";
        try (PreparedStatement stmt = cnx.prepareStatement(req)) {
            stmt.setString(1, mdp);
            stmt.setString(2, email);
            stmt.executeUpdate();
            System.out.println("Mot de passe mis à jour avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du mot de passe : " + e.getMessage());
        }
    }

    public List<User> rechercherParNom(String nom) {
        List<User> users = new ArrayList<>();
        String req = "SELECT * FROM user WHERE (role='Artiste' OR role='simple utilisateur') " +
                "AND (nom LIKE ? OR email LIKE ? OR prenom LIKE ?)";
        try (PreparedStatement stm = cnx.prepareStatement(req)) {
            String searchPattern = "%" + nom + "%";
            stm.setString(1, searchPattern);
            stm.setString(2, searchPattern);
            stm.setString(3, searchPattern);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par nom : " + e.getMessage());
        }
        return users;
    }

    public String getNom(int id) {
        String req = "SELECT nom FROM user WHERE id_user = ?";
        try (PreparedStatement st = cnx.prepareStatement(req)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("nom");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du nom : " + e.getMessage());
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
    }
}
