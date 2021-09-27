package ru.vsklamm.sd.refactoring.servlet;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.mockito.Mockito.when;
import static ru.vsklamm.sd.refactoring.database.ControllerDB.*;

public class ServletTestWrapper {
    protected final StringWriter writer = new StringWriter();

    protected void runSQL(final String sql) {
        try (var statement = createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Mock
    protected HttpServletRequest mockRequest; // FIXME:

    @Mock
    protected HttpServletResponse mockResponse;

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
}
