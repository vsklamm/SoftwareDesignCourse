package ru.vsklamm.sd.refactoring.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static ru.vsklamm.sd.refactoring.database.ControllerDB.getConnection;

public class GetProductsServlet extends AbstractServlet {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {
        try (var c = getConnection()) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
            ArrayList<String> log = new ArrayList<>();
            printResultSet(rs, log);
            logHttp(log, response);
            rs.close();
            stmt.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void printResultSet(ResultSet rs, ArrayList<String> log) throws SQLException {
        while (rs.next()) {
            var name = rs.getString("name");
            int price = rs.getInt("price");
            log.add(name + "\t" + price + "</br>");
        }
    }
}
