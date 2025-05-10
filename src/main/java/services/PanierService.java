package services;

import models.Panier;
import models.Produit;
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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PanierService {

    private Connection cnx;

    public PanierService() {
        // Assuming you have a method to initialize your DB connection
        cnx = MyDataBase.getInstance().getCnx();
    }

<<<<<<< HEAD
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
=======
    // Fetch all items in the cart
    public List<Panier> getCartItems() {
>>>>>>> origin/GestionCommande
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
>>>>>>> origin/integration-branch
