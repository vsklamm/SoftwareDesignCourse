package ru.vsklamm.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.vsklamm.sd.refactoring.servlet.AddProductServlet;
import ru.vsklamm.sd.refactoring.servlet.GetProductsServlet;
import ru.vsklamm.sd.refactoring.servlet.QueryServlet;
import ru.vsklamm.sd.refactoring.database.ControllerDB;

public class Main {
    public static void main(String[] args) throws Exception {
        ControllerDB.createDatabase();

        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet()), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet()), "/get-products");
        context.addServlet(new ServletHolder(new QueryServlet()), "/query");

        server.start();
        server.join();
    }
}
