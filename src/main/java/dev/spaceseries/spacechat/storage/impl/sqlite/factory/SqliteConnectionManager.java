package dev.spaceseries.spacechat.storage.impl.sqlite.factory;

import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.storage.impl.mysql.MysqlStorage;
import dev.spaceseries.spacechat.storage.impl.mysql.SqlAble;
import dev.spaceseries.spacechat.storage.impl.sqlite.SqliteStorage;
import dev.spaceseries.spacechat.storage.impl.sqlite.factory.o.SqliteConnectionInfo;

import java.sql.Connection;
import java.sql.SQLException;

import static dev.spaceseries.spacechat.config.Config.*;

public final class SqliteConnectionManager extends SqlAble {

    /**
     * The connection info
     */
    private final SqliteConnectionInfo connectionInfo;

    /**
     * Initializes Mysql Connection Manager
     */
    public SqliteConnectionManager(SqliteStorage.SqliteStorageFile sqliteStorageFile) {
        // create connection info
        connectionInfo = new SqliteConnectionInfo(
                sqliteStorageFile.getFile().getAbsolutePath()
        );

        // If not exists, create chat logging table
        try {
            execute(connectionInfo.getDataSource().getConnection(), String.format(SqliteStorage.LOG_CHAT_CREATION_STATEMENT, STORAGE_SQLITE_TABLES_CHAT_LOGS.get(Config.get())));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Gets the connection object
     *
     * @return The connection
     */
    public Connection getConnection() {
        try {
            return connectionInfo.getDataSource().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    /**
     * Returns connection info
     *
     * @return connection info
     */
    public SqliteConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }
}
