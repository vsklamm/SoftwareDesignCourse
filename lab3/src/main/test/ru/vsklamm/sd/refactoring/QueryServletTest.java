package ru.vsklamm.sd.refactoring;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import ru.vsklamm.sd.refactoring.servlet.AddProductServlet;
import ru.vsklamm.sd.refactoring.servlet.QueryServlet;
import ru.vsklamm.sd.refactoring.servlet.ServletTestWrapper;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class QueryServletTest extends ServletTestWrapper {

    private void addOneProduct(final String name, final String price) throws IOException {
        when(mockRequest.getParameter("name")).thenReturn(name);
        when(mockRequest.getParameter("price")).thenReturn(price);
        new AddProductServlet().doGet(mockRequest, mockResponse);
    }

    @Test
    @DisplayName("Sum test")
    public void testSum() throws IOException {
        addOneProduct("product1", "1");
        addOneProduct("product2", "2");
        addOneProduct("product3", "3");
        when(mockRequest.getParameter("command")).thenReturn("sum");
        new QueryServlet().doGet(mockRequest, mockResponse);
        final var result = writer.toString();
        assertTrue(result.contains("6"));
    }

    @Test
    @DisplayName("Max test")
    public void testMax() throws IOException {
        addOneProduct("product1", "1");
        addOneProduct("product2", "2");
        addOneProduct("product3", "3");
        when(mockRequest.getParameter("command")).thenReturn("max");
        new QueryServlet().doGet(mockRequest, mockResponse);
        final var result = writer.toString();
        assertTrue(result.contains("product3\t3"));
    }

    @Test
    @DisplayName("Min test")
    public void testMin() throws IOException {
        addOneProduct("product1", "1");
        addOneProduct("product2", "2");
        addOneProduct("product3", "3");
        when(mockRequest.getParameter("command")).thenReturn("min");
        new QueryServlet().doGet(mockRequest, mockResponse);
        final var result = writer.toString();
        assertTrue(result.contains("product1\t1"));
    }
}
