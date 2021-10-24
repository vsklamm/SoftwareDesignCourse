package ru.vsklamm.sd.refactoring.servlet;

import ru.vsklamm.sd.refactoring.model.Product;
import ru.vsklamm.sd.refactoring.model.ProductDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddProductServlet extends AbstractServlet {
    private final ProductDAO productDAO;

    public AddProductServlet(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));
        Product product = new Product(name, price);
        productDAO.addProduct(product);
        response.getWriter().println("OK");
    }
}
