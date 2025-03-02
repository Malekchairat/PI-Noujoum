package models;

import tools.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Panier {
    private int id_panier;
    private int id_produit;
    private int id_user;
    private int nbr_produit;
    private Connection connection;
    public Panier() {}

    public Panier(int id_panier, int id_produit, int id_user, int nbr_produit) {
        this.id_panier = id_panier;
        this.id_produit = id_produit;
        this.id_user = id_user;
        this.nbr_produit = nbr_produit;
        this.connection = MyDataBase.getInstance().getCnx();

    }

    public Panier(int id_produit, int id_user, int nbr_produit) {
        this.id_produit = id_produit;
        this.id_user = id_user;
        this.nbr_produit = nbr_produit;
    }


    public int getId_panier() { return id_panier; }
    public void setId_panier(int id_panier) { this.id_panier = id_panier; }

    public int getId_produit() { return id_produit; }
    public void setId_produit(int id_produit) { this.id_produit = id_produit; }

    public int getId_user() { return id_user; }
    public void setId_user(int id_user) { this.id_user = id_user; }

    public int getNbr_produit() { return nbr_produit; }
    public void setNbr_produit(int nbr_produit) { this.nbr_produit = nbr_produit; }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public String toString() {
        return "Panier{" +
                "id_panier=" + id_panier +
                ", id_produit=" + id_produit +
                ", id_user=" + id_user +
                ", nbr_produit=" + nbr_produit +
                '}';
    }
    public double getPrice() {
        String query = "SELECT prix FROM produit WHERE id_produit = ?";
        double price = 0;

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, this.id_produit);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                price = rs.getDouble("price");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving product price: " + e.getMessage());
        }

        return price;
    }
    public double getSubtotal() {
        return getPrice() * nbr_produit;
    }

}
