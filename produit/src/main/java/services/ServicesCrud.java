package services;

import models.Produit;
import tools.MyDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicesCrud implements IService<Produit> {
    private final Connection cnx;

    public ServicesCrud() {
        this.cnx = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Produit produit) {
        String query = "INSERT INTO produit (nom, description, categorie, prix, disponibilite, image) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, produit.getNom());
            pst.setString(2, produit.getDescription());
            pst.setString(3, produit.getCategorie().name());
            pst.setFloat(4, produit.getPrix());
            pst.setInt(5, produit.getDisponibilite());
            pst.setBlob(6, produit.getImage());

            int rows = pst.executeUpdate();
            System.out.println("Produit ajouté. Lignes affectées : " + rows);
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du produit : " + e.getMessage());
        }
    }

    @Override
    public boolean modifier(Produit produit) {
        String query = "UPDATE produit SET nom=?, description=?, categorie=?, prix=?, disponibilite=?, image=? WHERE idproduit=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, produit.getNom());
            pst.setString(2, produit.getDescription());
            pst.setString(3, produit.getCategorie().toString());
            pst.setFloat(4, produit.getPrix());
            pst.setInt(5, produit.getDisponibilite());
            pst.setBlob(6, produit.getImage());
            pst.setInt(7, produit.getIdproduit());

            int rows = pst.executeUpdate();
            System.out.println("Produit mis à jour. Lignes affectées : " + rows);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du produit : " + e.getMessage());
        }
        return false;
    }

    @Override
    public void supprimer(int id) {
        String query = "DELETE FROM produit WHERE idproduit = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            System.out.println("Produit supprimé. Lignes affectées : " + rows);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du produit : " + e.getMessage());
        }
    }

    @Override
    public Produit recuperer(int id) {  // ✅ Décommente @Override
        String query = "SELECT * FROM produit WHERE idproduit = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Produit(
                        rs.getInt("idproduit"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        Produit.Categorie.valueOf(rs.getString("categorie")),
                        rs.getFloat("prix"),
                        rs.getInt("disponibilite"),
                        rs.getBlob("image")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du produit : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Produit> recupererTous() {  // ✅ Décommente @Override
        String query = "SELECT * FROM produit";
        List<Produit> produits = new ArrayList<>();
        try (Statement stmt = cnx.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                produits.add(new Produit(
                        rs.getInt("idproduit"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        Produit.Categorie.valueOf(rs.getString("categorie")),
                        rs.getFloat("prix"),
                        rs.getInt("disponibilite"),
                        rs.getBlob("image")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de tous les produits : " + e.getMessage());
        }
        return produits;
    }
}
