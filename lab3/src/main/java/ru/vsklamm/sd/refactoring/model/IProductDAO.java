package ru.vsklamm.sd.refactoring.model;

import java.util.List;

public interface IProductDAO {
    List<Product> getProducts();

    void addProduct(Product product);

    Product maxProduct();

    Product minProduct();

    long sumPrices();

    long countProducts();
}
