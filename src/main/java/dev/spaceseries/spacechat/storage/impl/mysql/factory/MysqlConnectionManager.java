package dev.spaceseries.spacechat.storage.impl.mysql.factory;

import dev.spaceseries.spacechat.configuration.Config;
import dev.spaceseries.spacechat.storage.impl.mysql.MysqlStorage;
import dev.spaceseries.spacechat.storage.impl.mysql.SqlAble;
import dev.spaceseries.spacechat.storage.impl.mysql.factory.o.ConnectionInfo;
import dev.spaceseries.spacechat.storage.impl.mysql.factory.o.Credentials;
import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;

import static dev.spaceseries.spacechat.configuration.Config.*;

public final class MysqlConnectionManager extends SqlAble {

    /**
     * The connection info
     */
    @Getter
    private final ConnectionInfo connectionInfo;

    /**
     * The connection
     */
    private Connection connection;

    /**
     * Initializes Mysql Connection Manager
     */
    public MysqlConnectionManager() {
        // create connection info
        connectionInfo = new ConnectionInfo(
                MYSQL_ADDRESS.get(Config.get()),
                MYSQL_PORT.get(Config.get()),
                MYSQL_DATABASE.get(Config.get()),
                new Credentials(
                        MYSQL_USERNAME.get(Config.get()),
                        MYSQL_PASSWORD.get(Config.get())
                ),
                MYSQL_USE_SSL.get(Config.get()),
                MYSQL_VERIFY_SERVER_CERTIFICATE.get(Config.get())
        );

        // If not exists, create chat logging table
        try {
            execute(connectionInfo.getDataSource().getConnection(), String.format(MysqlStorage.LOG_CHAT_CREATION_STATEMENT, MYSQL_TABLES_CHAT_LOGS.get(Config.get())));
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
}
