package services;

import models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tools.MyDataBase;

public class UserService implements IService<User> {

    private final Connection cnx;

    public UserService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
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
        }
        return users;
    }

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
    }
}
