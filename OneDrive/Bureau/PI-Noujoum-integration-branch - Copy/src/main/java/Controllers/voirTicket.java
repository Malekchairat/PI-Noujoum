
package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import models.Evenement;
import models.Ticket;
import services.TicketService;
import java.io.IOException;
import java.util.List;

public class voirTicket {

    @FXML
    private VBox ticketContainer; // Conteneur principal des cartes de tickets

    private TicketService ticketService = new TicketService();

    @FXML
    public void initialize() {
        loadTickets();
    }

    private void loadTickets() {
        List<Ticket> tickets = ticketService.recuperer();
        System.out.println("🔍 Nombre de tickets trouvés: " + tickets.size());

        if (tickets.isEmpty()) {
            System.out.println("⚠️ Aucun ticket trouvé dans la base de données !");
            return;
        }

        for (Ticket ticket : tickets) {
            addTicketToContainer(ticket);
        }
    }

    private void addTicketToContainer(Ticket ticket) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ticketCard.fxml"));
            AnchorPane ticketCard = loader.load();

            // Récupérer le contrôleur de la carte du ticket
            TicketCardController controller = loader.getController();
            if (controller == null) {
                System.out.println("❌ Erreur : contrôleur de ticketCard.fxml est null !");
                return;
            }

            controller.setTicketData(ticket);
            ticketContainer.getChildren().add(ticketCard);
            System.out.println("🎟️ Ticket ajouté à l'affichage !");
        } catch (IOException e) {
            System.out.println("❌ Erreur lors du chargement de ticketCard.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }
}