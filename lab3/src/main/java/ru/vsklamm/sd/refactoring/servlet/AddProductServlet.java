package ru.vsklamm.sd.refactoring.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

import static ru.vsklamm.sd.refactoring.database.ControllerDB.getConnection;

public class AddProductServlet extends AbstractServlet {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        try (var c = getConnection()) {
            String sql = "INSERT INTO PRODUCT " +
                    "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.getWriter().println("OK");
    }
}
