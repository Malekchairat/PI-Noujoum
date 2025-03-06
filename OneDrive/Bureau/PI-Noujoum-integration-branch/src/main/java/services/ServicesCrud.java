package services;

import models.Produit;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicesCrud implements IService<Produit> {
    private final Connection cnx;

    public ServicesCrud() {
        this.cnx = MyDataBase.getInstance().getCnx();
    }
    public List<Produit> recupererFavoris(int userId) {
        List<Produit> produits = new ArrayList<>();
        try {
            String query = "SELECT p.* FROM favoris f INNER JOIN produit p ON f.id_produit = p.id_produit WHERE f.id_user = ?";
            PreparedStatement pst = cnx.prepareStatement(query);  // Use cnx instead of connection
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Produit produit = new Produit(
                        rs.getInt("id_produit"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        Produit.Categorie.valueOf(rs.getString("categorie").toUpperCase()),
                        rs.getFloat("prix"),
                        rs.getInt("disponibilite"),
                        rs.getBlob("image")
                );
                produits.add(produit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }


    @Override
    public void ajouter(Produit produit) {
        String query = "INSERT INTO produit (nom, description, categorie, prix, disponibilite, image) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, produit.getNom());
            pst.setString(2, produit.getDescription());
            pst.setString(3, produit.getCategorie().name());  // Saving the enum as a string
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
    public void modifier(Produit produit) {
        String query = "UPDATE produit SET nom=?, description=?, categorie=?, prix=?, disponibilite=?, image=? WHERE id_produit=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, produit.getNom());
            pst.setString(2, produit.getDescription());
            pst.setString(3, produit.getCategorie().name());  // Saving the enum as a string
            pst.setFloat(4, produit.getPrix());
            pst.setInt(5, produit.getDisponibilite());
            pst.setBlob(6, produit.getImage());
            pst.setInt(7, produit.getIdproduit());

            int rows = pst.executeUpdate();
            System.out.println("Produit mis à jour. Lignes affectées : " + rows);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du produit : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String query = "DELETE FROM produit WHERE id_produit = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            System.out.println("Produit supprimé. Lignes affectées : " + rows);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du produit : " + e.getMessage());
        }
    }

    @Override
    public List<Produit> recuperer() {
        String query = "SELECT * FROM produit";
        List<Produit> produits = new ArrayList<>();
        try (Statement stmt = cnx.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                produits.add(new Produit(
                        rs.getInt("id_produit"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        Produit.Categorie.valueOf(rs.getString("categorie").toUpperCase()),  // Convert String to Categorie enum
                        rs.getFloat("prix"),
                        rs.getInt("disponibilite"),
                        rs.getBlob("image")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des produits : " + e.getMessage());
        }
        return produits;
    }

    // Method to retrieve a single product by its ID
    public Produit recupererParId(int id) {
        Produit produit = null;
        String query = "SELECT * FROM produit WHERE id_produit = ?";

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                produit = new Produit(
                        rs.getInt("id_produit"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        Produit.Categorie.valueOf(rs.getString("categorie").toUpperCase()),  // Convert String to Categorie enum
                        rs.getFloat("prix"),
                        rs.getInt("disponibilite"),
                        rs.getBlob("image")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du produit : " + e.getMessage());
        }
        return produit;
    }

    // Get featured product (returns the nth product by ID)
    public Produit getFeaturedProduct(int id) {
        List<Produit> produits = recuperer();  // Fetch all products (adjust this to get the top featured ones)
        if (id <= produits.size()) {
            return produits.get(id - 1);  // Return the nth product (1-based index)
        }
        return null;  // Return null if the product id is out of bounds
    }
}
