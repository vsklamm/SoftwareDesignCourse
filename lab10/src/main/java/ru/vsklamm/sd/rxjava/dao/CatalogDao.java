package ru.vsklamm.sd.rxjava.dao;

import ru.vsklamm.sd.rxjava.model.Product;
import ru.vsklamm.sd.rxjava.model.User;
import rx.Observable;

public interface CatalogDao {
    /**
     * Register user
     *
     * @param user New user
     * @return True if user with given id is not in database and registration is successful, otherwise false
     */
    Observable<Boolean> registerUser(final User user);

    /**
     * Add new product
     *
     * @param product New product
     * @return True if product with given id is not in database and registration is successful, otherwise false
     */
    Observable<Boolean> addProduct(final Product product);

    /**
     * Get users
     *
     * @return users
     */
    Observable<User> getUsers();

    /**
     * Get products in the currency specified by the user
     *
     * @param userId User id
     * @return products
     */
    Observable<Product> getProductsByUser(final int userId);
}
