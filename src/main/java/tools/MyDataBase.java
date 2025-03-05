<<<<<<< HEAD
=======
<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> GestionCommande
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

<<<<<<< HEAD
>>>>>>> GestionEvenements
=======
>>>>>>> GestionCommande
>>>>>>> origin/integration-branch
package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
<<<<<<< HEAD
    public final String URL="jdbc:mysql://localhost:3306/noujoum";
    public final String USER="root";
    public final String PWD ="";
=======
<<<<<<< HEAD
<<<<<<< HEAD
    public final String URL="jdbc:mysql://localhost:3306/noujoum";
    public final String USER="root";
    public final String PWD ="";
    private Connection cnx;
    private static MyDataBase instance;

    private MyDataBase(){
        try {
            cnx = DriverManager.getConnection(URL,USER,PWD);
            System.out.println("cnx etablie!!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public static MyDataBase getInstance(){
        if(instance==null)
            instance= new MyDataBase();
=======
=======
>>>>>>> GestionCommande
    public final String URL = "jdbc:mysql://localhost:3306/noujoum";
    public final String USER = "root";
    public final String PWD = "";
>>>>>>> origin/integration-branch
    private Connection cnx;
    private static MyDataBase instance;

    private MyDataBase(){
        try {
            cnx = DriverManager.getConnection(URL,USER,PWD);
            System.out.println("cnx etablie!!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
<<<<<<< HEAD
    public static MyDataBase getInstance(){
        if(instance==null)
            instance= new MyDataBase();
=======

    public static MyDataBase getInstance() {
        if (instance == null) {
            instance = new MyDataBase();
        }

<<<<<<< HEAD
>>>>>>> GestionEvenements
=======
>>>>>>> GestionCommande
>>>>>>> origin/integration-branch
        return instance;
    }

    public Connection getCnx() {
<<<<<<< HEAD
        return cnx;
=======
<<<<<<< HEAD
<<<<<<< HEAD
        return cnx;
=======
        return this.cnx;
>>>>>>> GestionEvenements
=======
        return this.cnx;
>>>>>>> GestionCommande
>>>>>>> origin/integration-branch
    }
}