package services;

import models.User;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.serial.SerialBlob;

public class UserService implements IService<User> {

    private final Connection cnx;

    public UserService() {
        cnx = MyDataBase.getInstance().getCnx();
    }
    public User authenticateUser(String email, String password) {
        String query = "SELECT * FROM user WHERE email = ? AND mdp = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve user data including image blob
                    Blob blob = rs.getBlob("image");
                    byte[] profilePic = (blob != null) ? blob.getBytes(1, (int) blob.length()) : null;

                    User user = new User(
                            rs.getInt("id_user"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("mdp"),
                            rs.getInt("tel"),
                            rs.getString("role"),
                            rs.getBlob("image")
                    );

                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if authentication fails
    }
    public User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE id = ?"; // Adjust table name if needed
        try {
            Connection connection = MyDataBase.getInstance().getCnx();
            PreparedStatement stmt = connection.prepareStatement(query);            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getInt("phone"),
                        rs.getBlob("profile_picture") // Add other fields as needed
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static User findById(int id) {
        // Retrieve user from database using their ID
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = MyDataBase.getInstance().getCnx();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mdp"),
                        rs.getInt("telephone"),
                        rs.getString("role"),
                        rs.getBlob("image")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

            if (t.getImage() != null) {
                stmt.setBlob(6, t.getImage());
            } else {
                stmt.setNull(6, Types.BLOB);
            }

            stmt.setString(7, t.getRole());

            int result = stmt.executeUpdate();
            System.out.println(result + " enregistrement ajouté.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    public boolean existEmail(String email) throws SQLException {
        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
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
            stmt.setBlob(6, t.getImage());
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

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id_user"));
        user.setNom(rs.getString("nom"));
        user.setPrenom(rs.getString("prenom"));
        user.setEmail(rs.getString("email"));
        user.setMdp(rs.getString("mdp"));
        user.setTel(rs.getInt("tel"));
        user.setRole(rs.getString("role"));
        user.setImage(rs.getBlob("image"));
        return user;
    }
    public void modifMDP(String email, String newPassword) throws SQLException {
        String query = "UPDATE user SET mdp = ? WHERE email = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, newPassword);
            ps.setString(2, email);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Mot de passe mis à jour avec succès pour l'email : " + email);
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet email.");
            }
        }
    }
    public static User findByEmail(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        User result = null;

        try (PreparedStatement stmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Récupération du Blob (image)
                    Blob blob = rs.getBlob("image");
                    byte[] profilePic = (blob != null) ? blob.getBytes(1, (int) blob.length()) : null;

                    result = new User(
                            rs.getInt("id_user"),        // Correspond à id_user
                            rs.getString("nom"),        // Correspond à nom
                            rs.getString("prenom"),     // Correspond à prenom
                            rs.getString("email"),
                            rs.getString("mdp"),   // Correspond à mdp
                            rs.getInt("tel"),         // Correspond à tel (changement en int)
                            rs.getString("role"),       // Garde role en String
                            rs.getBlob("image")                        // Stocke le Blob dans l'attribut image
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Remplace par un logger en prod
        }

        return result;
    }


}
