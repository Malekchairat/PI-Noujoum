// Favoris.java
package models;

import java.util.Objects;
import java.time.LocalDate;

public class Favoris {
    private int idFavoris;
    private int idProduit;
    private int idUser;
    private LocalDate date;

    public Favoris(int idFavoris, int idProduit, int idUser, LocalDate date) {
        this.idFavoris = idFavoris;
        this.idProduit = idProduit;
        this.idUser = idUser;
        this.date = date;
    }

    public Favoris(int idProduit, int idUser, LocalDate date) {
        this.idProduit = idProduit;
        this.idUser = idUser;
        this.date = date;
    }

    public Favoris() {}

    public int getIdFavoris() { return idFavoris; }
    public void setIdFavoris(int idFavoris) { this.idFavoris = idFavoris; }

    public int getIdProduit() { return idProduit; }
    public void setIdProduit(int idProduit) { this.idProduit = idProduit; }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    @Override
    public int hashCode() {
        return Objects.hash(idFavoris, idProduit, idUser, date);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Favoris favoris = (Favoris) obj;
        return idFavoris == favoris.idFavoris && idProduit == favoris.idProduit && idUser == favoris.idUser && Objects.equals(date, favoris.date);
    }

    @Override
    public String toString() {
        return "Favoris{" +
                "idFavoris=" + idFavoris +
                ", idProduit=" + idProduit +
                ", idUser=" + idUser +
                ", date=" + date +
                '}';
    }
}
