package models;

import java.sql.Blob;

public class Produit {
    private int idproduit;
    private String nom;
    private String description;
    private Categorie categorie;
    private float prix;
    private int disponibilite;
    private Blob image;

<<<<<<< HEAD
    // Variable statique pour stocker l'ID du produit sélectionné
    private static int selectedProduitId;

    // Enum pour les catégories
=======
    // Enum pour les catégories

>>>>>>> origin/GestionCommande
    public enum Categorie {
        ELECTRONIQUE, VETEMENTS, ALIMENTATION, AUTRE;

        public static Categorie fromString(String categorieStr) {
            if (categorieStr == null) return Categorie.AUTRE;
            try {
                return Categorie.valueOf(categorieStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                return Categorie.AUTRE; // Valeur par défaut en cas d'erreur
            }
        }
    }
<<<<<<< HEAD

=======
    public Produit(){}
>>>>>>> origin/GestionCommande
    // Constructeur
    public Produit(int idproduit, String nom, String description, String categorie, float prix, int disponibilite, Blob image) {
        this.idproduit = idproduit;
        this.nom = nom;
        this.description = description;
        this.categorie = Categorie.fromString(categorie); // Conversion sécurisée
        this.prix = prix;
        this.disponibilite = disponibilite;
        this.image = image;
    }

    // Getters et setters
    public int getIdproduit() {
        return idproduit;
    }

    public void setIdproduit(int idproduit) {
        this.idproduit = idproduit;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = Categorie.fromString(categorie);
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(int disponibilite) {
        this.disponibilite = disponibilite;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }
<<<<<<< HEAD

    // Méthodes pour stocker l'ID du produit sélectionné
    public static void setProduitId(int id) {
        selectedProduitId = id;
    }

    public static int getProduitId() {
        return selectedProduitId;
    } public static int produitId; // Variable statique pour stocker l'ID sélectionné


}

=======
}
>>>>>>> origin/GestionCommande
