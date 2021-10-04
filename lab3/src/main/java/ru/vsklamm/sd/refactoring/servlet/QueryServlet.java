package ru.vsklamm.sd.refactoring.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static ru.vsklamm.sd.refactoring.database.ControllerDB.getConnection;

public class QueryServlet extends AbstractServlet {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            try {
                try (var c = getConnection()) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
                    ArrayList<String> log = new ArrayList<>();
                    log.add("<h1>Product with max price: </h1>");
                    GetProductsServlet.printResultSet(rs, log);
                    logHttp(log, response);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("min".equals(command)) {
            try {
                try (var c = getConnection()) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
                    ArrayList<String> log = new ArrayList<>();
                    log.add("<h1>Product with min price: </h1>");
                    GetProductsServlet.printResultSet(rs, log);
                    logHttp(log, response);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("sum".equals(command)) {
            try {
                try (var c = getConnection()) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");
                    ArrayList<String> log = new ArrayList<>();
                    log.add("Summary price: ");

                    if (rs.next()) {
                        log.add(rs.getString(1));
                    }
                    logHttp(log, response);
                    rs.close();
                    stmt.close();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("count".equals(command)) {
            try {
                try (var c = getConnection()) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");
                    ArrayList<String> log = new ArrayList<>();
                    log.add("Number of products: ");

                    if (rs.next()) {
                        log.add(rs.getString(1));
                    }
                    logHttp(log, response);
                    rs.close();
                    stmt.close();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            response.getWriter().println("Unknown command: " + command);
        }
    }

}
