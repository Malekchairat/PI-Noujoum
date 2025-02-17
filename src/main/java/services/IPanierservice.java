package services;

import java.sql.SQLException;
import java.util.List;

public interface IPanierservice<Panier> {
    void ajouter(Panier var1) throws SQLException;
    void supprimer(Panier var1);
    void modifier(Panier panier, String var2) throws SQLException;  // ✅ Ajout de throws SQLException
    List<Panier> recuperer() throws SQLException;
}
