package models;

import tools.MyDataBase;
import java.sql.*;

public class Produit {
    private int idproduit;
    private String nom;
    private String description;
    private Categorie categorie;
    private float prix;
    private int disponibilite;
    private Blob image;
    private Connection cnx;
    private Promotion promotion;

    // Enum Categorie
    public enum Categorie {
        ELECTRONIQUE, VETEMENTS, ALIMENTATION, AUTRE, ALBUM;

        // Method to convert String to Categorie enum
        public static Categorie fromString(String categorieStr) {
            if (categorieStr == null) return Categorie.AUTRE; // Default to AUTRE if the input is null
            try {
                // Convert string to uppercase to handle case insensitivity
                return Categorie.valueOf(categorieStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Log the error (optional) and return a default category (AUTRE)
                System.out.println("Unknown category: " + categorieStr);
                return Categorie.AUTRE; // Default category in case of an invalid string
            }
        }
    }

    // Constructor with Categorie enum
    public Produit(int idproduit, String nom, String description, Categorie categorie, float prix, int disponibilite, Blob image) {
        this.idproduit = idproduit;
        this.nom = nom;
        this.description = description;
        this.categorie = categorie;
        this.prix = prix;
        this.disponibilite = disponibilite;
        this.image = image;
        this.cnx = MyDataBase.getInstance().getCnx();
    }

    // Getter and Setter methods
    public int getIdproduit() { return idproduit; }
    public String getNom() { return nom; }
    public String getDescription() { return description; }
    public Categorie getCategorie() { return categorie; }
    public float getPrix() { return prix; }
    public int getDisponibilite() { return disponibilite; }
    public Blob getImage() { return image; }
    public Connection getCnx() { return cnx; }

    public void setNom(String nom) { this.nom = nom; }
    public void setDescription(String description) { this.description = description; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    public void setPrix(float prix) { this.prix = prix; }
    public void setDisponibilite(int disponibilite) { this.disponibilite = disponibilite; }
    public void setImage(Blob image) { this.image = image; }

    @Override
    public String toString() {
        return "Produit{" +
                "idproduit=" + idproduit +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", categorie=" + categorie +
                ", prix=" + prix +
                ", disponibilite=" + disponibilite +
                ", image=" + image +
                '}';
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public float getPrixAvecPromo() {
        if (promotion != null) {
            return prix - (prix * (promotion.getPourcentage() / 100));
        }
        return prix;
    }
}
