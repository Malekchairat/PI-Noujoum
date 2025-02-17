package controller;

import entity.Produit;
import tools.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ServicesCrud implements InterfaceServices<Produit> {
    private final Connection cnx;
    private PreparedStatement pst;

    public ServicesCrud() {
        this.cnx = DataSource.getInstance().getConnection();
    }

    @Override
    public void add(Produit produit) {
        // Use a prepared statement to avoid SQL injection
        String q1 = "INSERT INTO produit (nom, description, categorie, prix, disponibilite, image) VALUES (?, ?, ?, ?, ?,?)";

        try (PreparedStatement pst = cnx.prepareStatement(q1)) {
            // Set the parameters for the prepared statement
            pst.setString(1, produit.getNom());
            pst.setString(2, produit.getDescription());
            pst.setString(3, produit.getCategorie().name());
            pst.setFloat(4, produit.getPrix());
            pst.setInt(5, produit.getDisponibilite());
            pst.setBlob(6, produit.getImage());
            //pst.setInt(7, produit.getPromotionid());

            // Execute the query
            int x = pst.executeUpdate();
            System.out.println("Rows affected: " + x);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Produit produit) {
        String query = "UPDATE produit SET nom=?, description=?, categorie=?, prix=?, disponibilite=?, image=? WHERE idproduit=?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, produit.getNom());
            pst.setString(2, produit.getDescription());
            pst.setString(3, produit.getCategorie().toString());
            pst.setFloat(4, produit.getPrix());
            pst.setInt(5, produit.getDisponibilite());

            Blob imageBlob = produit.getImage();
            if (imageBlob != null) {
                pst.setBlob(6, imageBlob);
            } else {
                pst.setNull(6, java.sql.Types.BLOB);
            }

            pst.setInt(7, produit.getIdproduit());

            int rows = pst.executeUpdate();
            System.out.println("Produit mis à jour. Lignes affectées : " + rows);

        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du produit : " + e.getMessage());
        }
    }



    @Override
    public void delete(Produit produit) {
        String q1 = "DELETE FROM produit WHERE idproduit = ?";

        try {
            PreparedStatement pst = cnx.prepareStatement(q1);
            pst.setInt(1, produit.getIdproduit());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User deleted successfully! Rows affected: " + rowsAffected);
            } else {
                System.out.println("No user found with ID: " + produit.getIdproduit());
            }
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }


    @Override
    public Produit find(Produit produit) {
        // String concatenation for SQL select query
        String q1 = "SELECT * FROM produit WHERE idproduit = "
                + produit.getIdproduit();

        Produit result = null;

        try {
            Statement stmt = cnx.createStatement();
            ResultSet rs = stmt.executeQuery(q1);
            if (rs.next()) {
                result = new Produit(
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
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public List<Produit> findAll() {
        // String concatenation for SQL select query
        String q1 = "SELECT * FROM produit";
        List<Produit> resultList = new ArrayList<>();

        try {
            Statement stmt = cnx.createStatement();
            ResultSet rs = stmt.executeQuery(q1);
            while (rs.next()) {
                Produit per = new Produit(
                        rs.getInt("idproduit"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        Produit.Categorie.valueOf(rs.getString("categorie")),
                        rs.getFloat("prix"),
                        rs.getInt("disponibilite"),
                        rs.getBlob("image")

                );
                resultList.add(per);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return resultList;
    }
}
