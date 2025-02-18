package models;

public class Panier {
    private int id_panier;
    private int id_produit;
    private int id_user;
    private int nbr_produit;

    public Panier() {
    }

    public Panier(int id_produit, int nbr_produit) {
        this.id_produit = id_produit;
        this.nbr_produit = nbr_produit;
    }

    public Panier(int id_panier, int id_produit, int nbr_produit) {
        this.id_panier = id_panier;
        this.id_produit = id_produit;
        this.nbr_produit = nbr_produit;
    }

    public int getId_panier() {
        return this.id_panier;
    }

    public void setId_panier(int id_panier) {
        this.id_panier = id_panier;
    }

    public int getId_produit() {
        return this.id_produit;
    }

    public void setId_produit(int id_produit) {
        this.id_produit = id_produit;
    }

    public int getId_user() {
        return this.id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getNbr_produit() {
        return this.nbr_produit;
    }

    public void setNbr_produit(int nbr_produit) {
        this.nbr_produit = nbr_produit;
    }

    @Override
    public String toString() {
        return "Panier{id_panier=" + this.id_panier + ", id_produit=" + this.id_produit + ", id_user=" + this.id_user + ", nbr_produit=" + this.nbr_produit + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.id_panier;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Panier other = (Panier) obj;
            return this.id_panier == other.id_panier;
        }
    }
}