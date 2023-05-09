package com.globant.ibacademy.billing.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface  SQLDao<T> extends Dao<T> {

    default Integer getId(Statement ps) throws SQLException {
        ResultSet id = ps.getGeneratedKeys();
        id.next();
        return id.getInt(1);
    }

}
