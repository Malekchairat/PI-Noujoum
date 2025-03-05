package services;

import models.Favoris;
import tools.MyDataBase;

import java.sql.*;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> origin/GestionCommande
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FavorisService implements IService<Favoris> {
    Connection cnx = MyDataBase.getInstance().getCnx();

    @Override

    public void ajouter(Favoris f) {
        // Vérifier si le favori existe déjà pour le même user et produit
        if (favoriExiste(f.getIdUser(), f.getIdProduit())) {
            System.out.println("⚠ Ce favori existe déjà !");
            return;
        }

        // Set the current date automatically
        if (f.getDate() == null) {
            f.setDate(LocalDate.now());  // Set current date if not already set
        }

        String req = "INSERT INTO favoris (id_user, id_produit, date) VALUES (?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, f.getIdUser());
            pst.setInt(2, f.getIdProduit());
            pst.setDate(3, java.sql.Date.valueOf(f.getDate()));  // Use the current date if not provided
            pst.executeUpdate();
            System.out.println("✅ Favoris ajouté avec succès !");
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de l'ajout du favori : " + ex.getMessage());
        }
    }


    @Override
    public List<Favoris> recuperer() {
        List<Favoris> list = new ArrayList<>();
        String req = "SELECT * FROM favoris";
        try (PreparedStatement pst = cnx.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                LocalDate date = (rs.getDate("date") != null) ? rs.getDate("date").toLocalDate() : null;
                list.add(new Favoris(
                        rs.getInt("id_favoris"),
                        rs.getInt("id_produit"),
                        rs.getInt("id_user"),
                        date
                ));
            }
            System.out.println("✅ Nombre de favoris récupérés : " + list.size());
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de l'affichage des favoris : " + ex.getMessage());
        }
        return list;
    }


    @Override
    public void supprimer(int id) {
        String req = "DELETE FROM favoris WHERE id_favoris = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Favoris supprimé !");
            } else {
                System.out.println("⚠ Aucune entrée trouvée avec cet ID.");
            }
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de la suppression du favori : " + ex.getMessage());
        }
    }

    @Override

    public void modifier(Favoris f) {
        String req = "UPDATE favoris SET id_user = ?, id_produit = ?, date = ? WHERE id_favoris = ?";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, f.getIdUser());
            pst.setInt(2, f.getIdProduit());

            // Vérifier si la date est non nulle avant de l'insérer
<<<<<<< HEAD
=======
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
>>>>>>> origin/integration-branch
=======
>>>>>>> origin/GestionCommande
            if (f.getDate() != null) {
                pst.setDate(3, Date.valueOf(f.getDate()));
            } else {
                pst.setNull(3, Types.DATE);
            }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> origin/GestionCommande
            pst.setInt(4, f.getIdFavoris()); // ID du favori à modifier

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Favori modifié avec succès !");
            } else {
                System.out.println("⚠ Aucun favori trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la modification du favori : " + e.getMessage());
        }
    }

    // Vérifier si un produit existe
    public boolean produitExiste(int idProduit) {
        String req = "SELECT COUNT(*) FROM produit WHERE id_produit = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, idProduit);
            ResultSet rs = pst.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de la vérification du produit : " + ex.getMessage());
        }
        return false;
    }

    // Vérifier si un favori existe déjà pour un utilisateur et un produit
    public boolean favoriExiste(int idUser, int idProduit) {
        String req = "SELECT COUNT(*) FROM favoris WHERE id_user = ? AND id_produit = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, idUser);
            pst.setInt(2, idProduit);
            ResultSet rs = pst.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de la vérification du favori : " + ex.getMessage());
        }
        return false;
    }
}
<<<<<<< HEAD
=======
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
>>>>>>> origin/integration-branch
=======
>>>>>>> origin/GestionCommande
