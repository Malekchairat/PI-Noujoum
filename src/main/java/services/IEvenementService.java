//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package services;

import models.Evenement;

import java.sql.SQLException;
import java.util.List;

public interface IEvenementService {
    void ajouter(Evenement var1) throws SQLException;

    void modifier(Evenement var1, int var2) throws SQLException;

    void supprimer(int var1) throws SQLException;

    List<Evenement> recuperer() throws SQLException;
}
