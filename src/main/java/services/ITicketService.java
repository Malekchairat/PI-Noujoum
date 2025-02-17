//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package services;

import models.Evenement;
import models.Ticket;

import java.sql.SQLException;
import java.util.List;

public interface ITicketService {
    void ajouter(Evenement var1, Ticket var2) throws SQLException;

    void supprimer(Ticket var1) throws SQLException;

    void modifier(Ticket var1) throws SQLException;

    List<Ticket> recuperer() throws SQLException;
}
