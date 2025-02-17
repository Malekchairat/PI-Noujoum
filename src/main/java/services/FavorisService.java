package services;

import models.Favoris;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavorisService implements IFavorisService<Favoris> {

    private final Connection cnx;

    public FavorisService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    @Override
    public void ajouterFavoris(Favoris f) throws SQLException {
        String req = "INSERT INTO favoris (id_user, id_produit, date) VALUES (?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, f.getIdUser());
            pst.setInt(2, f.getIdProduit());

            // Ensure date is not null before inserting
            if (f.getDate() != null) {
                pst.setDate(3, Date.valueOf(f.getDate()));
            } else {
                pst.setNull(3, Types.DATE);
            }

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        f.setIdFavoris(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }


    @Override
    public Favoris recupererDernierFavoris(int id_user) throws SQLException {
        String req = "SELECT * FROM favoris WHERE id_user = ? ORDER BY id_favoris DESC LIMIT 1";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, id_user);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Favoris(
                            rs.getInt("id_favoris"),
                            rs.getInt("id_produit"),
                            rs.getInt("id_user"),
                            rs.getDate("date").toLocalDate()

                    );
                }
            }
        }
        return null;
    }

    @Override
    public void modifierFavoris(Favoris f) throws SQLException {
        String req = "UPDATE favoris SET id_produit = ?, date = ? WHERE id_favoris = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, f.getIdProduit());
            pst.setDate(2, Date.valueOf(f.getDate()));
            pst.setInt(3, f.getIdFavoris());

            pst.executeUpdate();
        }
    }

    @Override
    public void supprimerFavoris(int id_favoris) throws SQLException {
        String req = "DELETE FROM favoris WHERE id_favoris = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, id_favoris);
            pst.executeUpdate();
        }
    }

    @Override
    public List<Favoris> recupererFavoris() throws SQLException {
        List<Favoris> favorisList = new ArrayList<>();
        String req = "SELECT * FROM favoris";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Favoris f = new Favoris(
                        rs.getInt("id_favoris"),
                        rs.getInt("id_produit"),
                        rs.getInt("id_user"),
                        rs.getDate("date").toLocalDate()

                );
                favorisList.add(f);
            }
        }
        return favorisList;
    }
}