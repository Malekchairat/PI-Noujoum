package services;

import tools.MyDataBase;
import models.Commande;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;

public class CommandeService implements IService<Commande> {
    Connection cnx = MyDataBase.getInstance().getCnx();

    public CommandeService() {
    }

    // ✅ Correction : Suppression de "throws SQLException" pour respecter IService<T>
    @Override
    public void ajouter(Commande t) {
        String sql = "INSERT INTO commande (id_panier, rue, ville, code_postal, etat, montant_total, methodePaiment, id_user) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setInt(1, t.getId_panier());
            stm.setString(2, t.getRue());
            stm.setString(3, t.getVille());
            stm.setString(4, t.getCode_postal());
            stm.setString(5, t.getEtat());
            stm.setFloat(6, t.getMontant_total());
            stm.setString(7, t.getMethodePaiment());
            stm.setInt(8, t.getId_user());

            stm.executeUpdate();
            System.out.println("✅ Commande ajoutée avec succès !");
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de l'ajout : " + ex.getMessage());
        }
    }

    // ✅ Correction : IService<T> attend "supprimer(int id)", pas un objet Commande
    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM commande WHERE commande_id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Commande supprimée avec succès.");
            } else {
                System.out.println("❌ Aucune commande trouvée avec cet ID.");
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la suppression de la commande : " + ex.getMessage());
        }
    }

    // ✅ Correction : Enlever "throws SQLException" et gérer exceptions en interne
    @Override
    public void modifier(Commande commande) {
        String sql = "UPDATE commande SET id_panier = ?, rue = ?, ville = ?, code_postal = ?, etat = ?, montant_total = ?, methodePaiment = ?, id_user = ? WHERE commande_id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, commande.getId_panier());
            stmt.setString(2, commande.getRue());
            stmt.setString(3, commande.getVille());
            stmt.setString(4, commande.getCode_postal());
            stmt.setString(5, commande.getEtat());
            stmt.setFloat(6, commande.getMontant_total());
            stmt.setString(7, commande.getMethodePaiment());
            stmt.setInt(8, commande.getId_user());
            stmt.setInt(9, commande.getCommande_id());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Commande mise à jour avec succès.");
            } else {
                System.out.println("❌ Aucune commande trouvée avec cet ID.");
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la mise à jour de la commande : " + ex.getMessage());
        }
    }

    // ✅ Correction : Enlever "throws SQLException" et gérer exceptions en interne
    @Override
    public List<Commande> recuperer() {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande";

        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                Commande c = new Commande();
                c.setCommande_id(rs.getInt("commande_id"));
                c.setId_panier(rs.getInt("id_panier"));
                c.setRue(rs.getString("rue"));
                c.setVille(rs.getString("ville"));
                c.setCode_postal(rs.getString("code_postal"));
                c.setEtat(rs.getString("etat"));
                c.setMontant_total(rs.getFloat("montant_total"));
                c.setMethodePaiment(rs.getString("methodePaiment"));
                c.setId_user(rs.getInt("id_user"));
                commandes.add(c);
            }
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de la récupération des commandes : " + ex.getMessage());
        }

        return commandes;
    }
}
