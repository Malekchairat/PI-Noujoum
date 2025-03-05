package services;
import models.Produit;
import models.Favoris;
import tools.MyDataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FavorisService implements IService<Favoris> {
    Connection cnx = MyDataBase.getInstance().getCnx();
    public void supprimerFavori(int userId, int produitId) {
        String req = "DELETE FROM favoris WHERE id_user = ? AND id_produit = ?";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, userId);
            pst.setInt(2, produitId);
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Favori supprimé avec succès !");
            } else {
                System.out.println("⚠ Aucun favori trouvé pour cet utilisateur et produit.");
            }
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de la suppression du favori : " + ex.getMessage());
        }
    }

    public List<Produit> recupererFavoris(int userId) {
        List<Produit> produits = new ArrayList<>();
        String req = "SELECT p.* FROM produit p JOIN favoris f ON p.id_produit = f.id_produit WHERE f.id_user = ?";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Produit produit = new Produit(
                        rs.getInt("id_produit"),
                        rs.getString("nom"),
                        rs.getString("description"), // Ajouter ce champ si nécessaire
                        Produit.Categorie.valueOf(rs.getString("categorie")), // Conversion String → enum
                        rs.getFloat("prix"), // Utiliser le type float ici
                        rs.getInt("disponibilite"), // Utiliser int pour la disponibilité
                        rs.getBlob("image")
                );


                produits.add(produit);
            }
            System.out.println("✅ Favoris récupérés pour l'utilisateur " + userId);
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de la récupération des favoris : " + ex.getMessage());
        }

        return produits;
    }

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
            if (f.getDate() != null) {
                pst.setDate(3, Date.valueOf(f.getDate()));
            } else {
                pst.setNull(3, Types.DATE);
            }

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
