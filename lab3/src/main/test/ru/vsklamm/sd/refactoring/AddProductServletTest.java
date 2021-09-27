package ru.vsklamm.sd.refactoring;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
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

public class AddProductServletTest {
    private final StringWriter writer = new StringWriter();
    private static final String DATABASE = "jdbc:sqlite:test.db";

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    private void addOneProduct(final String name, final String price) throws IOException {
        when(mockRequest.getParameter("name")).thenReturn(name);
        when(mockRequest.getParameter("price")).thenReturn(price);
        new AddProductServlet().doGet(mockRequest, mockResponse);
    }

    private void runSQL(String sql) {
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
    @DisplayName("testing adding one product")
    public void addOneTest() throws IOException {
        addOneProduct("product", "1");
        var result = writer.toString();
        assertTrue(result.contains("OK"));
        new GetProductsServlet().doGet(mockRequest, mockResponse);
        result = writer.toString();
        assertTrue(result.contains("product"));
    }

    @Test
    @DisplayName("testing adding many products")
    public void addManyTest() throws IOException {
        addOneProduct("product1", "1");
        var result = writer.toString();
        assertTrue(result.contains("OK"));

        addOneProduct("product2", "2");
        result = writer.toString();
        assertTrue(result.contains("OK"));

        addOneProduct("product3", "3");
        result = writer.toString();
        assertTrue(result.contains("OK"));

        new GetProductsServlet().doGet(mockRequest, mockResponse);
        result = writer.toString();
        assertTrue(result.contains("product1"));
        assertTrue(result.contains("product2"));
        assertTrue(result.contains("product3"));
    }
}
