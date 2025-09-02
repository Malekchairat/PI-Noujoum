package services;

import models.Evenement;
import models.Ticket;
import models.Type_P;
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketService implements IService<Ticket> {
    private final Connection cnx = MyDataBase.getInstance().getCnx();

    // ✅ Implémentation correcte de "ajouter(Ticket ticket)" pour respecter l'interface
    @Override
    public void ajouter(Ticket ticket) {
        String sql = "INSERT INTO ticket (id_evenement, id_utilisateur, prix, quantite, qr_code, methode_paiement) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement st = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, ticket.getIdEvenement());
            st.setInt(2, ticket.getIdUtilisateur());
            st.setFloat(3, ticket.getPrix());
            st.setInt(4, ticket.getQuantite());
            st.setString(5, ticket.getQrCode());
            st.setString(6, ticket.getMethodePaiement().name());

            st.executeUpdate();

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ticket.setIdTicket(generatedKeys.getInt(1));
                    System.out.println("✅ Ticket ajouté avec succès ! ID: " + ticket.getIdTicket());
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du ticket : " + e.getMessage());
        }
    }

    // ✅ Ajout d'une méthode spécifique pour ajouter un ticket avec un événement
    public void ajouterAvecEvenement(Evenement evenement, Ticket ticket) {
        ticket.setIdEvenement(evenement.getIdEvenement()); // Associe l'événement au ticket
        ajouter(ticket); // Réutilise la méthode existante
    }

    // ✅ Supprimer un ticket par ID
    @Override
    public void supprimer(int id) {
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
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    // ✅ Modifier un ticket
    @Override
    public void modifier(Ticket t) {
        if (t.getIdTicket() <= 0) {
            System.out.println("❌ Erreur : ID de ticket invalide.");
            return;
        }

        String sql = "UPDATE ticket SET prix = ?, quantite = ?, methode_paiement = ? WHERE id_ticket = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setFloat(1, t.getPrix());
            st.setInt(2, t.getQuantite());
            st.setString(3, t.getMethodePaiement().name());
            st.setInt(4, t.getIdTicket());

            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Ticket modifié avec succès !");
            } else {
                System.out.println("❌ La modification a échoué.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la modification : " + e.getMessage());
        }
    }

    // ✅ Récupérer tous les tickets
    @Override
    public List<Ticket> recuperer() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket";

        try (Statement statement = cnx.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Ticket ticket = new Ticket();
                ticket.setIdTicket(resultSet.getInt("id_ticket"));
                ticket.setIdEvenement(resultSet.getInt("id_evenement"));
                ticket.setIdUtilisateur(resultSet.getInt("id_utilisateur"));
                ticket.setPrix(resultSet.getFloat("prix"));
                ticket.setQuantite(resultSet.getInt("quantite"));
                ticket.setQrCode(resultSet.getString("qr_code"));
                ticket.setMethodePaiement(Type_P.valueOf(resultSet.getString("methode_paiement")));

                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération : " + e.getMessage());
        }

        return tickets;
    }
}
