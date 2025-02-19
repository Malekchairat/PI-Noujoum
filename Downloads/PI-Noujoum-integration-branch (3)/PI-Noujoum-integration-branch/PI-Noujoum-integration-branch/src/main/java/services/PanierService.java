package services;

import tools.MyDataBase;
import models.Panier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PanierService implements IService<Panier> {

    private Connection cnx = MyDataBase.getInstance().getCnx();

    @Override
    public void ajouter(Panier t) {
        String req = "INSERT INTO panier (id_produit, id_user, nbr_produit) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = cnx.prepareStatement(req)) {
            pstmt.setInt(1, t.getId_produit());
            pstmt.setInt(2, t.getId_user());
            pstmt.setInt(3, t.getNbr_produit());
            pstmt.executeUpdate();
            System.out.println("Panier ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du panier : " + e.getMessage());
        }
    }

    @Override
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
}
