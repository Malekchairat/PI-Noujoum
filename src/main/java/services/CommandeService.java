package services;

import tools.MyDataBase;
import models.Commande;

<<<<<<< HEAD
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
<<<<<<< HEAD
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class CommandeService implements IService<Commande> {
=======
=======
import java.sql.*;
>>>>>>> origin/GestionCommande
import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
>>>>>>> origin/integration-branch
=======
public class CommandeService implements IService<Commande> {
>>>>>>> origin/GestionCommande
    Connection cnx = MyDataBase.getInstance().getCnx();

    public CommandeService() {
    }

    @Override
<<<<<<< HEAD
<<<<<<< HEAD
    public void ajouter(Commande t) {
        if (!isUserExists(t.getId_user())) {  // Check if the user exists
            System.out.println("❌ L'utilisateur avec l'ID " + t.getId_user() + " n'existe pas.");
            return;  // Exit the method if user does not exist
        }

        String sql = "INSERT INTO commande (id_panier, rue, ville, code_postal, etat, montant_total, methodePaiment, id_user) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
=======
    public void ajouter(Commande t) {
        if (!isUserExists(t.getId_user())) {  // Vérifier si l'utilisateur existe
            System.out.println("❌ L'utilisateur avec l'ID " + t.getId_user() + " n'existe pas.");
            return;  // Quitter la méthode si l'utilisateur n'existe pas
        }

        String sql = "INSERT INTO commande (id_panier, rue, ville, code_postal, etat, montant_total, methodePaiment, id_user, produit) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            // Assurez-vous que toutes les propriétés de t sont valides avant de les insérer
            if (t.getId_panier() <= 0 || t.getMontant_total() <= 0 || t.getMethodePaiment() == null || t.getMethodePaiment().isEmpty()) {
                System.out.println("❌ Données de commande invalides.");
                return;  // Ne pas insérer si les données sont invalides
            }

>>>>>>> origin/GestionCommande
            stm.setInt(1, t.getId_panier());
            stm.setString(2, t.getRue());
            stm.setString(3, t.getVille());
            stm.setString(4, t.getCode_postal());
            stm.setString(5, t.getEtat());
            stm.setFloat(6, t.getMontant_total());
            stm.setString(7, t.getMethodePaiment());
            stm.setInt(8, t.getId_user());
<<<<<<< HEAD

            stm.executeUpdate();
=======
    public void ajouter(Commande t) throws SQLException{
        try {
            String req = "INSERT INTO `commande`(`id_panier`, `rue`, `ville`, `code_postal`, `etat`, `montant_total`, `methodePaiment`, `id_user`) " +
                    "VALUES ('" + t.getId_panier() + "', '" + t.getRue() + "', '" + t.getVille() + "', '" + t.getCode_postal() + "', '"
                    + t.getEtat() + "', '" + t.getMontant_total() + "', '" + t.getMethodePaiment() + "', '" + t.getId_user() + "')";
            Statement stm = cnx.createStatement();
            stm.executeUpdate(req);
>>>>>>> origin/integration-branch
=======
            stm.setString(9, t.getProduit());

            stm.executeUpdate();
>>>>>>> origin/GestionCommande
            System.out.println("✅ Commande ajoutée avec succès !");
        } catch (SQLIntegrityConstraintViolationException ex) {
            System.out.println("❌ Erreur de contrainte d'intégrité : " + ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de l'ajout : " + ex.getMessage());
        }
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======

>>>>>>> origin/GestionCommande
    public boolean isPanierExists(int idPanier) {
        String sql = "SELECT COUNT(*) FROM panier WHERE id_panier = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, idPanier);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // Panier ID exists
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la vérification du panier ID : " + ex.getMessage());
        }
        return false; // Panier ID does not exist
    }

    public boolean isUserExists(int idUser) {
        String sql = "SELECT COUNT(*) FROM user WHERE id_user = ?"; // Updated to use the correct table name
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // User ID exists
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la vérification du user ID : " + ex.getMessage());
        }
        return false; // User ID does not exist
    }


<<<<<<< HEAD
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

    @Override
    public void modifier(Commande commande) {
=======
    @Override
    public void modifier(Commande commande, String var2) throws SQLException {
>>>>>>> origin/integration-branch
        String sql = "UPDATE commande SET id_panier = ?, rue = ?, ville = ?, code_postal = ?, etat = ?, montant_total = ?, methodePaiment = ?, id_user = ? WHERE commande_id = ?";
=======
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

    @Override
    public void modifier(Commande commande) {
        String sql = "UPDATE commande SET id_panier = ?, rue = ?, ville = ?, code_postal = ?, etat = ?, montant_total = ?, methodePaiment = ?, id_user = ?,produit = ? WHERE commande_id = ?";
>>>>>>> origin/GestionCommande
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, commande.getId_panier());
            stmt.setString(2, commande.getRue());
            stmt.setString(3, commande.getVille());
            stmt.setString(4, commande.getCode_postal());
            stmt.setString(5, commande.getEtat());
            stmt.setFloat(6, commande.getMontant_total());
            stmt.setString(7, commande.getMethodePaiment());
            stmt.setInt(8, commande.getId_user());
<<<<<<< HEAD
            stmt.setInt(9, commande.getCommande_id());
<<<<<<< HEAD

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Commande mise à jour avec succès.");
            } else {
                System.out.println("❌ Aucune commande trouvée avec cet ID.");
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la mise à jour de la commande : " + ex.getMessage());
=======
=======
            stmt.setString(9, commande.getProduit());
            stmt.setInt(10, commande.getCommande_id());


>>>>>>> origin/GestionCommande
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Commande mise à jour avec succès.");
            } else {
                System.out.println("❌ Aucune commande trouvée avec cet ID.");
            }
        } catch (SQLException ex) {
<<<<<<< HEAD
            System.err.println("Erreur lors de la mise à jour de la commande : " + ex.getMessage());
            throw ex;
        }
    }


    @Override

    public void supprimer(Commande commande) {
        String sql = "DELETE FROM commande WHERE commande_id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, commande.getCommande_id());
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Commande supprimée avec succès.");
            } else {
                System.out.println("Aucune commande trouvée avec cet ID.");
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la suppression de la commande : " + ex.getMessage());
>>>>>>> origin/integration-branch
=======
            System.err.println("❌ Erreur lors de la mise à jour de la commande : " + ex.getMessage());
>>>>>>> origin/GestionCommande
        }
    }

    @Override
<<<<<<< HEAD
<<<<<<< HEAD
    public List<Commande> recuperer() {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande";

        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(sql)) {
=======
    public List<Commande> recuperer() throws SQLException {
=======
    public List<Commande> recuperer() {
>>>>>>> origin/GestionCommande
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande";

<<<<<<< HEAD
>>>>>>> origin/integration-branch
=======
        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(sql)) {
>>>>>>> origin/GestionCommande
            while (rs.next()) {
                Commande c = new Commande();
                c.setCommande_id(rs.getInt("commande_id"));
                c.setId_panier(rs.getInt("id_panier"));
                c.setRue(rs.getString("rue"));
                c.setVille(rs.getString("ville"));
                c.setCode_postal(rs.getString("code_postal"));
                c.setEtat(rs.getString("etat"));
<<<<<<< HEAD
<<<<<<< HEAD
                c.setMontant_total(rs.getFloat("montant_total"));
                c.setMethodePaiment(rs.getString("methodePaiment"));
=======
                c.setMontant_total((float) rs.getDouble("montant_total"));
                c.setMethodePaiment(rs.getString("MethodePaiment"));
>>>>>>> origin/integration-branch
=======
                c.setMontant_total(rs.getFloat("montant_total"));
                c.setMethodePaiment(rs.getString("methodePaiment"));
>>>>>>> origin/GestionCommande
                c.setId_user(rs.getInt("id_user"));
                c.setProduit(rs.getString("produit"));
                commandes.add(c);
            }
        } catch (SQLException ex) {
<<<<<<< HEAD
<<<<<<< HEAD
            System.out.println("❌ Erreur lors de la récupération des commandes : " + ex.getMessage());
=======
            System.out.println("❌ Erreur lors de la récupération : " + ex.getMessage());
>>>>>>> origin/integration-branch
=======
            System.out.println("❌ Erreur lors de la récupération des commandes : " + ex.getMessage());
>>>>>>> origin/GestionCommande
        }

        return commandes;
    }
}
