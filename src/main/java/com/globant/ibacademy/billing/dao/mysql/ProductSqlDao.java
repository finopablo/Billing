package com.globant.ibacademy.billing.dao.mysql;

import com.globant.ibacademy.billing.dao.ProductDao;
import com.globant.ibacademy.billing.dao.SQLDao;
import com.globant.ibacademy.billing.exceptions.DataAccessException;
import com.globant.ibacademy.billing.model.Product;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Slf4j
public final class ProductSqlDao implements ProductDao, SQLDao<Product> {

    final Connection connection;

    public ProductSqlDao(Connection conn) {
        this.connection = conn;
    }

    @Override
    public Product save(Product data) throws DataAccessException {
        if (Objects.isNull(data)) throw new IllegalArgumentException("Product can't be null");
        try {
            if (Objects.isNull(data.id())) {
                log.info("Adding new product {}", data);
                return insertProduct(data);
            } else {
                updateProduct(data);
                return data;
            }
        } catch(SQLException e) {
            throw new DataAccessException(String.format("Error inserting product %s", data),e);
        }
    }

    @Override
    public void delete(Integer id) throws DataAccessException {
        try (
                PreparedStatement ps = connection.prepareStatement("delete from products where id = ?");
        ) {
            ps.setInt(1, id);
            ps.execute();
            log.info("Deleted product id = {}", id);
        } catch (SQLException e){
            throw new DataAccessException(String.format("Error deleting product ID %d", id), e);
        }
    }

    @Override
    public Optional<Product> findById(Integer id) throws DataAccessException {
        try (
                PreparedStatement ps = connection.prepareStatement("select id, name, description, price from products where id = ?");
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            final Product p = rs.next() ? get(rs):null;
            return Optional.ofNullable(p);
        } catch (SQLException e){
            throw new DataAccessException(String.format("Error returning product ID %d" ,id) ,e);
        }
    }

    @Override
    public List<Product> findAll() throws DataAccessException {
        List<Product> list = new LinkedList<>();
        try (
            PreparedStatement ps = connection.prepareStatement("select id, name, description, price from products");
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                    list.add(get(rs));
            }
            return list;
        } catch(SQLException e){
            throw new DataAccessException("Database error finding all Products",e);
        }
    }


    private void updateProduct(Product data) throws SQLException {
        log.info("Updating product {}", data);
        try (
            PreparedStatement ps = connection.prepareStatement("update products set name = ?, description = ?, price = ?  where id = ?")
        ) {
            ps.setString(1, data.name());
            ps.setString(2, data.description());
            ps.setDouble(3, data.price());
            ps.setInt(4, data.id());
            ps.execute();
        }
    }

    private Product insertProduct(Product data) throws SQLException {
        try (
            PreparedStatement ps = connection.prepareStatement("insert into products values (default,?,?,?)", RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, data.name());
            ps.setString(2, data.description());
            ps.setDouble(3, data.price());
            ps.execute();
            return new Product(getId(ps), data.name(), data.description(), data.price());
        }
    }



    private Product get(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDouble("price"));
    }
}
