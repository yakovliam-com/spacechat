package dev.spaceseries.spacechat.storage.impl.sql.mysql.factory;

import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.storage.impl.ConnectionFactory;
import dev.spaceseries.spacechat.storage.impl.sql.mysql.MysqlStorage;
import dev.spaceseries.spacechat.storage.impl.sql.mysql.SqlHelper;
import dev.spaceseries.spacechat.storage.impl.sql.mysql.factory.o.MysqlConnectionInfo;
import dev.spaceseries.spacechat.storage.impl.sql.mysql.factory.o.MysqlCredentials;

import java.sql.Connection;
import java.sql.SQLException;

import static dev.spaceseries.spacechat.config.SpaceChatConfigKeys.*;

public final class MysqlConnectionManager extends ConnectionFactory {

    /**
     * The connection info
     */
    private final MysqlConnectionInfo connectionInfo;

    /**
     * Initializes Mysql Connection Manager
     */
    public MysqlConnectionManager(SpaceChat plugin) {
        super(plugin);

        // create connection info
        connectionInfo = new MysqlConnectionInfo(
                STORAGE_MYSQL_ADDRESS.get(plugin.getSpaceChatConfig().getAdapter()),
                STORAGE_MYSQL_PORT.get(plugin.getSpaceChatConfig().getAdapter()),
                STORAGE_MYSQL_DATABASE.get(plugin.getSpaceChatConfig().getAdapter()),
                new MysqlCredentials(
                        STORAGE_MYSQL_USERNAME.get(plugin.getSpaceChatConfig().getAdapter()),
                        STORAGE_MYSQL_PASSWORD.get(plugin.getSpaceChatConfig().getAdapter())
                ),
                STORAGE_MYSQL_USE_SSL.get(plugin.getSpaceChatConfig().getAdapter()),
                STORAGE_MYSQL_VERIFY_SERVER_CERTIFICATE.get(plugin.getSpaceChatConfig().getAdapter())
        );
    }

    @Override
    public void init() {
        // If not exists, create chat logging table
        SqlHelper.execute(getConnection(), String.format(MysqlStorage.LOG_CHAT_CREATION_STATEMENT, STORAGE_MYSQL_TABLES_CHAT_LOGS.get(plugin.getSpaceChatConfig().getAdapter())));
        SqlHelper.execute(getConnection(), String.format(MysqlStorage.USERS_CREATION_STATEMENT, STORAGE_MYSQL_TABLES_USERS.get(plugin.getSpaceChatConfig().getAdapter())));
        SqlHelper.execute(getConnection(), String.format(MysqlStorage.USERS_SUBSCRIBED_CHANNELS_CREATION_STATEMENT, STORAGE_MYSQL_TABLES_SUBSCRIBED_CHANNELS.get(plugin.getSpaceChatConfig().getAdapter())));
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
