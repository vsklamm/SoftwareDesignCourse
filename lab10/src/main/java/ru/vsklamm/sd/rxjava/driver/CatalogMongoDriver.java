package ru.vsklamm.sd.rxjava.driver;

import com.mongodb.rx.client.MongoClients;
import ru.vsklamm.sd.rxjava.dao.CatalogMongoDao;

public class CatalogMongoDriver {
    static public CatalogMongoDao createMongo(
            final String port,
            final String database
    ) {
        var db = MongoClients.create(port).getDatabase(database);
        return new CatalogMongoDao(db.getCollection("users"), db.getCollection("products"));
    }
}
