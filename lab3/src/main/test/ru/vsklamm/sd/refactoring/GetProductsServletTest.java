package ru.vsklamm.sd.refactoring;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import ru.vsklamm.sd.refactoring.model.ProductDAO;
import ru.vsklamm.sd.refactoring.servlet.GetProductsServlet;
import ru.vsklamm.sd.refactoring.servlet.ServletTestWrapper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.vsklamm.sd.refactoring.database.ControllerDB.*;

public class GetProductsServletTest extends ServletTestWrapper {

    @Test
    @DisplayName("testing GetProductServlet")
    public void someProductsTest() {
        runSQL(SQL_TEST_INPUT);
        new GetProductsServlet(new ProductDAO()).doGet(mockRequest, mockResponse);
        final var result = writer.toString();
        assertTrue(result.contains("bath_water\t1000"));
        assertTrue(result.contains("hydrate\t10"));
        assertTrue(result.contains("water\t100"));
        assertTrue(result.contains("sub\t50"));
    }
}