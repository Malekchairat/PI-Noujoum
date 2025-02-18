package services;

import tools.MyDataBase;
import models.Panier;
import java.sql.*;
import java.util.List;

public class PanierService implements IService<Panier> {

    private Connection cnx = MyDataBase.getInstance().getCnx();

    @Override
    public void ajouter(Panier panier) {
        String req = "INSERT INTO panier (id_produit, id_user, nbr_produit) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = cnx.prepareStatement(req)) {
            pstmt.setInt(1, panier.getId_produit());
            pstmt.setInt(2, panier.getId_user());
            pstmt.setInt(3, panier.getNbr_produit());
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
        // Implémentation pour récupérer la liste des paniers
        return null;  // À compléter selon la logique nécessaire
    }

    // Méthode pour vérifier si un produit existe
    public boolean produitExiste(int idProduit) {
        String query = "SELECT COUNT(*) FROM produit WHERE id_produit = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, idProduit);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Si le produit existe (compte > 0), retourner true
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du produit : " + e.getMessage());
        }
        return false; // Retourne false si le produit n'existe pas
    }

    // Méthode pour vérifier si un utilisateur existe
    public boolean userExiste(int idUser) {  // Changer private en public
        String query = "SELECT COUNT(*) FROM user WHERE id_user = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, idUser);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Si l'utilisateur existe (compte > 0), retourner true
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'utilisateur : " + e.getMessage());
        }
        return false; // Retourne false si l'utilisateur n'existe pas
    }

    // Méthode pour vérifier si un panier existe par son ID
    public boolean exists(int idPanier) {
        String query = "SELECT COUNT(*) FROM panier WHERE id_panier = ?";
        try (PreparedStatement pstmt = cnx.prepareStatement(query)) {
            pstmt.setInt(1, idPanier);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Si le panier existe (compte > 0), retourner true
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du panier : " + e.getMessage());
        }
        return false; // Retourne false si le panier n'existe pas
    }
}
