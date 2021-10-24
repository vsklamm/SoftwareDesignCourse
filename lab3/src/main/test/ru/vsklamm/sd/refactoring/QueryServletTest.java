package ru.vsklamm.sd.refactoring;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import ru.vsklamm.sd.refactoring.model.ProductDAO;
import ru.vsklamm.sd.refactoring.servlet.AddProductServlet;
import ru.vsklamm.sd.refactoring.servlet.QueryServlet;
import ru.vsklamm.sd.refactoring.servlet.ServletTestWrapper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class QueryServletTest extends ServletTestWrapper {

    private void addOneProduct(final String name, final String price) {
        when(mockRequest.getParameter("name")).thenReturn(name);
        when(mockRequest.getParameter("price")).thenReturn(price);
        new AddProductServlet(new ProductDAO()).doGet(mockRequest, mockResponse);
    }

    @Test
    @DisplayName("Sum test")
    public void testSum() {
        addOneProduct("product1", "1");
        addOneProduct("product2", "2");
        addOneProduct("product3", "3");
        when(mockRequest.getParameter("command")).thenReturn("sum");
        new QueryServlet(new ProductDAO()).doGet(mockRequest, mockResponse);
        final var result = writer.toString();
        assertTrue(result.contains("6"));
    }

    @Test
    @DisplayName("Max test")
    public void testMax() {
        addOneProduct("product1", "1");
        addOneProduct("product2", "2");
        addOneProduct("product3", "3");
        when(mockRequest.getParameter("command")).thenReturn("max");
        new QueryServlet(new ProductDAO()).doGet(mockRequest, mockResponse);
        final var result = writer.toString();
        assertTrue(result.contains("product3\t3"));
    }

    @Test
    @DisplayName("Min test")
    public void testMin() {
        addOneProduct("product1", "1");
        addOneProduct("product2", "2");
        addOneProduct("product3", "3");
        when(mockRequest.getParameter("command")).thenReturn("min");
        new QueryServlet(new ProductDAO()).doGet(mockRequest, mockResponse);
        final var result = writer.toString();
        assertTrue(result.contains("product1\t1"));
    }

    @Test
    @DisplayName("Count test")
    public void testCount() {
        addOneProduct("product1", "1");
        addOneProduct("product2", "2");
        addOneProduct("product3", "3");
        when(mockRequest.getParameter("command")).thenReturn("count");
        new QueryServlet(new ProductDAO()).doGet(mockRequest, mockResponse);
        final var result = writer.toString();
        assertTrue(result.contains("3"));
    }
}
