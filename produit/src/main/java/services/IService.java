package services;

import java.util.List;

public interface IService<T> {
    void ajouter(T t);
    boolean modifier(T t);
    void supprimer(int id);
    T recuperer(int id); // Vérifie bien cette signature
    List<T> recupererTous();
}
