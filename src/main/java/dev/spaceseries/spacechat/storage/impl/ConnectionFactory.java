package dev.spaceseries.spacechat.storage.impl;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactory {

    void init();

    void shutdown() throws Exception;

    Connection getConnection() throws SQLException;

}