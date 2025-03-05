<<<<<<< HEAD
<<<<<<< HEAD
package services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import models.Evenement;
import models.Ticket;
import tools.MyDataBase;

import models.Favoris;

import java.util.List;

public interface IService<T> {
    void ajouter(T t);
    List<T> recuperer(); // Remplace "afficher"

    void supprimer(int id);
    void modifier(T t);
    //void ajouter(Evenement  Ticket);// Suppression du second paramètre
}
=======
<<<<<<< HEAD
=======
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

>>>>>>> GestionCommande
=======
>>>>>>> origin/GestionCommande
package services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

<<<<<<< HEAD
<<<<<<< HEAD

public interface IService<T> {

    public void ajouter(T t) throws SQLException;
    public void modifier(T t) throws SQLException;
    public void supprimer(T t) throws SQLException;
    public List<T> recuperer() throws SQLException;

}
=======
=======
import models.Evenement;
import models.Ticket;
import tools.MyDataBase;

import models.Favoris;

import java.util.List;

>>>>>>> origin/GestionCommande
public interface IService<T> {
    void ajouter(T t);
    List<T> recuperer(); // Remplace "afficher"

<<<<<<< HEAD
    void supprimer(T var1);

    void modifier(T var1, String var2);

    List<T> recuperer() throws SQLException;
}
>>>>>>> GestionCommande
>>>>>>> origin/integration-branch
=======
    void supprimer(int id);
    void modifier(T t);
    //void ajouter(Evenement  Ticket);// Suppression du second paramètre
}
>>>>>>> origin/GestionCommande
