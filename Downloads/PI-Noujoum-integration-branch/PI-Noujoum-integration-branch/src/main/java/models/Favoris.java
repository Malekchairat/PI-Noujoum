package models;

import java.util.Objects;
import java.time.LocalDate;
public class Favoris {

    private int id_favoris;
    private int id_produit;
    private int id_user;
    private LocalDate date;

    public Favoris(int id_favoris, int id_produit, int id_user, LocalDate date) {
        this.id_favoris = id_favoris;
        this.id_produit = id_produit;
        this.id_user = id_user;
        this.date = date;
    }


    public Favoris(int id_produit, int id_user, LocalDate date) {
        this.id_produit = id_produit;
        this.id_user = id_user;
        this.date = date;
    }


    public Favoris() {}

    public int getIdFavoris() {
        return id_favoris;
    }

    public void setIdFavoris(int id_favoris) {
        this.id_favoris = id_favoris;
    }

    public int getIdProduit() {
        return id_produit;
    }

    public void setIdProduit(int id_produit) {
        this.id_produit = id_produit;
    }

    public int getIdUser() {
        return id_user;
    }

    public void setIdUser(int id_user) {
        this.id_user = id_user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id_favoris, id_produit, id_user, date);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Favoris favoris = (Favoris) obj;
        return id_favoris == favoris.id_favoris &&
                id_produit == favoris.id_produit &&
                id_user == favoris.id_user &&
                Objects.equals(date, favoris.date);
    }


    @Override
    public String toString() {
        return "Favoris{" +
                "id_favoris=" + id_favoris +
                ", id_produit=" + id_produit +
                ", id_user=" + id_user +
                ", date='" + date + '\'' +
                '}';
    }
}
