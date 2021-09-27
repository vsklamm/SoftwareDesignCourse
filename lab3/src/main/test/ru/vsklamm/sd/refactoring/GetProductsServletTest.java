package ru.vsklamm.sd.refactoring;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;

import static ru.akirakozov.sd.refactoring.database.ControllerDB.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static ru.akirakozov.sd.refactoring.database.ControllerDB.createStatement;

public class GetProductsServletTest {
    private final StringWriter writer = new StringWriter();
    private static final String DATABASE = "jdbc:sqlite:test.db";

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    private void runSQL(final String sql) {
        try (var statement = createStatement()) {
            statement.executeUpdate(sql);
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
        runSQL(SQL_TEST_INPUT);
        new GetProductsServlet().doGet(mockRequest, mockResponse);
        final var result = writer.toString();
        assertTrue(result.contains("bath_water\t1000"));
        assertTrue(result.contains("hydrate\t10"));
        assertTrue(result.contains("water\t100"));
        assertTrue(result.contains("sub\t50"));
    }
}