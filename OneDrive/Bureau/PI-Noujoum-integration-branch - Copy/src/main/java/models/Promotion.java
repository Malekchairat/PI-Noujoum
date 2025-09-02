package models;

import tools.MyDataBase;

import java.sql.Connection;

public class Promotion {
    private int idpromotion;
    private String code;
    private float pourcentage;
    private String expiration;
    private int produitid;
    private String produit;
    private Connection cnx;

    // Constructeur avec produitId et produit
    public Promotion(int idpromotion, String code, float pourcentage, String expiration, int produitId, String produit) {
        this.idpromotion = idpromotion;
        this.code = code;
        this.pourcentage = pourcentage;
        this.expiration = expiration;
        this.produitid = produitId;
        this.produit = produit;
        this.cnx = MyDataBase.getInstance().getCnx();
    }

    // Constructeur sans produit (si vous voulez l'ajouter plus tard)
    public Promotion(int idpromotion, String code, float pourcentage, String expiration) {
        this.idpromotion = idpromotion;
        this.code = code;
        this.pourcentage = pourcentage;
        this.expiration = expiration;
        this.cnx = MyDataBase.getInstance().getCnx();
    }

    // Constructeur utilisé dans le contrôleur pour ajouter une promotion
    public Promotion(int idpromotion, String promo, float percent, String date, int produitId) {
        this.idpromotion = idpromotion;
        this.code = promo;
        this.pourcentage = percent;
        this.expiration = date;
        this.produitid = produitId;
        this.cnx = MyDataBase.getInstance().getCnx();
    }

    // Getters
    public int getIdpromotion() { return idpromotion; }
    public String getCode() { return code; }
    public float getPourcentage() { return pourcentage; }
    public String getExpiration() { return expiration; }
    public int getProduitId() { return produitid; }
    public Connection getCnx() { return cnx; }

    public String getProduit() {
        return produit; // Retourne le nom du produit
    }

    // Setters
    public void setCode(String code) { this.code = code; }
    public void setPourcentage(float pourcentage) { this.pourcentage = pourcentage; }
    public void setExpiration(String expiration) { this.expiration = expiration; }
    public void setProduitId(int produitId) { this.produitid = produitId; }

    @Override
    public String toString() {
        return "Promotion{" +
                "idpromotion=" + idpromotion +
                ", code='" + code + '\'' +
                ", pourcentage=" + pourcentage +
                ", expiration='" + expiration + '\'' +
                ", produitId=" + produitid +
                ", cnx=" + cnx +
                '}';
    }
}
