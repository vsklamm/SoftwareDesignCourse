package ru.vsklamm.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractServlet extends HttpServlet {

    protected final String htmlOpenTag = "<html><body>";
    protected final String htmlCloseTag = "</body></html>";

    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException;

    protected void printToResponseWriter(HttpServletResponse response, String line) {
        try {
            response.getWriter().println(line);
        } catch (IOException ignored) {
        }
    }

    protected void logHttp(List<String> info, HttpServletResponse response) throws IOException {
        response.getWriter().println(htmlOpenTag);
        info.forEach(line -> printToResponseWriter(response, line));
        response.getWriter().println(htmlCloseTag);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            processRequest(request, response);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}