package services;

import models.Promotion;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromotionCrud implements IService<Promotion> {
    private final Connection cnx;

    public PromotionCrud() {
        this.cnx = MyDataBase.getInstance().getCnx();
    }

    // Ajouter une promotion
    @Override
    public void ajouter(Promotion promotion) {
        String query = "INSERT INTO promotion (code, pourcentage, expiration, id_produit) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, promotion.getCode());
            stmt.setFloat(2, promotion.getPourcentage());
            stmt.setString(3, promotion.getExpiration());
            stmt.setInt(4, promotion.getProduitId());

            int rowsAffected = stmt.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Promotion ajoutée avec succès" : "Aucune ligne affectée");

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la promotion : " + e.getMessage());
        }
    }

    // Modifier une promotion
    @Override
    public void modifier(Promotion promotion) {
        String query = "UPDATE promotion SET code = ?, pourcentage = ?, expiration = ?, id_produit = ? WHERE id_promotion = ?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, promotion.getCode());
            pst.setFloat(2, promotion.getPourcentage());
            pst.setString(3, promotion.getExpiration());
            pst.setInt(4, promotion.getProduitId());
            pst.setInt(5, promotion.getIdpromotion());

            int rows = pst.executeUpdate();
            System.out.println(rows > 0 ? "Promotion mise à jour avec succès" : "Aucune mise à jour effectuée");

        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la promotion : " + e.getMessage());
        }
    }

    // Supprimer une promotion
    @Override
    public void supprimer(int id) {
        String query = "DELETE FROM promotion WHERE id_promotion = ?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);

            int rows = pst.executeUpdate();
            System.out.println(rows > 0 ? "Promotion supprimée avec succès" : "Aucune suppression effectuée");

        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la promotion : " + e.getMessage());
        }
    }

    // Récupérer une promotion par ID
    //@Override
    public Promotion recuperer(int id) {
        String query = "SELECT * FROM promotion WHERE id_promotion = ?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new Promotion(
                        rs.getInt("id_promotion"),
                        rs.getString("code"),
                        rs.getFloat("pourcentage"),
                        rs.getString("expiration"),
                        rs.getInt("id_produit"),
                        getProduitName(rs.getInt("id_produit"))
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la promotion : " + e.getMessage());
        }

        return null;
    }

    // Récupérer toutes les promotions
    @Override
    public List<Promotion> recuperer() {
        List<Promotion> promotions = new ArrayList<>();
        String query = "SELECT * FROM promotion";

        try (Statement stmt = cnx.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                promotions.add(new Promotion(
                        rs.getInt("id_promotion"),
                        rs.getString("code"),
                        rs.getFloat("pourcentage"),
                        rs.getString("expiration"),
                        rs.getInt("id_produit"),
                        getProduitName(rs.getInt("id_produit"))
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des promotions : " + e.getMessage());
        }

        return promotions;
    }

    // Méthode pour récupérer le nom d'un produit à partir de son ID
    public String getProduitName(int produitId) {
        String query = "SELECT nom FROM produit WHERE idproduit = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, produitId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("nom");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du nom du produit : " + e.getMessage());
        }
        return "Produit inconnu";
    }
}
