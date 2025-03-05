package services;

import java.sql.SQLException;
import java.util.List;

public interface IFavorisService<T> {
    void ajouterFavoris(T t) throws SQLException;
    T recupererDernierFavoris(int id_user) throws SQLException;
    void modifierFavoris(T t) throws SQLException;
    void supprimerFavoris(int id_favoris) throws SQLException;
    List<T> recupererFavoris() throws SQLException;
}
