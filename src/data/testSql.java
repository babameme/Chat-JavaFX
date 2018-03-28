package data;

import java.sql.Connection;
import java.sql.SQLException;

public class testSql {
    public static void main(String[] args) {
        Connection sql = DataAccess.connection();
        try {
            if (!sql.isClosed()){
                System.out.println("Connect successful");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
