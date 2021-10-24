package ru.vsklamm.sd.refactoring.servlet;

import ru.vsklamm.sd.refactoring.model.Product;
import ru.vsklamm.sd.refactoring.model.ProductDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GetProductsServlet extends AbstractServlet {

    private final ProductDAO productDAO;

    public GetProductsServlet(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Product> products = productDAO.getProducts();
        List<String> log = products.stream().map(Product::toHttp).collect(Collectors.toList());
        logHttp(log, response);
    }
}
