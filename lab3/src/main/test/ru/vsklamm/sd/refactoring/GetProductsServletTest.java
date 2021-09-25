package ru.vsklamm.sd.refactoring;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class GetProductsServletTest {
    private final StringWriter writer = new StringWriter();
    private static final String DATABASE = "jdbc:sqlite:test.db";

    private static final String SQL_INPUT =
            "INSERT INTO PRODUCT(NAME, PRICE) " +
                    "VALUES ('bath_water', 1000), ('hydrate', 10), ('water', 100), ('sub', 50)";
    private static final String CREATE_PRODUCT =
            "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
    private static final String DROP_PRODUCT =
            "DROP TABLE IF EXISTS PRODUCT";

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    private void runSQL(final String sql) {
        try (Connection c = DriverManager.getConnection(DATABASE)) {
            var statement = c.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Before
    public void setMocks() throws IOException {
        MockitoAnnotations.openMocks(this);
        when(mockResponse.getWriter()).thenReturn(new PrintWriter(writer));
    }

    @Before
    public void createProductDB() {
        runSQL(CREATE_PRODUCT);
    }

    @After
    public void dropProductDB() {
        runSQL(DROP_PRODUCT);
    }

    @Test
    @DisplayName("testing GetProductServlet")
    public void someProductsTest() throws IOException {
        runSQL(SQL_INPUT);
        new GetProductsServlet().doGet(mockRequest, mockResponse);
        final var result = writer.toString();
        assertTrue(result.contains("bath_water\t1000"));
        assertTrue(result.contains("hydrate\t10"));
        assertTrue(result.contains("water\t100"));
        assertTrue(result.contains("sub\t50"));
    }
}