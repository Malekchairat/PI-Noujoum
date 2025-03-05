package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    private static final String URL = "jdbc:mysql://localhost:3306/noujoum1";
    private static final String USER = "root";
    private static final String PWD = "";

    private static MyDataBase instance;
    private Connection cnx;

    private MyDataBase() {
        try {
            // Load MySQL Driver (Optional, but recommended)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Initialize connection
            cnx = DriverManager.getConnection(URL, USER, PWD);
            System.out.println("✅ Connection to the database established.");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
        }
    }

    public static synchronized MyDataBase getInstance() {
        if (instance == null) {
            instance = new MyDataBase();
        }
        return instance;
    }

    public Connection getCnx() {
        if (cnx == null) {
            System.err.println("❌ Connection is null. Reconnecting...");
            instance = new MyDataBase(); // Try to reconnect
        }
        return cnx;
    }
}
