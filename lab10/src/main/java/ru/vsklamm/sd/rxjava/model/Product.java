package ru.vsklamm.sd.rxjava.model;

import org.bson.Document;
import ru.vsklamm.sd.rxjava.util.Converter;

public class Product {
    public final int id;
    public final String name;
    public double price; // FIXME: I know double is bad
    public Currency currency;

    public Product(
            final int id,
            final String name,
            final double price,
            final Currency currency
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.currency = currency;
    }

    public Product(final Document doc) {
        this(
                doc.getInteger("id"),
                doc.getString("name"),
                doc.getDouble("price"),
                Currency.valueOf(doc.getString("currency"))
        );
    }

    public Document toDocument() {
        return new Document()
                .append("id", id)
                .append("name", name)
                .append("price", price)
                .append("currency", currency.toString());
    }

    public void convertCurrency(final Currency to) {
        price = Converter.convert(price, currency, to);
        currency = to;
    }
}
