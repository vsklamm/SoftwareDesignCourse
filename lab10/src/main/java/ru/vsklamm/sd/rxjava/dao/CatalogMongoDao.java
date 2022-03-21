package ru.vsklamm.sd.rxjava.dao;

import com.mongodb.client.model.Filters;
import com.mongodb.rx.client.MongoCollection;
import org.bson.Document;
import ru.vsklamm.sd.rxjava.model.Currency;
import ru.vsklamm.sd.rxjava.model.Product;
import ru.vsklamm.sd.rxjava.model.User;
import rx.Observable;

public class CatalogMongoDao implements CatalogDao {
    private final MongoCollection<Document> users;
    private final MongoCollection<Document> products;

    public CatalogMongoDao(
            final MongoCollection<Document> users,
            final MongoCollection<Document> products) {
        this.users = users;
        this.products = products;
    }

    @Override
    public Observable<Boolean> registerUser(final User user) {
        return insertInto(users, user.id, user.toDocument());
    }

    @Override
    public Observable<Boolean> addProduct(final Product product) {
        return insertInto(products, product.id, product.toDocument());
    }

    @Override
    public rx.Observable<User> getUsers() {
        return users.find().toObservable().map(User::new);
    }

    @Override
    public Observable<Product> getProductsByUser(final int userId) {
        return users
                .find(Filters.eq("id", userId)).toObservable()
                .map(doc -> Currency.valueOf(doc.getString("currency")))
                .flatMap(viewCurrency -> products
                        .find().toObservable()
                        .map(doc -> {
                            var product = new Product(doc);
                            product.convertCurrency(viewCurrency);
                            return product;
                        }));
    }

    private Observable<Boolean> insertInto(
            final MongoCollection<Document> coll,
            final int id,
            final Document doc
    ) {
        return coll
                .find(Filters.eq("id", id)).toObservable()
                .singleOrDefault(null)
                .flatMap(found -> {
                    if (found != null) {
                        return Observable.just(false);
                    } else {
                        return coll.insertOne(doc).asObservable().isEmpty().map(it -> !it);
                    }
                });
    }
}
