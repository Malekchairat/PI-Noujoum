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
    public enum Categorie {
        albums, vetements, accesoires, lightsticks
    }

    // Constructeur principal

    public Produit(int idproduit, String nom, String description, Categorie categorie, float prix, int disponibilite, Blob image) {
        this.idproduit = idproduit;
        this.nom = nom;
        this.description = description;
        this.categorie = categorie;
        this.prix = prix;
        this.disponibilite = disponibilite;
        this.image = image;
        this.cnx = MyDataBase.getInstance().getConnection();
        this.promotion = loadPromotion();  // 🔴 Charger la promo dès la création
    }

    // Constructeur avec connexion
    public Produit(int idproduit, String nom, String description, Categorie categorie, float prix, int disponibilite, Blob image,  Connection cnx) {
        this.idproduit = idproduit;
        this.nom = nom;
        this.description = description;
        this.categorie = categorie;
        this.prix = prix;
        this.disponibilite = disponibilite;
        this.image = image;

        this.cnx = cnx;
    }


    private Promotion loadPromotion() {
        Promotion promo = null;
        try {
            String query = "SELECT * FROM promotion WHERE produitId = ?";
            PreparedStatement pst = cnx.prepareStatement(query);
            pst.setInt(1, this.idproduit);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                promo = new Promotion(
                        rs.getInt("idpromotion"),
                        rs.getString("code"),
                        rs.getFloat("pourcentage"),
                        rs.getString("expiration"),
                        this.idproduit
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return promo;
    }
    // Getters
    public int getIdproduit() { return idproduit; }
    public String getNom() { return nom; }
    public String getDescription() { return description; }
    public Categorie getCategorie() { return categorie; }
    public float getPrix() { return prix; }
    public int getDisponibilite() { return disponibilite; }
   // public int getPromotionid() { return promotionid; }
    public Blob getImage() { return image; }
    public Connection getCnx() { return cnx; }

    // Setters
    public void setNom(String nom) { this.nom = nom; }
    public void setDescription(String description) { this.description = description; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    public void setPrix(float prix) { this.prix = prix; }
    public void setDisponibilite(int disponibilite) { this.disponibilite = disponibilite; }
   // public void setPromotionid(int promotionid) { this.promotionid = promotionid; }
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
               // ", promotionid=" + promotionid +
                ", cnx=" + cnx +
                '}';
    }

    public Promotion getPromotion() {
        return promotion;
    }

    // ✅ Setter pour modifier la promotion
    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    // ✅ Modifier le prix en fonction de la promo (si applicable)
    public float getPrixAvecPromo() {
        if (promotion != null) {
            return prix - (prix * (promotion.getPourcentage() / 100));
        }
        return prix;
    }
}
