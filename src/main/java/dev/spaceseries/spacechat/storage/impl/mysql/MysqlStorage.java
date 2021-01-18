package dev.spaceseries.spacechat.storage.impl.mysql;

import dev.spaceseries.spacechat.configuration.Config;
import dev.spaceseries.spacechat.logging.wrap.LogChatWrap;
import dev.spaceseries.spacechat.logging.wrap.LogType;
import dev.spaceseries.spacechat.logging.wrap.LogWrapper;
import dev.spaceseries.spacechat.storage.Storage;
import dev.spaceseries.spacechat.storage.impl.mysql.factory.MysqlConnectionManager;
import dev.spaceseries.spacechat.util.date.DateUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static dev.spaceseries.spacechat.configuration.Config.MYSQL_TABLES_CHAT_LOGS;

public class MysqlStorage implements Storage {

    // Creates chat logging table (automatically)
    public static final String LOG_CHAT_CREATION_STATEMENT = "CREATE TABLE IF NOT EXISTS `%s` (\n" +
            "`uuid` TEXT NOT NULL,\n" +
            "`name` TEXT,\n" +
            "`message` TEXT,\n" +
            "`date` TEXT NOT NULL,\n" +
            "`id` INT NOT NULL AUTO_INCREMENT,\n" +
            "PRIMARY KEY (`id`)\n" +
            ");";

    // Logs chat into the 'chat logging' database table
    private static final String LOG_CHAT = "INSERT INTO " + MYSQL_TABLES_CHAT_LOGS.get(Config.get()) + " (uuid, name, message, date) VALUES(?, ?, ?, ?)";

    /**
     * The connection manager
     */
    private MysqlConnectionManager mysqlConnectionManager;

    /**
     * Initializes new mysql storage
     */
    public MysqlStorage() {
        // initialize new connection manager
        mysqlConnectionManager = new MysqlConnectionManager();
    }

    @Override
    public void log(LogWrapper data) {
        // if chat
        if (data.getLogType() == LogType.CHAT) {
            logChat((LogChatWrap) data);
        }
    }

    /**
     * Logs chat to the MySQL database
     *
     * @param data The data
     */
    private void logChat(LogChatWrap data) {
        // create prepared statement
        try {
            PreparedStatement preparedStatement = mysqlConnectionManager.getConnection().prepareStatement(LOG_CHAT);
            // replace
            preparedStatement.setString(1, data.getSenderUUID().toString());
            preparedStatement.setString(2, data.getSenderName());
            preparedStatement.setString(3, data.getMessage());
            preparedStatement.setString(4, DateUtil.toString(data.getAt()));

            // execute
            preparedStatement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
