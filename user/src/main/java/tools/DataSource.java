package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSource {
    String url ="jdbc:mysql://localhost:3306/gestionproduits";
    String user = "root";
    String pwd = "";
    Connection con ;
    Statement stmt ;
    private static DataSource instance;
    public DataSource() {
        try {
            // Load MySQL JDBC Driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(url, user, pwd);
            System.out.println("Connection established \n Bingo");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }


    public static DataSource getInstance() {
        if(instance == null){
            instance = new DataSource();
        }
        return instance;
    }
    public  Connection getConnection(){
        return con;
    }
}
