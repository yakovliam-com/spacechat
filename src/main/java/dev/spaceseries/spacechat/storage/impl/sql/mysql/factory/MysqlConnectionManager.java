package dev.spaceseries.spacechat.storage.impl.sql.mysql.factory;

import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.storage.impl.ConnectionFactory;
import dev.spaceseries.spacechat.storage.impl.sql.mysql.MysqlStorage;
import dev.spaceseries.spacechat.storage.impl.sql.mysql.SqlHelper;
import dev.spaceseries.spacechat.storage.impl.sql.mysql.factory.o.MysqlConnectionInfo;
import dev.spaceseries.spacechat.storage.impl.sql.mysql.factory.o.MysqlCredentials;

import java.sql.Connection;
import java.sql.SQLException;

import static dev.spaceseries.spacechat.config.Config.*;

public final class MysqlConnectionManager implements ConnectionFactory {

    /**
     * The connection info
     */
    private final MysqlConnectionInfo connectionInfo;

    /**
     * Initializes Mysql Connection Manager
     */
    public MysqlConnectionManager() {
        // create connection info
        connectionInfo = new MysqlConnectionInfo(
                STORAGE_MYSQL_ADDRESS.get(Config.get()),
                STORAGE_MYSQL_PORT.get(Config.get()),
                STORAGE_MYSQL_DATABASE.get(Config.get()),
                new MysqlCredentials(
                        STORAGE_MYSQL_USERNAME.get(Config.get()),
                        STORAGE_MYSQL_PASSWORD.get(Config.get())
                ),
                STORAGE_MYSQL_USE_SSL.get(Config.get()),
                STORAGE_MYSQL_VERIFY_SERVER_CERTIFICATE.get(Config.get())
        );
    }

    @Override
    public void init() {
        // If not exists, create chat logging table
        try {
            SqlHelper.execute(connectionInfo.getDataSource().getConnection(), String.format(MysqlStorage.LOG_CHAT_CREATION_STATEMENT, STORAGE_MYSQL_TABLES_CHAT_LOGS.get(Config.get())));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        // close
        this.getConnectionInfo().getDataSource().close();
    }

    /**
     * Gets the connection object
     *
     * @return The connection
     */
    @Override
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
    public MysqlConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }
}