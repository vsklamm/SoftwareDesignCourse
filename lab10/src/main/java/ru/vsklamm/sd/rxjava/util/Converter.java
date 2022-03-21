package ru.vsklamm.sd.rxjava.util;

import ru.vsklamm.sd.rxjava.model.Currency;

import java.util.Map;

public class Converter {
    static private final Map<Currency, ? extends Number> rates = Map.of(
            Currency.RUB, 1,
            Currency.EUR, 111.5,
            Currency.USD, 101.9
    );

    /**
     * The most unfair and broken converter
     *
     * @param amount Amount of money to convert
     * @param from   From currency
     * @param to     To currency
     * @return Amount of money in a new currency
     */
    static public double convert(
            final double amount,
            final Currency from,
            final Currency to) {
        return amount * rates.get(from).doubleValue() / rates.get(to).doubleValue();
    }
}
