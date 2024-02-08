package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBUtil {
    private static Connection instance;

    public static Connection getConnection() {

        try {
            if(instance == null || instance.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instance = DriverManager.getConnection("jdbc:mysql://localhost:3300/sys",
                        "root", "123456");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Problem with the database connection has occurred" + ex.getMessage());
            try {
                instance.close();
            } catch (SQLException e) {
                System.out.println("Problem with closing the established connection has occurred" + e.getMessage());;
            }
        }
        return instance;
    }
}


