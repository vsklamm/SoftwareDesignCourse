package ru.vsklamm.sd.rxjava.controller;

import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import ru.vsklamm.sd.rxjava.dao.CatalogDao;
import ru.vsklamm.sd.rxjava.model.Currency;
import ru.vsklamm.sd.rxjava.model.Product;
import ru.vsklamm.sd.rxjava.model.User;
import rx.Observable;

public class RxHttpController implements HttpNettyController {
    private final CatalogDao dao;

    public RxHttpController(final CatalogDao dao) {
        this.dao = dao;
    }

    @Override
    public <T> Observable<String> getResponse(final HttpServerRequest<T> request) {
        var path = request.getDecodedPath().substring(1);
        return switch (path) {
            case ("register") -> registerUser(request);
            case ("add_product") -> addProduct(request);
            case ("users") -> getUsers();
            case ("user_products") -> getProductsForUser(request);
            default -> Observable.just("Unknown: " + path);
        };
    }

    private <T> Observable<String> registerUser(final HttpServerRequest<T> request) {
        final var params = request.getQueryParameters();
        final var id = Integer.parseInt(params.get("id").get(0));
        final var login = params.get("login").get(0);
        final var name = params.get("name").get(0);
        final var currency = Currency.valueOf(params.get("currency").get(0));

        return dao.registerUser(new User(id, login, name, currency)).map(Object::toString);
    }

    private <T> Observable<String> addProduct(final HttpServerRequest<T> request) {
        final var params = request.getQueryParameters();
        final var id = Integer.parseInt(params.get("id").get(0));
        final var name = params.get("name").get(0);
        final var price = Double.parseDouble(params.get("price").get(0));
        final var currency = Currency.valueOf(params.get("currency").get(0));

        return dao.addProduct(new Product(id, name, price, currency)).map(Object::toString);
    }

    private <T> Observable<String> getUsers() {
        return dao.getUsers().map(Object::toString);
    }

    private <T> Observable<String> getProductsForUser(final HttpServerRequest<T> request) {
        final var params = request.getQueryParameters();
        final var id = Integer.parseInt(params.get("id").get(0));

        return dao.getProductsByUser(id).map(Object::toString);
    }
}

