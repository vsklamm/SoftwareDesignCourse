package ru.vsklamm.sd.rxjava.controller;

import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import rx.Observable;

public interface HttpNettyController {
    <T> Observable<String> getResponse(final HttpServerRequest<T> request);
}
