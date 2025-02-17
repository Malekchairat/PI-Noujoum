package services;

import tools.MyDataBase;

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
        }
    }

    @Override
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