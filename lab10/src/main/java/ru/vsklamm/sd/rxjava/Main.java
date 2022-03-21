package ru.vsklamm.sd.rxjava;

import io.reactivex.netty.protocol.http.server.HttpServer;
import ru.vsklamm.sd.rxjava.controller.HttpNettyController;
import ru.vsklamm.sd.rxjava.controller.RxHttpController;
import ru.vsklamm.sd.rxjava.driver.CatalogMongoDriver;

public class Main {
    public static void main(String[] args) {
        HttpNettyController controller = new RxHttpController(
                CatalogMongoDriver.createMongo("mongodb://localhost:27017", "catalog")
        );

        HttpServer.newServer(8080)
                .start((req, resp) -> {
                    var response = controller.getResponse(req);
                    return resp.writeString(response);
                })
                .awaitShutdown();
    }
}


