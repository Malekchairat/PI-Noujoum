<<<<<<< HEAD
=======
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

>>>>>>> GestionCommande
package services;

import java.sql.SQLException;
import java.util.List;

<<<<<<< HEAD

public interface IService<T> {

    public void ajouter(T t) throws SQLException;
    public void modifier(T t) throws SQLException;
    public void supprimer(T t) throws SQLException;
    public List<T> recuperer() throws SQLException;

}
=======
public interface IService<T> {
    void ajouter(T var1) throws SQLException;

    void supprimer(T var1);

    void modifier(T var1, String var2);

    List<T> recuperer() throws SQLException;
}
>>>>>>> GestionCommande
