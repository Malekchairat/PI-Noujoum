package services;

<<<<<<< HEAD
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Evenement;
import models.Ticket;
import models.Type_P;
import models.Type_e;
=======
import models.Evenement;
import models.Ticket;
import models.Type_P;
>>>>>>> origin/GestionCommande
import tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
<<<<<<< HEAD
public class TicketService implements IService<Ticket> {
    private final Connection cnx = MyDataBase.getInstance().getCnx();

    public Evenement getEvenementById(int idEvenement) {
        String query = "SELECT * FROM evenement WHERE id_evenement = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, idEvenement);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Evenement(
                        rs.getInt("id_evenement"),
                        rs.getString("location"),
                        rs.getString("artist"),
                        rs.getString("description"),
                        rs.getDate("StartDate"),
                        rs.getDate("EndDate"),
                        rs.getInt("time"),
                        rs.getFloat("price"),
                        Type_e.valueOf(rs.getString("type")),
                        rs.getInt("ticketCount"),
                        rs.getBlob("imageE")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'événement : " + e.getMessage());
        }
        return null;
    }

    public List<Ticket> getTicketsByUser(int userId) {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT t.*, e.artist FROM ticket t " +
                "JOIN evenement e ON t.id_evenement = e.id_evenement " +
                "WHERE t.id_utilisateur = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Ticket ticket = new Ticket(
                        rs.getInt("id_ticket"),
                        rs.getInt("id_evenement"),
                        rs.getInt("id_utilisateur"),
                        rs.getFloat("prix"),
                        rs.getInt("quantite"),
                        rs.getString("qr_code"),
                        Type_P.valueOf(rs.getString("methode_paiement"))
                );
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }


    public boolean utilisateurExiste(int idUtilisateur) {
        String query = "SELECT COUNT(*) FROM user WHERE id_user = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, idUtilisateur);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'utilisateur: " + e.getMessage());
        }
        return false;
    }
    @Override

    public void ajouter(Ticket ticket) {
        try {
            double eventPrice = getEventPrice(ticket.getIdEvenement());
            double prix = ticket.getQuantite() * eventPrice;
            String req = "INSERT INTO ticket (id_evenement, id_utilisateur, quantite, prix, qr_code, methode_paiement) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = cnx.prepareStatement(req)) {
                ps.setInt(1, ticket.getIdEvenement());
                ps.setInt(2, ticket.getIdUtilisateur());
                ps.setInt(3, ticket.getQuantite());
                ps.setDouble(4, prix);
                ps.setString(5, ticket.getQrCode()); // Ajout du QR code ici
                ps.setString(6, ticket.getMethodePaiement().name());
                ps.executeUpdate();
                System.out.println("✅ Ticket ajouté avec succès avec QR Code !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout du ticket: " + e.getMessage());
        }
    }


    private double getEventPrice(int eventId) {
        String query = "SELECT price FROM evenement WHERE id_evenement = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du prix: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public void supprimer(int id) {
=======
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
=======
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
>>>>>>> origin/GestionCommande

            st.executeUpdate();

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ticket.setIdTicket(generatedKeys.getInt(1));
<<<<<<< HEAD
                    System.out.println("✅ Ticket ajouté avec succès ! ID: " + ticket.getIdTicket() + ", Quantité: " + ticket.getQuantite());
                }
            }
        }
    }

    // ✅ Correction : Suppression d'un ticket
    public void supprimer(int id) throws SQLException {
>>>>>>> origin/integration-branch
=======
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
>>>>>>> origin/GestionCommande
        if (id <= 0) {
            System.out.println("❌ Erreur : ID de ticket invalide.");
            return;
        }
<<<<<<< HEAD
<<<<<<< HEAD
=======

>>>>>>> origin/integration-branch
=======

>>>>>>> origin/GestionCommande
        String sql = "DELETE FROM ticket WHERE id_ticket = ?";
        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
<<<<<<< HEAD
<<<<<<< HEAD
            System.out.println(rowsDeleted > 0 ? "✅ Ticket supprimé avec succès !" : "❌ Aucun ticket trouvé.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public void modifier(Ticket t) {
        if (t.getIdTicket() <= 0 || t.getMethodePaiement() == null) {
            System.out.println("❌ Erreur : ID ou méthode de paiement invalide.");
            return;
        }
        String sql = "UPDATE ticket SET prix = ?, quantite = ?, methode_paiement = ? WHERE id_ticket = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setFloat(1, t.getPrix());
            st.setInt(2, t.getQuantite());
            st.setString(3, t.getMethodePaiement().name());
            st.setInt(4, t.getIdTicket());
            int rowsUpdated = st.executeUpdate();
            System.out.println(rowsUpdated > 0 ? "✅ Ticket modifié avec succès !" : "❌ La modification a échoué.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la modification : " + e.getMessage());
        }
    }

    @Override
    public List<Ticket> recuperer() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket";
        try (Statement statement = cnx.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                tickets.add(new Ticket(
                        resultSet.getInt("id_ticket"),
                        resultSet.getInt("id_evenement"),
                        resultSet.getInt("id_utilisateur"),
                        resultSet.getFloat("prix"),
                        resultSet.getInt("quantite"),
                        resultSet.getString("qr_code"),
                        Type_P.valueOf(resultSet.getString("methode_paiement"))
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération : " + e.getMessage());
=======
=======
>>>>>>> origin/GestionCommande
            if (rowsDeleted > 0) {
                System.out.println("✅ Ticket supprimé avec succès !");
            } else {
                System.out.println("❌ Aucun ticket trouvé avec cet ID.");
            }
<<<<<<< HEAD
        }
    }

    // ✅ Correction : Modification de la quantité possible
    public void modifier(Ticket t) throws SQLException {
=======
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    // ✅ Modifier un ticket
    @Override
    public void modifier(Ticket t) {
>>>>>>> origin/GestionCommande
        if (t.getIdTicket() <= 0) {
            System.out.println("❌ Erreur : ID de ticket invalide.");
            return;
        }

        String sql = "UPDATE ticket SET prix = ?, quantite = ?, methode_paiement = ? WHERE id_ticket = ?";
        try (PreparedStatement st = cnx.prepareStatement(sql)) {
            st.setFloat(1, t.getPrix());
<<<<<<< HEAD
            st.setInt(2, t.getQuantite()); // ✅ Ajout de la quantité dans la modification
=======
            st.setInt(2, t.getQuantite());
>>>>>>> origin/GestionCommande
            st.setString(3, t.getMethodePaiement().name());
            st.setInt(4, t.getIdTicket());

            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Ticket modifié avec succès !");
            } else {
                System.out.println("❌ La modification a échoué.");
            }
<<<<<<< HEAD
        }
    }

    // ✅ Correction : Récupération de la quantité
    public List<Ticket> recuperer() throws SQLException {
        String sql = "SELECT * FROM ticket";
        List<Ticket> tickets = new ArrayList<>();
=======
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la modification : " + e.getMessage());
        }
    }

    // ✅ Récupérer tous les tickets
    @Override
    public List<Ticket> recuperer() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket";
>>>>>>> origin/GestionCommande

        try (Statement statement = cnx.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Ticket ticket = new Ticket();
                ticket.setIdTicket(resultSet.getInt("id_ticket"));
                ticket.setIdEvenement(resultSet.getInt("id_evenement"));
                ticket.setIdUtilisateur(resultSet.getInt("id_utilisateur"));
                ticket.setPrix(resultSet.getFloat("prix"));
<<<<<<< HEAD
                ticket.setQuantite(resultSet.getInt("quantite")); // ✅ Ajout de la récupération de la quantité
=======
                ticket.setQuantite(resultSet.getInt("quantite"));
>>>>>>> origin/GestionCommande
                ticket.setQrCode(resultSet.getString("qr_code"));
                ticket.setMethodePaiement(Type_P.valueOf(resultSet.getString("methode_paiement")));

                tickets.add(ticket);
            }
<<<<<<< HEAD
>>>>>>> origin/integration-branch
        }
=======
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération : " + e.getMessage());
        }

>>>>>>> origin/GestionCommande
        return tickets;
    }
}
