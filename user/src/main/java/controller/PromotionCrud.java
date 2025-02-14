package controller;

import entity.Promotion;
import tools.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromotionCrud {
    private final Connection cnx;

    public PromotionCrud() {
        this.cnx = DataSource.getInstance().getConnection();
    }

    // Add a new promotion
    public void add(Promotion promotion) {
        String query = "INSERT INTO promotion (code, pourcentage, expiration) VALUES (?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, promotion.getCode());
            pst.setFloat(2, promotion.getPourcentage());
            pst.setString(3, promotion.getExpiration());

            int rows = pst.executeUpdate();
            System.out.println("Promotion added successfully. Rows affected: " + rows);
        } catch (SQLException e) {
            System.out.println("Error adding promotion: " + e.getMessage());
        }
    }

    // Update a promotion
    public void update(Promotion promotion) {
        String query = "UPDATE promotion SET code = ?, pourcentage = ?, expiration = ? WHERE idpromotion = ?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, promotion.getCode());
            pst.setFloat(2, promotion.getPourcentage());
            pst.setString(3, promotion.getExpiration());
            pst.setInt(4, promotion.getIdpromotion());

            int rows = pst.executeUpdate();
            System.out.println("Promotion updated successfully. Rows affected: " + rows);
        } catch (SQLException e) {
            System.out.println("Error updating promotion: " + e.getMessage());
        }
    }

    // Delete a promotion
    public void delete(int idpromotion) {
        String query = "DELETE FROM promotion WHERE idpromotion = ?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, idpromotion);

            int rows = pst.executeUpdate();
            System.out.println("Promotion deleted successfully. Rows affected: " + rows);
        } catch (SQLException e) {
            System.out.println("Error deleting promotion: " + e.getMessage());
        }
    }

    // Find a promotion by ID
    public Promotion find(int idpromotion) {
        String query = "SELECT * FROM promotion WHERE idpromotion = ?";

        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, idpromotion);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new Promotion(
                        rs.getInt("idpromotion"),
                        rs.getString("code"),
                        rs.getFloat("pourcentage"),
                        rs.getString("expiration")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error finding promotion: " + e.getMessage());
        }

        return null;
    }

    // Retrieve all promotions
    public List<Promotion> findAll() {
        List<Promotion> promotions = new ArrayList<>();
        String query = "SELECT * FROM promotion";

        try (Statement stmt = cnx.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                promotions.add(new Promotion(
                        rs.getInt("idpromotion"),
                        rs.getString("code"),
                        rs.getFloat("pourcentage"),
                        rs.getString("expiration")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving promotions: " + e.getMessage());
        }

        return promotions;
    }
}
