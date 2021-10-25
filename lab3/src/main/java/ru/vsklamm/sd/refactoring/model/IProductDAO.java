package ru.vsklamm.sd.refactoring.model;

import java.util.List;

public interface IProductDAO {
    List<Product> getProducts();

    void addProduct(final Product product);

    Product maxProduct();

    Product minProduct();

    long sumPrices();

    long countProducts();
}
