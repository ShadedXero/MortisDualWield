package com.mortisdevelopment.mortisdualwield.databases;

import java.sql.*;
import java.util.Properties;

public abstract class Database {

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    public Database(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public Properties getProperties() {
        Properties properties = new Properties();
        if (username != null) {
            properties.setProperty("user", username);
        }
        if (password != null) {
            properties.setProperty("password", password);
        }
        return properties;
    }

    public abstract Connection getConnection();

    public void execute(String sql) {
        try {
            Statement statement = getConnection().createStatement();
            statement.execute(sql);
        }catch (SQLException exp) {
            exp.printStackTrace();
        }
    }

    public void execute(String sql, Object... objects) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i + 1, objects[i]);
            }
            statement.execute();
        }catch (SQLException exp) {
            exp.printStackTrace();
        }
    }

    public void update(String sql) {
        try {
            Statement statement = getConnection().createStatement();
            statement.executeUpdate(sql);
        }catch (SQLException exp) {
            exp.printStackTrace();
        }
    }

    public ResultSet update(String sql, Object... objects) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i + 1, objects[i]);
            }
            statement.executeUpdate();
            return statement.getResultSet();
        }catch (SQLException exp) {
            exp.printStackTrace();
        }
        return null;
    }

    public ResultSet query(String sql) {
        try {
            Statement statement = getConnection().createStatement();
            statement.executeQuery(sql);
            return statement.getResultSet();
        }catch (SQLException exp) {
            exp.printStackTrace();
        }
        return null;
    }

    public void query(String sql, Object... objects) {
        try {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i + 1, objects[i]);
            }
            statement.executeQuery();
        }catch (SQLException exp) {
            exp.printStackTrace();
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
