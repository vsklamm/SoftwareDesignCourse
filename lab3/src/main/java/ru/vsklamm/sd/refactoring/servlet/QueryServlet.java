package ru.vsklamm.sd.refactoring.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static ru.vsklamm.sd.refactoring.database.ControllerDB.*;

public class QueryServlet extends AbstractServlet {

    private void processOp(String header, String sql, HttpServletResponse response, String op) {
        try {
            try (var c = getConnection()) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                ArrayList<String> log = new ArrayList<>();
                log.add(header);
                switch (op) {
                    case "max":
                    case "min":
                        GetProductsServlet.printResultSet(rs, log);
                        break;
                    case "sum":
                    case "count":
                        if (rs.next()) {
                            log.add(rs.getString(1));
                        }
                        break;
                    default:
                        break;
                }
                logHttp(log, response);
                rs.close();
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        switch (command) {
            case "max":
                String sqlMax = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
                String headerMax = "<h1>Product with max price: </h1>";
                processOp(headerMax, sqlMax, response, "max");
                break;
            case "min":
                String sqlMin = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
                String headerMin = "<h1>Product with min price: </h1>";
                processOp(headerMin, sqlMin, response, "min");
                break;
            case "sum":
                String sqlSum = "SELECT SUM(price) FROM PRODUCT";
                String headerSum = "Summary price: ";
                processOp(headerSum, sqlSum, response, "sum");
                break;
            case "count":
                String sqlCount = "SELECT COUNT(*) FROM PRODUCT";
                String headerCount = "Number of products: ";
                processOp(headerCount, sqlCount, response, "count");
                break;
            default:
                response.getWriter().println("Unknown command: " + command);
        }
    }

}