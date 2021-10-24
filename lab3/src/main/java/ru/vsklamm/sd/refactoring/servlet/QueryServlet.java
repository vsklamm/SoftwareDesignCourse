package ru.vsklamm.sd.refactoring.servlet;

import ru.vsklamm.sd.refactoring.model.Product;
import ru.vsklamm.sd.refactoring.model.ProductDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class QueryServlet extends AbstractServlet {

    private final ProductDAO productDAO;

    public QueryServlet(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        switch (command) {
            case "max":
                Product maxProduct = productDAO.maxProduct();
                String maxHeader = "<h1>Product with max price: </h1>";
                logHttp(List.of(maxHeader, maxProduct.toHttp()), response);
                break;
            case "min":
                Product minProduct = productDAO.minProduct();
                String minHeader = "<h1>Product with min price: </h1>";
                logHttp(List.of(minHeader, minProduct.toHttp()), response);
                break;
            case "sum":
                logHttp(List.of("Summary price: ", Long.toString(productDAO.sumPrices())), response);
                break;
            case "count":
                logHttp(List.of("Number of products: ", Long.toString(productDAO.countProducts())), response);
                break;
            default:
                response.getWriter().println("Unknown command: " + command);
                break;
        }
    }
}