//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tests;

<<<<<<< HEAD
<<<<<<< HEAD
=======
import java.sql.SQLException;
import java.util.List;


import models.Panier;
import models.Personne;
import services.PersonneService;
import services.PanierService;
import services.CommandeService;
import models.Commande;


import services.PanierService;
import models.Panier;
import java.sql.SQLException;

>>>>>>> origin/integration-branch
=======
>>>>>>> origin/GestionCommande
//modifier+supprimer panier
/*
public class main {
    public static void main(String[] args) {
        // Création d'une instance de PanierService
        PanierService panierService = new PanierService();

        // Test de la méthode modifier
        try {
            // Création d'un objet Panier à modifier
            Panier panierToUpdate = new Panier();
            panierToUpdate.setId_panier(3); // ID du panier à modifier
            panierToUpdate.setId_produit(100); // Nouveau ID de produit
            panierToUpdate.setNbr_produit(20); // Nouvelle quantité de produit

            // Appel de la méthode modifier
            panierService.modifier(panierToUpdate, "modifier");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Test de la méthode supprimer
        try {
            // Création d'un objet Panier à supprimer
            Panier panierToDelete = new Panier();
            panierToDelete.setId_panier(6); // ID du panier à supprimer

            // Appel de la méthode supprimer
            panierService.supprimer(panierToDelete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
//ajouter+afficher panier

public class main {
    public static void main(String[] args) {
        // Créer un service pour gérer les paniers
        PanierService panierService = new PanierService();

        // Créer un nouveau panier et l'ajouter
        Panier panier = new Panier();
        panier.setId_produit(10); // Remplace avec un ID valide existant dans ta table "produit"
        panier.setId_user(10);    // Remplace avec un ID valide existant dans ta table "user"
        panier.setNbr_produit(100);

        try {
            // Ajouter un panier
            panierService.ajouter(panier);
            System.out.println("✅ Panier ajouté avec succès !");

            // Afficher tous les paniers
            System.out.println("📌 Liste des paniers :");
            List<Panier> paniers = panierService.recuperer();
            for (Panier p : paniers) {
                System.out.println("🛒 Panier ID: " + p.getId_panier() +
                        ", Produit ID: " + p.getId_produit() +
                        ", User ID: " + p.getId_user() +
                        ", Quantité: " + p.getNbr_produit());
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
        
    }
}


//ajouter+afficher commande
public class main {
    public static void main(String[] args) {
        // Création du service Commande
        CommandeService commandeService = new CommandeService();

        // Création d'une nouvelle commande
        Commande nouvelleCommande = new Commande();
        nouvelleCommande.setId_panier(5);  // Assurez-vous que cet ID existe dans la table panier
        nouvelleCommande.setRue("Rue Ghana");
        nouvelleCommande.setVille("Wardeya");
        nouvelleCommande.setCode_postal("2000");
        nouvelleCommande.setEtat("Tunis");
        nouvelleCommande.setMontant_total(19.999); // Vérifiez que la méthode accepte bien un float
        nouvelleCommande.setMethodePaiment("Carte bancaire");
        nouvelleCommande.setId_user(1);  // Assurez-vous que cet ID existe dans la table utilisateur

        try {
            // Ajouter la commande à la base de données
            commandeService.ajouter(nouvelleCommande);
            System.out.println("Commande ajoutée avec succès !");

            // Récupérer et afficher toutes les commandes
            List<Commande> commandes = commandeService.recuperer();
            System.out.println("Liste des commandes :");
            for (Commande c : commandes) {
                System.out.println(c);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
    }
}


//modifier+supprimer commande
public class main {
    public static void main(String[] args) {
        // Création d'une instance de CommandeService
        CommandeService commandeService = new CommandeService();

        // Test de la méthode modifier
        try {
            // Création d'une commande à modifier
            Commande commandeToUpdate = new Commande();
            commandeToUpdate.setCommande_id(1);  // Remplacez par l'ID de la commande à modifier
            commandeToUpdate.setId_panier(1);  // Nouvel ID panier
            commandeToUpdate.setRue("Rue Mozambique");
            commandeToUpdate.setVille("Boumhal");
            commandeToUpdate.setCode_postal("2050");
            commandeToUpdate.setEtat("Ben Arous");
            commandeToUpdate.setMontant_total(2.5);
            commandeToUpdate.setMethodePaiment("Carte bancaire");
            commandeToUpdate.setId_user(10);  // ID de l'utilisateur

            // Appel de la méthode modifier
            commandeService.modifier(commandeToUpdate, "Test modification");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la commande : " + e.getMessage());
        }

        // Test de la méthode supprimer
        try {
            // Création d'une commande à supprimer
            Commande commandeToDelete = new Commande();
            commandeToDelete.setCommande_id(3);  // Remplacez par l'ID de la commande à supprimer

            // Appel de la méthode supprimer
            commandeService.supprimer(commandeToDelete);
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de la commande : " + e.getMessage());
        }
    }
}
*/


