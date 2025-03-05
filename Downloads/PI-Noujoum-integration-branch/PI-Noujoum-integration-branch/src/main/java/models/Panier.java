package models;

public class Panier {

    private int id_panier;
    private int id_produit;
    private int id_user;
    private int nbr_produit;

    // Constructeur
    public Panier(int id_produit, int id_user, int nbr_produit) {
        this.id_produit = id_produit;
        this.id_user = id_user;
        this.nbr_produit = nbr_produit;
    }

    // Getters et Setters
    public int getId_panier() {
        return id_panier;
    }

    public void setId_panier(int id_panier) {
        this.id_panier = id_panier;
    }

    public int getId_produit() {
        return id_produit;
    }

    public void setId_produit(int id_produit) {
        this.id_produit = id_produit;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getNbr_produit() {
        return nbr_produit;
    }

    public void setNbr_produit(int nbr_produit) {
        this.nbr_produit = nbr_produit;
    }
}
