package com.globant.ibacademy.billing;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class TestUtil {


    public static Connection createConnection() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException, IOException {
        Class.forName("org.h2.Driver").getDeclaredConstructor().newInstance();
        Connection conn = DriverManager.getConnection("jdbc:h2:mem://localhost/~/test", "sa", "");
        InputStream inStream = TestUtil.class.getClassLoader().getResourceAsStream("testdb.sql");
        Scanner reader = new Scanner(inStream);
        reader.useDelimiter(";");
        while (reader.hasNext()) {
            conn.createStatement().execute(reader.next());
        }
        reader.close();
        inStream.close();
        return conn;
    }
}
