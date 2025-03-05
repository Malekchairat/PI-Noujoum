package services;

import java.util.List;

public interface IService <T>{
    public void ajouter(T t);
    public void modifier(T t, int id);
    public void supprimer(int id);
    public List<T> recuperer();
    public T recupererParId(int id);
}
