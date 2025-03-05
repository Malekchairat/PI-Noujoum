package services;

import java.sql.SQLException;
import java.util.List;

public interface ICommandeService <Commande> {
    void ajouter(Commande var1) throws SQLException;

    void supprimer(Commande var1);

    void modifier(Commande var1, String var2) throws SQLException;

    List<Commande> recuperer() throws SQLException;
}
