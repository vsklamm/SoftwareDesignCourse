package ru.vsklamm.sd.rxjava.model;

import org.bson.Document;

public class User {
    public final int id;
    public final String login;
    public final String name;
    public final Currency currency;

    public User(
            final int id,
            final String login,
            final String name,
            final Currency currency
    ) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.currency = currency;
    }

    public User(final Document doc) {
        this(
                doc.getDouble("id").intValue(),
                doc.getString("login"),
                doc.getString("name"),
                Currency.valueOf(doc.getString("currency"))
        );
    }

    public Document toDocument() {
        return new Document()
                .append("id", id)
                .append("login", login)
                .append("name", name)
                .append("currency", currency.toString());
    }
}
