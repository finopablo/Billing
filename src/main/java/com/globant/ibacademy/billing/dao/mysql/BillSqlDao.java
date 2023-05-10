package com.globant.ibacademy.billing.dao.mysql;

import com.globant.ibacademy.billing.dao.BillDao;
import com.globant.ibacademy.billing.dao.SQLDao;
import com.globant.ibacademy.billing.exceptions.DataAccessException;
import com.globant.ibacademy.billing.model.Bill;
import com.globant.ibacademy.billing.model.Item;
import com.globant.ibacademy.billing.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;


import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Repository
@Slf4j
public class BillSqlDao  implements BillDao, SQLDao<Bill> {
    final Connection connection;

    @Autowired
    public BillSqlDao(Connection conn) {
        connection = conn;
    }

    @Override
    public Optional<Bill> findByNumber(Integer number) throws DataAccessException {
        try (
            PreparedStatement ps = connection.prepareStatement("select * from bills where number = ?")
            ) {
            ps.setInt(1, number);
            ResultSet rs = ps.executeQuery();
            Bill b = rs.next() ? get(rs) : null;
            return Optional.ofNullable(b);
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error getting BillNumber=%d", number), e);
        }
    }

    @Override
    public List<Bill> findByClient(String client) throws DataAccessException {
        List<Bill> list = new LinkedList<>();
        try  (
                PreparedStatement ps = connection.prepareStatement("select * from bills where client = ?");
        ) {
            ps.setString(1, client);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(get(rs));
            }
              return list;
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error getting client = %s bills", client), e);
        }
    }

    @Override
    public Bill save(Bill data)  {
        if (Objects.isNull(data)) throw new IllegalArgumentException("Bill data can't be null");

        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement("insert into bills(number, date, customer) values (?,?,?)", RETURN_GENERATED_KEYS)) {
                ps.setInt(1, data.number());
                ps.setTimestamp(2, Timestamp.valueOf(data.date()));
                ps.setString(3, data.customer());
                ps.execute();
                Integer billId = getId(ps);
                for (Item i : data.items()) {
                    insertItem(billId, i);
                }
                connection.commit();

                return new Bill(billId, data.number(), data.date(), data.customer(), data.items());
            }
        } catch(SQLException e) {
            try {
                connection.rollback();
            } catch(SQLException rollbackException) {
                log.info("Rollback exception");
            }
            throw new DataAccessException(String.format("Error when trying to save bill %s ", data), e );
        }
    }

    @Override
    public void delete(Integer id) throws DataAccessException {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement psItems = connection.prepareStatement("delete from items where id = ?")) {
                psItems.setInt(1, id);
                psItems.execute();

                try (PreparedStatement psBills = connection.prepareStatement("delete from bills where id = ?")) {
                    psBills.setInt(1, id);
                    psBills.execute();
                }
                connection.commit();
            }
        } catch(SQLException e) {
            throw new DataAccessException(String.format("Error deleting Bill id=%d", id), e);
        }
    }

    @Override
    public Optional<Bill> findById(Integer id) throws DataAccessException {
      try (
          PreparedStatement ps = connection.prepareStatement("select * from bills where id = ?");
      ) {
          ps.setInt(1, id);
          ResultSet rs = ps.executeQuery();
          Bill b = rs.next()?get(rs):null;
          return Optional.ofNullable(b);
      } catch (SQLException e) {
          throw new DataAccessException(String.format("Error getting BillId=%s", id), e);
      }

    }

    @Override
    public List<Bill> findAll() throws DataAccessException {

        List<Bill> list = new LinkedList<>();
        try (
            PreparedStatement ps = connection.prepareStatement("select * from bills");
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(get(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DataAccessException("Error getting all bills", e);
        }
    }

    private Bill get(ResultSet rs) throws SQLException {
        return
                    new Bill(rs.getInt("id"),
                    rs.getInt("number"),
                    rs.getTimestamp("date").toLocalDateTime(),
                    rs.getString("customer"),
                    getItems(rs.getInt("id")));

    }

    protected Set<Item> getItems(final Integer billId) throws SQLException {
        Set<Item> items = new HashSet<>();

        try (PreparedStatement ps = connection.prepareStatement("select i.id_product, i.id_bill, qty, i.price as item_price, p.price as product_price,  p.name, p.description, p.price as product_price  from items i inner join products p on i.id_product = p.id where i.id_bill = ?");) {
            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new Item(
                        new Product(rs.getInt("id_product"), rs.getString("name"), rs.getString("description"), rs.getDouble("product_price")),
                        rs.getLong("qty"),
                        rs.getDouble("item_price")));
            }
            return items;
        }
    }

    private void insertItem(Integer billId, Item i) throws SQLException {
        try (PreparedStatement psItem = connection.prepareStatement("insert into items(id_bill, id_product, qty, price) values (?,?,?,?);")) {
            psItem.setInt(1, billId);
            psItem.setInt(2, i.product().id());
            psItem.setLong(3, i.qty());
            psItem.setDouble(4, i.price());
            psItem.execute();
        }

    }

}
