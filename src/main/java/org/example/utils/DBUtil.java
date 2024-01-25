package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBUtil {
    private static Connection instance;

    private DBUtil() {}

    public static Connection getConnection() {

        if(instance == null){
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instance = DriverManager.getConnection("jdbc:mysql://localhost:3300", "root", "123456");
            } catch (ClassNotFoundException | SQLException e) {
                System.exit(0);
            }
        }

        return instance;

    }

}
