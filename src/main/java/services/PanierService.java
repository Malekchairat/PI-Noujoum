package services;

import models.Panier;
import models.Produit;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PanierService {

    private Connection cnx;

    public PanierService() {
        // Assuming you have a method to initialize your DB connection
        cnx = MyDataBase.getInstance().getCnx();
    }

    // Fetch all items in the cart
    public List<Panier> getCartItems() {
        List<Panier> paniers = new ArrayList<>();
        String query = "SELECT * FROM panier";

        try (Statement stmt = cnx.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Panier panier = new Panier();
                panier.setId_produit(rs.getInt("id_produit"));
                panier.setNbr_produit(rs.getInt("nbr_produit"));
                paniers.add(panier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paniers;
    }

    // Fetch product details by ID
    public Produit getProductById(int id_produit) {
        Produit produit = null;
        String query = "SELECT * FROM produit WHERE id_produit = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, id_produit);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                produit = new Produit();
                produit.setIdproduit(rs.getInt("id_produit"));
                produit.setNom(rs.getString("nom"));
                produit.setDescription(rs.getString("description"));
                produit.setCategorie(rs.getString("categorie"));  // Assuming categorie is stored as string
                produit.setPrix(rs.getFloat("prix"));
                produit.setImage(rs.getBlob("image"));
            }
        } catch (SQLException e) {
            System.out.println("Error getting product by ID: " + e.getMessage());
        }

        return produit;
    }

    // Fetch product price by ID
    public double getProductPrice(int id_produit) {
        double price = 0;
        String query = "SELECT prix FROM produit WHERE id_produit = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, id_produit);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                price = rs.getDouble("prix");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching product price: " + e.getMessage());
        }

        return price;
    }

    // Update or insert item in the cart
    public void addOrUpdateCartItem(int id_produit, int newQuantity, int id_user) {
        String checkQuery = "SELECT nbr_produit FROM panier WHERE id_produit = ? AND id_user = ?";
        String insertQuery = "INSERT INTO panier (id_produit, nbr_produit, id_user) VALUES (?, ?, ?)";
        String updateQuery = "UPDATE panier SET nbr_produit = ? WHERE id_produit = ? AND id_user = ?";

        try (PreparedStatement checkStmt = cnx.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, id_produit);
            checkStmt.setInt(2, id_user);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int currentQuantity = rs.getInt("nbr_produit");
                int newQuantityFinal = currentQuantity + newQuantity;

                try (PreparedStatement updateStmt = cnx.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, newQuantityFinal);
                    updateStmt.setInt(2, id_produit);
                    updateStmt.setInt(3, id_user);
                    updateStmt.executeUpdate();
                }
            } else {
                try (PreparedStatement insertStmt = cnx.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, id_produit);
                    insertStmt.setInt(2, newQuantity);
                    insertStmt.setInt(3, id_user);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding/updating cart item: " + e.getMessage());
        }
    }

    // Remove product from cart
    public void removeProductFromCart(int id_produit) {
        String query = "DELETE FROM panier WHERE id_produit = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, id_produit);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error removing product from cart: " + e.getMessage());
        }
    }

    public void updateCartQuantity(int id_produit, int newQuantity) {
        String query = "UPDATE panier SET nbr_produit = ? WHERE id_produit = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, id_produit);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating cart quantity: " + e.getMessage());
        }
    }
    public int getNewIdPanier() {
        // Assuming you have a database connection and a query to get the next id_panier value
        // This is a simplified example and may need to be adjusted based on your actual database schema
        String query = "SELECT id_panier FROM panier";
        try (Statement statement = cnx.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return 1; // If no rows are found, start with id_panier = 1
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}