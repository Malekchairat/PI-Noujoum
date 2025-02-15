//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.Evenement;
import models.Type_e;
import tools.MyDataBase;

public class EvenementService implements IEvenementService {
    private Connection cnx = MyDataBase.getInstance().getCnx();

    public EvenementService() {
    }

    public void ajouter(Evenement e) throws SQLException {
        String sql = "INSERT INTO Evenement(location, artist, description, startDate, endDate, time, price, type, ticketCount) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement st = this.cnx.prepareStatement(sql);
        st.setString(1, e.getLocation());
        st.setString(2, e.getArtist());
        st.setString(3, e.getDescription());
        st.setDate(4, new Date(e.getStartDate().getTime()));
        st.setDate(5, new Date(e.getEndDate().getTime()));
        st.setInt(6, e.getTime());
        st.setFloat(7, e.getPrice());
        st.setString(8, e.getType().getTypeString());
        st.setInt(9, e.getTicketCount());
        st.executeUpdate();
        System.out.println("Événement ajouté avec succès !");
    }

    public void modifier(Evenement evenement, int ancienId) throws SQLException {
        String sql = "UPDATE evenement SET location = ?, artist = ?, description = ?, StartDate = ?, EndDate = ?, time = ?, price = ?, type = ?, ticketCount = ? WHERE id_evenement = ?";

        try {
            PreparedStatement st = this.cnx.prepareStatement(sql);

            try {
                st.setString(1, evenement.getLocation());
                st.setString(2, evenement.getArtist());
                st.setString(3, evenement.getDescription());
                st.setDate(4, new Date(evenement.getStartDate().getTime()));
                st.setDate(5, new Date(evenement.getEndDate().getTime()));
                st.setInt(6, evenement.getTime());
                st.setFloat(7, evenement.getPrice());
                st.setString(8, evenement.getType().name());
                st.setInt(9, evenement.getTicketCount());
                st.setInt(10, ancienId);
                int affectedRows = st.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("✅ Événement modifié avec succès !");
                } else {
                    System.out.println("⚠️ Aucun événement trouvé avec l'ID : " + ancienId);
                }
            } catch (Throwable var8) {
                if (st != null) {
                    try {
                        st.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }

                throw var8;
            }

            if (st != null) {
                st.close();
            }

        } catch (SQLException var9) {
            SQLException ex = var9;
            System.err.println("❌ Erreur lors de la modification de l'événement : " + ex.getMessage());
            throw ex;
        }
    }

    public void supprimer(int idEvenement) throws SQLException {
        String sql = "DELETE FROM evenement WHERE id_evenement = ?";

        try {
            PreparedStatement st = this.cnx.prepareStatement(sql);

            try {
                st.setInt(1, idEvenement);
                int rowsDeleted = st.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("✅ Événement avec ID " + idEvenement + " supprimé avec succès !");
                } else {
                    System.out.println("⚠️ Aucun événement trouvé avec l'ID : " + idEvenement);
                }
            } catch (Throwable var7) {
                if (st != null) {
                    try {
                        st.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }

                throw var7;
            }

            if (st != null) {
                st.close();
            }

        } catch (SQLException var8) {
            SQLException ex = var8;
            System.err.println("❌ Erreur lors de la suppression de l'événement : " + ex.getMessage());
            throw ex;
        }
    }

    public List<Evenement> recuperer() throws SQLException {
        String sql = "SELECT * FROM evenement";
        Statement statement = this.cnx.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<Evenement> evenements = new ArrayList();

        while(resultSet.next()) {
            Evenement evenement = new Evenement();
            evenement.setIdEvenement(resultSet.getInt("id_evenement"));
            evenement.setLocation(resultSet.getString("location"));
            evenement.setArtist(resultSet.getString("artist"));
            evenement.setDescription(resultSet.getString("description"));
            evenement.setStartDate(new java.util.Date(resultSet.getDate("startDate").getTime()));
            evenement.setEndDate(new java.util.Date(resultSet.getDate("endDate").getTime()));
            evenement.setTime(resultSet.getInt("time"));
            evenement.setPrice(resultSet.getFloat("price"));
            evenement.setType(Type_e.valueOf(resultSet.getString("type")));
            evenement.setTicketCount(resultSet.getInt("ticketCount"));
            evenements.add(evenement);
        }

        return evenements;
    }
}
