package com.company;
/* BSD license
Copyright (c) 2015 Jimmy Ruska (jimmyruska@gmail.com)
All rights reserved.

Redistribution and use in source and binary forms are permitted
provided that the above copyright notice and this paragraph are
duplicated in all such forms and that any documentation,
advertising materials, and other materials related to such
distribution and use acknowledge that the software was developed
by the <organization>. The name of the
<organization> may not be used to endorse or promote products derived
from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.*;

public final class SQL {
    private static final String USER = "sa";
    private static JdbcConnectionPool conn;

    public static void create_db(String db) {
        try {
            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL("jdbc:h2:/"+Util.userDir()+"/."+SharedState.APP_NAME_FILE+"/h2");
            ds.setUser(USER);
            ds.setPassword("");
            conn = JdbcConnectionPool.create(ds);

            initDB(conn.getConnection());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void initDB(Connection connection) throws Exception{
        Statement statement = connection.createStatement();
        String qry = Util.readResource("sql/initdb.sql");
        int x = statement.executeUpdate(qry);
        connection.close();
    }

    public static void put(String key, String val) {
        try {
            Connection connection = conn.getConnection();
            PreparedStatement prep = connection.prepareStatement("MERGE INTO KV KEY(\"KEY\") VALUES (?, ?);");
            prep.setString(1, key);
            prep.setString(2, val);
            prep.executeUpdate();
            connection.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        String value = "";
        try {
            Connection connection = conn.getConnection();
            PreparedStatement prep = connection.prepareStatement("SELECT * FROM KV where KEY = ?;");
            prep.setString(1,key);
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                value = rs.getString("VALUE");
            }
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return value;

    }

    public static Connection getConnection () throws Exception {
        return conn.getConnection();
    }
}
