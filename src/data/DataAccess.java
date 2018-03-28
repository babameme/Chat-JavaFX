package data;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataAccess {
    private static final String jdbc_driver = "com.mysql.jdbc.Driver";
    private static final String database_link = "jdbc:mysql://localhost:3306/users";
    public static Connection connection(){
        Connection res = null;
        try {
            Class.forName(jdbc_driver);
            res = DriverManager.getConnection(database_link, "root","abcd");
        } catch (Exception e) {
            System.err.println("Error when try to connect: " + e.getMessage());
        }
        return res;
    }
}
