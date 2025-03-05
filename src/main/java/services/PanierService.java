package services;

import tools.MyDataBase;
<<<<<<< HEAD
import models.Panier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PanierService implements IService<Panier> {

    private Connection cnx = MyDataBase.getInstance().getCnx();

    @Override
    public void ajouter(Panier t) {
        if (!userExists(t.getId_user())) {
            System.err.println("Erreur : L'utilisateur avec l'ID " + t.getId_user() + " n'existe pas.");
            return;
        }

        if (!produitExists(t.getId_produit())) {
            System.err.println("Erreur : Le produit avec l'ID " + t.getId_produit() + " n'existe pas.");
            return;
        }

        String req = "INSERT INTO panier (id_produit, id_user, nbr_produit) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = cnx.prepareStatement(req)) {
            pstmt.setInt(1, t.getId_produit());
            pstmt.setInt(2, t.getId_user());
            pstmt.setInt(3, t.getNbr_produit());
            pstmt.executeUpdate();
            System.out.println("Panier ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du panier : " + e.getMessage());
=======

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.Panier;
import java.sql.PreparedStatement;


public class PanierService implements IPanierservice<Panier> {

    Connection cnx = MyDataBase.getInstance().getCnx();

    public PanierService() {
    }

    public void ajouter(Panier t) throws SQLException {
        try {
            String req = "INSERT INTO `panier`(`id_produit`, `id_user`, `nbr_produit`) VALUES ('" + t.getId_produit() + "','" + t.getId_user() + "','" + t.getNbr_produit() + "')";
            Statement stm = this.cnx.createStatement();
            stm.executeUpdate(req);
        } catch (SQLException var4) {
            SQLException ex = var4;
            System.out.println(ex.getMessage());
>>>>>>> origin/integration-branch
        }
    }

    @Override
<<<<<<< HEAD
    public void modifier(Panier panier) {
        String sql = "UPDATE panier SET id_produit = ?, id_user = ?, nbr_produit = ? WHERE id_panier = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, panier.getId_produit());
            stmt.setInt(2, panier.getId_user());
            stmt.setInt(3, panier.getNbr_produit());
            stmt.setInt(4, panier.getId_panier());

            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Panier mis à jour !" : "Aucun panier trouvé.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM panier WHERE id_panier = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            System.out.println(rowsDeleted > 0 ? "Panier supprimé !" : "Aucun panier trouvé.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public List<Panier> recuperer() {
        List<Panier> paniers = new ArrayList<>();
        String req = "SELECT * FROM panier";

        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(req)) {
            while (rs.next()) {
                paniers.add(new Panier(rs.getInt("id_panier"), rs.getInt("id_produit"), rs.getInt("id_user"), rs.getInt("nbr_produit")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération : " + e.getMessage());
        }
        return paniers;
    }

    // Vérifie si l'utilisateur existe dans la base de données
    public boolean userExists(int idUser) {
        String query = "SELECT COUNT(*) FROM user WHERE id_user = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'utilisateur : " + e.getMessage());
        }
        return false;
    }

    // Vérifie si le produit existe dans la base de données
    public boolean produitExists(int idProduit) {
        String query = "SELECT COUNT(*) FROM produit WHERE id_produit = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, idProduit);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du produit : " + e.getMessage());
        }
        return false;
    }
}
=======
    public void modifier(Panier panier, String var2) throws SQLException {
        String sql = "UPDATE panier SET id_produit = ?, id_user = ?, nbr_produit = ? WHERE id_panier = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, panier.getId_produit());
            stmt.setInt(2, panier.getId_user()); // Ajout de id_user
            stmt.setInt(3, panier.getNbr_produit());
            stmt.setInt(4, panier.getId_panier()); // id_panier reste dans WHERE pour identifier la ligne

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Panier mis à jour avec succès.");
            } else {
                System.out.println("Aucun panier trouvé avec cet ID.");
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la mise à jour du panier : " + ex.getMessage());
            throw ex;
        }
    }


    @Override
    public void supprimer(Panier panier) {
        String sql = "DELETE FROM panier WHERE id_panier = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, panier.getId_panier());
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Panier supprimé avec succès.");
            } else {
                System.out.println("Aucun panier trouvé avec cet ID.");
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la suppression du panier : " + ex.getMessage());
        }
    }


    @Override
    public List<Panier> recuperer() throws SQLException {
        List<Panier> paniers = new ArrayList<>();
        String req = "SELECT * FROM panier";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);

            while (rs.next()) {
                Panier p = new Panier();
                p.setId_panier(rs.getInt("id_panier"));
                p.setId_produit(rs.getInt("id_produit"));
                p.setId_user(rs.getInt("id_user"));
                p.setNbr_produit(rs.getInt("nbr_produit"));
                paniers.add(p);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return paniers;
    }

    public Panier getOne(Panier t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Panier> getAll(Panier t) {
        String req = "SELECT * FROM `panier`";
        ArrayList<Panier> paniers = new ArrayList();

        try {
            Statement stm = this.cnx.createStatement();
            ResultSet rs = stm.executeQuery(req);

            while (rs.next()) {
                Panier p = new Panier();
                p.setId_panier(rs.getInt("id_panier"));
                p.setId_produit(rs.getInt("id_produit"));
                p.setId_user(rs.getInt("id_user"));
                p.setNbr_produit(rs.getInt("nbr_produit"));
                paniers.add(p);
            }
        } catch (SQLException var7) {
            SQLException ex = var7;
            System.out.println(ex.getMessage());
        }

        return paniers;
    }
}
>>>>>>> origin/integration-branch
