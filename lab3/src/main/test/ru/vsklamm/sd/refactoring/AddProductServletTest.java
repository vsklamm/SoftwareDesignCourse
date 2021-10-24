package ru.vsklamm.sd.refactoring;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import ru.vsklamm.sd.refactoring.model.ProductDAO;
import ru.vsklamm.sd.refactoring.servlet.AddProductServlet;
import ru.vsklamm.sd.refactoring.servlet.GetProductsServlet;
import ru.vsklamm.sd.refactoring.servlet.ServletTestWrapper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class AddProductServletTest extends ServletTestWrapper {

    private void addOneProduct(final String name, final String price) {
        when(mockRequest.getParameter("name")).thenReturn(name);
        when(mockRequest.getParameter("price")).thenReturn(price);
        new AddProductServlet(new ProductDAO()).doGet(mockRequest, mockResponse);
    }

    @Test
    @DisplayName("testing adding one product")
    public void addOneTest() {
        addOneProduct("product", "1");
        var result = writer.toString();
        assertTrue(result.contains("OK"));
        new GetProductsServlet(new ProductDAO()).doGet(mockRequest, mockResponse);
        result = writer.toString();
        assertTrue(result.contains("product"));
    }

    @Test
    @DisplayName("testing adding many products")
    public void addManyTest() {
        addOneProduct("product1", "1");
        var result = writer.toString();
        assertTrue(result.contains("OK"));

        addOneProduct("product2", "2");
        result = writer.toString();
        assertTrue(result.contains("OK"));

        addOneProduct("product3", "3");
        result = writer.toString();
        assertTrue(result.contains("OK"));

        new GetProductsServlet(new ProductDAO()).doGet(mockRequest, mockResponse);
        result = writer.toString();
        assertTrue(result.contains("product1"));
        assertTrue(result.contains("product2"));
        assertTrue(result.contains("product3"));
    }
}
