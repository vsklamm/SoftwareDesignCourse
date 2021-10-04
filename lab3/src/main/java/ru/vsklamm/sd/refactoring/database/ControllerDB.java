package ru.vsklamm.sd.refactoring.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ControllerDB {
    private static final String DATABASE = "jdbc:sqlite:test.db";

    public static final String SQL_TEST_INPUT =
            "INSERT INTO PRODUCT(NAME, PRICE) " +
                    "VALUES ('bath_water', 1000), ('hydrate', 10), ('water', 100), ('sub', 50)";
    public static final String CREATE_PRODUCT =
            "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
    public static final String DROP_PRODUCT =
            "DROP TABLE IF EXISTS PRODUCT";

    public static Statement createStatement() throws SQLException {
        return getConnection().createStatement();
    }

    public static void createDatabase() throws SQLException {
        try (var statement = createStatement()) {
            statement.executeUpdate(CREATE_PRODUCT);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE);
    }
}
