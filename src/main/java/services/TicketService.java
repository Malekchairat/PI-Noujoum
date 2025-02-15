//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.Evenement;
import models.Ticket;
import models.Type_P;
import tools.MyDataBase;

public class TicketService implements ITicketService {
    private Connection cnx = MyDataBase.getInstance().getCnx();

    public TicketService() {
    }

    public void ajouter(Evenement evenement, Ticket ticket) throws SQLException {
        String checkEventSql = "SELECT COUNT(*) FROM evenement WHERE id_evenement = ?";
        PreparedStatement checkEventStmt = this.cnx.prepareStatement(checkEventSql);
        checkEventStmt.setInt(1, evenement.getIdEvenement());
        ResultSet resultSet = checkEventStmt.executeQuery();
        resultSet.next();
        if (resultSet.getInt(1) == 0) {
            System.out.println("L'événement avec l'ID " + evenement.getIdEvenement() + " n'existe pas.");
        } else {
            String sql = "INSERT INTO ticket (id_evenement, id_utilisateur, prix, qr_code, methode_paiement) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement st = this.cnx.prepareStatement(sql, 1);
            st.setInt(1, evenement.getIdEvenement());
            st.setInt(2, ticket.getIdUtilisateur());
            st.setFloat(3, ticket.getPrix());
            st.setString(4, ticket.getQrCode());
            st.setString(5, ticket.getMethodePaiement().name());
            st.executeUpdate();
            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                ticket.setIdTicket(generatedKeys.getInt(1));
            }

            System.out.println("✅ Ticket ajouté avec succès ! ID: " + ticket.getIdTicket());
        }
    }

    public void supprimer(Ticket t) throws SQLException {
        int id = t.getIdTicket();
        System.out.println("\ud83d\udd04 Tentative de suppression du ticket avec ID: " + id);
        if (id <= 0) {
            System.out.println("❌ Erreur : ID de ticket invalide.");
        } else {
            String sql = "DELETE FROM ticket WHERE id_ticket = ?";
            PreparedStatement statement = this.cnx.prepareStatement(sql);
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Ticket supprimé avec succès !");
            } else {
                System.out.println("❌ Aucun ticket trouvé avec cet ID.");
            }

        }
    }

    public void modifier(Ticket t) throws SQLException {
        int id = t.getIdTicket();
        System.out.println("\ud83d\udd04 Tentative de modification du ticket avec ID: " + id);
        if (id <= 0) {
            System.out.println("❌ Erreur : ID de ticket invalide.");
        } else {
            String checkSql = "SELECT COUNT(*) FROM ticket WHERE id_ticket = ?";
            PreparedStatement checkStatement = this.cnx.prepareStatement(checkSql);
            checkStatement.setInt(1, id);
            ResultSet checkResult = checkStatement.executeQuery();
            checkResult.next();
            if (checkResult.getInt(1) == 0) {
                System.out.println("❌ Aucun ticket trouvé avec cet ID.");
            } else {
                String sql = "UPDATE ticket SET prix = ?, methode_paiement = ? WHERE id_ticket = ?";
                PreparedStatement st = this.cnx.prepareStatement(sql);
                st.setFloat(1, t.getPrix());
                st.setString(2, t.getMethodePaiement().name());
                st.setInt(3, id);
                int rowsUpdated = st.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("✅ Ticket modifié avec succès !");
                } else {
                    System.out.println("❌ La modification a échoué.");
                }

            }
        }
    }

    public List<Ticket> recuperer() throws SQLException {
        String sql = "SELECT * FROM ticket";
        Statement statement = this.cnx.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<Ticket> tickets = new ArrayList();

        while(resultSet.next()) {
            Ticket ticket = new Ticket();
            ticket.setIdTicket(resultSet.getInt("id_ticket"));
            ticket.setIdEvenement(resultSet.getInt("id_evenement"));
            Evenement evenement = new Evenement();
            evenement.setIdEvenement(resultSet.getInt("id_evenement"));
            ticket.setEvenement(evenement);
            ticket.setIdUtilisateur(resultSet.getInt("id_utilisateur"));
            ticket.setPrix(resultSet.getFloat("prix"));
            ticket.setQrCode(resultSet.getString("qr_code"));
            ticket.setMethodePaiement(Type_P.valueOf(resultSet.getString("methode_paiement")));
            tickets.add(ticket);
        }

        return tickets;
    }
}
