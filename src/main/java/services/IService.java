//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package services;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {
    void ajouter(T var1) throws SQLException;

    void supprimer(T var1);

    void modifier(T var1, String var2);

    List<T> recuperer() throws SQLException;
}
