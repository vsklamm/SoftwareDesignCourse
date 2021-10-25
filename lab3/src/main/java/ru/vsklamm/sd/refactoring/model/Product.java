package ru.vsklamm.sd.refactoring.model;

import java.util.Objects;

public class Product {
    private final String name;
    private final long price;

    public Product(final String name, long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public long getPrice() {
        return this.price;
    }

    public String toHttp() {
        return this.name + "\t" + this.price + "</br>";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Product product = (Product) obj;
        return price == product.price &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

}
