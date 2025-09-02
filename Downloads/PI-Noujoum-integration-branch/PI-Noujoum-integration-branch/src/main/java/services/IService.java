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
