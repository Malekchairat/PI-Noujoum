//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    public final String URL = "jdbc:mysql://localhost:3306/noujoum";
    public final String USER = "root";
    public final String PWD = "";
    private Connection cnx;
    private static MyDataBase instance;

    private MyDataBase() {
        try {
            this.cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/noujoum", "root", "");
            System.out.println("cnx etablie!!");
        } catch (SQLException var2) {
            SQLException e = var2;
            System.err.println(e.getMessage());
        }

    }

    public static MyDataBase getInstance() {
        if (instance == null) {
            instance = new MyDataBase();
        }

        return instance;
    }

    public Connection getCnx() {
        return this.cnx;
    }
}
