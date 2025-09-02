package services;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Evenement;
import models.Type_e;
import tools.MyDataBase;

public class EvenementService implements IService<Evenement> {

    private Connection connection;

    public EvenementService() {
        connection = MyDataBase.getInstance().getCnx();
    }

    public Evenement recupererParId(int id) {
        String sql = "SELECT * FROM evenement WHERE id_evenement = ?";
        Evenement evenement = null;

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Blob imageBlob = resultSet.getBlob("imageE");
                evenement = new Evenement(
                        resultSet.getInt("id_evenement"),
                        resultSet.getString("location"),
                        resultSet.getString("artist"),
                        resultSet.getString("description"),
                        resultSet.getDate("startDate"),
                        resultSet.getDate("endDate"),
                        resultSet.getInt("time"),
                        resultSet.getFloat("price"),
                        Type_e.valueOf(resultSet.getString("type")),
                        resultSet.getInt("ticketCount"),
                        imageBlob
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return evenement;
    }

    @Override
    public void ajouter(Evenement evenement) {
        String sql = "INSERT INTO evenement (location, artist, description, startDate, endDate, time, price, type, ticketCount, imageE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, evenement.getLocation());
            statement.setString(2, evenement.getArtist());
            statement.setString(3, evenement.getDescription());
            statement.setDate(4, new java.sql.Date(evenement.getStartDate().getTime()));
            statement.setDate(5, new java.sql.Date(evenement.getEndDate().getTime()));
            statement.setInt(6, evenement.getTime());
            statement.setFloat(7, evenement.getPrice());
            statement.setString(8, evenement.getType().toString());
            statement.setInt(9, evenement.getTicketCount());

            // Ajout de l'image
            InputStream imageStream = evenement.getImageE().getBinaryStream();
            statement.setBinaryStream(10, imageStream);

            statement.executeUpdate();
            System.out.println("Événement ajouté avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Evenement> recuperer() {
        List<Evenement> evenements = new ArrayList<>();
        String sql = "SELECT * FROM evenement";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Blob imageBlob = resultSet.getBlob("imageE");
                Evenement evenement = new Evenement(
                        resultSet.getInt("id_evenement"),
                        resultSet.getString("location"),
                        resultSet.getString("artist"),
                        resultSet.getString("description"),
                        resultSet.getDate("startDate"),
                        resultSet.getDate("endDate"),
                        resultSet.getInt("time"),
                        resultSet.getFloat("price"),
                        Type_e.valueOf(resultSet.getString("type")),
                        resultSet.getInt("ticketCount"),
                        imageBlob
                );
                evenements.add(evenement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return evenements;
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM evenement WHERE id_evenement = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Événement supprimé avec succès !");
            } else {
                System.out.println("Aucun événement trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Evenement evenement) {
        System.out.println("Utilisez modifier(Evenement evenement, int id) au lieu de cette méthode.");
    }

    public void modifier(Evenement evenement, int id) {
        String sql = "UPDATE evenement SET location = ?, artist = ?, description = ?, startDate = ?, endDate = ?, time = ?, price = ?, type = ?, ticketCount = ?, imageE = ? WHERE id_evenement = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, evenement.getLocation());
            statement.setString(2, evenement.getArtist());
            statement.setString(3, evenement.getDescription());
            statement.setDate(4, new java.sql.Date(evenement.getStartDate().getTime()));
            statement.setDate(5, new java.sql.Date(evenement.getEndDate().getTime()));
            statement.setInt(6, evenement.getTime());
            statement.setFloat(7, evenement.getPrice());
            statement.setString(8, evenement.getType().toString());
            statement.setInt(9, evenement.getTicketCount());

            // Mise à jour de l'image
            InputStream imageStream = evenement.getImageE().getBinaryStream();
            statement.setBinaryStream(10, imageStream);
            statement.setInt(11, id);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Événement mis à jour avec succès !");
            } else {
                System.out.println("Aucun événement trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
<<<<<<< HEAD
    public void mettreAJourGoogleId(int eventId, String googleEventId) {
        String query = "UPDATE evenement SET google_calendar_id = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, googleEventId);
            pstmt.setInt(2, eventId);
            pstmt.executeUpdate();
            System.out.println("✅ ID Google Calendar enregistré avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
=======
>>>>>>> origin/GestionCommande
}
