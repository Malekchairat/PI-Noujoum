package services;

import models.Evenement;
import models.Ticket;
import models.Type_P;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketService implements ITicketService {
    private final Connection cnx = MyDataBase.getInstance().getCnx();

    // ✅ Correction : Ajout de la colonne "quantite" dans l'insertion
    public void ajouter(Evenement evenement, Ticket ticket) throws SQLException {
        String sql = "INSERT INTO ticket (id_evenement, id_utilisateur, prix, quantite, qr_code, methode_paiement) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement st = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, evenement.getIdEvenement());  // ✅ Correct
            st.setInt(2, ticket.getIdUtilisateur());   // ✅ Correct
            st.setFloat(3, ticket.getPrix());          // ✅ Correction ici
            st.setInt(4, ticket.getQuantite());        // ✅ Ajout de la quantité
            st.setString(5, ticket.getQrCode());       // ✅ Correct
            st.setString(6, ticket.getMethodePaiement().name()); // ✅ Correct

            st.executeUpdate();

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ticket.setIdTicket(generatedKeys.getInt(1));
                    System.out.println("✅ Ticket ajouté avec succès ! ID: " + ticket.getIdTicket() + ", Quantité: " + ticket.getQuantite());
                }
            }
        }
    }

    // ✅ Correction : Suppression d'un ticket
    public void supprimer(int id) throws SQLException {
        if (id <= 0) {
            System.out.println("❌ Erreur : ID de ticket invalide.");
            return;
        }

        String sql = "DELETE FROM ticket WHERE id_ticket = ?";
        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Ticket supprimé avec succès !");
            } else {
                System.out.println("❌ Aucun ticket trouvé avec cet ID.");
            }
        }
    }

    // ✅ Correction : Modification de la quantité possible
    public void modifier(Ticket t) throws SQLException {
        if (t.getIdTicket() <= 0) {
            System.out.println("❌ Erreur : ID de ticket invalide.");
            return;
        }

        String sql = "UPDATE ticket SET prix = ?, quantite = ?, methode_paiement = ? WHERE id_ticket = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setFloat(1, t.getPrix());
            st.setInt(2, t.getQuantite()); // ✅ Ajout de la quantité dans la modification
            st.setString(3, t.getMethodePaiement().name());
            st.setInt(4, t.getIdTicket());

            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Ticket modifié avec succès !");
            } else {
                System.out.println("❌ La modification a échoué.");
            }
        }
    }

    // ✅ Correction : Récupération de la quantité
    public List<Ticket> recuperer() throws SQLException {
        String sql = "SELECT * FROM ticket";
        List<Ticket> tickets = new ArrayList<>();

        try (Statement statement = cnx.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Ticket ticket = new Ticket();
                ticket.setIdTicket(resultSet.getInt("id_ticket"));
                ticket.setIdEvenement(resultSet.getInt("id_evenement"));
                ticket.setIdUtilisateur(resultSet.getInt("id_utilisateur"));
                ticket.setPrix(resultSet.getFloat("prix"));
                ticket.setQuantite(resultSet.getInt("quantite")); // ✅ Ajout de la récupération de la quantité
                ticket.setQrCode(resultSet.getString("qr_code"));
                ticket.setMethodePaiement(Type_P.valueOf(resultSet.getString("methode_paiement")));

                tickets.add(ticket);
            }
        }

        return tickets;
    }
}
