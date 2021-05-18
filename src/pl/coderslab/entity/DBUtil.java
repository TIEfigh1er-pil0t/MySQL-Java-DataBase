package pl.coderslab.entity;

import java.sql.*;

public class DBUtil {
    public static String DB_URL =
            "jdbc:mysql://localhost:3306/workshop2?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=FALSE";
    public static String DB_USER = "root";
    public static String DB_PASS = "coderslab";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
}
