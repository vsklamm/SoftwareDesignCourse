package ru.vsklamm.sd.refactoring.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static ru.vsklamm.sd.refactoring.database.ControllerDB.*;

public class ProductDAO implements IProductDAO {

    private Product getMinOrMax(final String sql) {
        try (var statement = createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                final var name = rs.getString("name");
                final long price = rs.getLong("price");

                products.add(new Product(name, price));
            }
            return products.get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private long sumOrCount(String sql) {
        try (var statement = createStatement()) {
            var rs = statement.executeQuery(sql);
            return rs.getLong("res");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> getProducts() {
        try (var statement = createStatement()) {
            var rs = statement.executeQuery(SELECT_SQL);
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                final var name = rs.getString("name");
                long price = rs.getLong("price");
                products.add(new Product(name, price));
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addProduct(final Product product) {
        try (Statement statement = createStatement()) {
            var sql = INSERT_SQL + "(\"" + product.getName() + "\"," + product.getPrice() + ")";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product maxProduct() {
        return getMinOrMax(MAX_SQL);
    }

    @Override
    public Product minProduct() {
        return getMinOrMax(MIN_SQL);
    }

    @Override
    public long sumPrices() {
        return sumOrCount(SUM_SQL);
    }

    @Override
    public long countProducts() {
        return sumOrCount(COUNT_SQL);
    }
}