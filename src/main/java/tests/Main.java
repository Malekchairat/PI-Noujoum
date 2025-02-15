//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tests;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import models.Evenement;
import models.Ticket;
import models.Type_P;
import models.Type_e;
import services.EvenementService;
import services.TicketService;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        EvenementService evenementService = new EvenementService();
        TicketService ticketService = new TicketService();

        try {
            Evenement evenement = new Evenement("Paris", "DJ Snake", "Soirée électro", new Date(), new Date(), 20, 50.0F, Type_e.CONCERT, 200);
            evenementService.ajouter(evenement);
            System.out.println("✅ 1 evenement ajouté avec succès !");
            List<Evenement> evenements = evenementService.recuperer();
            if (evenements.isEmpty()) {
                System.out.println("❌ Aucun événement trouvé.");
                return;
            }

            Ticket ticket1 = new Ticket(evenement, 1, 99.99F, "QR123456", Type_P.CASH);
            Ticket ticket2 = new Ticket(evenement, 2, 79.99F, "QR789012", Type_P.CREDIT_CARD);
            Ticket ticket3 = new Ticket(evenement, 3, 119.99F, "QR345678", Type_P.PAYPAL);
            ticketService.ajouter(evenement, ticket1);
            ticketService.ajouter(evenement, ticket2);
            ticketService.ajouter(evenement, ticket3);
            System.out.println("✅ 3 Tickets ajoutés avec succès pour l'événement : " + evenement.getArtist());
            ticket2.setPrix(89.99F);
            ticket2.setMethodePaiement(Type_P.CREDIT_CARD);
            ticketService.modifier(ticket2);
            System.out.println("✅ Ticket modifié avec succès (ID: " + ticket2.getIdTicket() + ")");
            ticketService.supprimer(ticket3);
            System.out.println("✅ Ticket supprimé avec succès (ID: " + ticket3.getIdTicket() + ")");
            System.out.println("Liste des événements :");
            Iterator var8 = evenements.iterator();

            PrintStream var10000;
            int var10001;
            while(var8.hasNext()) {
                Evenement e = (Evenement)var8.next();
                var10000 = System.out;
                var10001 = e.getIdEvenement();
                var10000.println("ID: " + var10001 + ", Nom: " + e.getArtist());
            }

            List<Ticket> tickets = ticketService.recuperer();
            System.out.println("\nListe des tickets :");
            Iterator var16 = tickets.iterator();

            while(var16.hasNext()) {
                Ticket ticket = (Ticket)var16.next();
                var10000 = System.out;
                var10001 = ticket.getIdTicket();
                var10000.println("ID Ticket: " + var10001 + ", Événement ID: " + ticket.getEvenement().getIdEvenement() + ", Prix: " + ticket.getPrix() + ", Méthode de Paiement: " + String.valueOf(ticket.getMethodePaiement()));
            }
        } catch (SQLException var11) {
            SQLException e = var11;
            System.err.println("❌ Erreur SQL : " + e.getMessage());
        } catch (Exception var12) {
            Exception e = var12;
            System.err.println("❌ Erreur : " + e.getMessage());
        }

    }
}
