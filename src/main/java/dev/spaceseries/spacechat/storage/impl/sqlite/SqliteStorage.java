package dev.spaceseries.spacechat.storage.impl.sqlite;

import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.logging.wrap.LogChatWrap;
import dev.spaceseries.spacechat.logging.wrap.LogType;
import dev.spaceseries.spacechat.logging.wrap.LogWrapper;
import dev.spaceseries.spacechat.storage.Storage;
import dev.spaceseries.spacechat.storage.impl.sqlite.factory.SqliteConnectionManager;
import dev.spaceseries.spacechat.util.date.DateUtil;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class SqliteStorage implements Storage {

    // Creates chat logging table (automatically)
    public static final String LOG_CHAT_CREATION_STATEMENT = "CREATE TABLE IF NOT EXISTS `%s` (\n" +
            "`uuid` TEXT NOT NULL,\n" +
            "`name` TEXT,\n" +
            "`message` TEXT,\n" +
            "`date` TEXT NOT NULL,\n" +
            "`id` INTEGER PRIMARY KEY AUTOINCREMENT\n" +
            ");";

    // Logs chat into the 'chat logging' database table
    private static final String LOG_CHAT = "INSERT INTO " + Config.STORAGE_SQLITE_TABLES_CHAT_LOGS.get(Config.get()) + " (uuid, name, message, date) VALUES(?, ?, ?, ?)";

    /**
     * The connection manager
     */
    private final SqliteConnectionManager sqliteConnectionManager;

    /**
     * Initializes new mysql storage
     */
    public SqliteStorage() {
        // storage file
        SqliteStorageFile sqliteStorageFile = new SqliteStorageFile();
        // initialize new connection manager
        sqliteConnectionManager = new SqliteConnectionManager(sqliteStorageFile);
    }

    @Override
    public void log(LogWrapper data) {
        // if chat
        if (data.getLogType() == LogType.CHAT) {
            logChat((LogChatWrap) data);
        }
    }

    /**
     * Logs chat to the SqLite database
     *
     * @param data The data
     */
    private void logChat(LogChatWrap data) {
        // async logging with sqlite
        Bukkit.getScheduler().runTaskAsynchronously(SpaceChat.getInstance(), () -> {
            // create prepared statement
            try (Connection connection = sqliteConnectionManager.getConnection(); PreparedStatement preparedStatement = Objects.requireNonNull(connection).prepareStatement(LOG_CHAT)) {
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
        });
    }

    public static final class SqliteStorageFile {

        /**
         * File
         */
        private final File file;

        public SqliteStorageFile() {
            this.file = new File(SpaceChat.getInstance().getDataFolder() + File.separator + "storage", Config.STORAGE_SQLITE_DATABASE.get(Config.get()) + ".db");
            if (!this.file.exists()) {
                this.file.getParentFile().mkdirs();
                try {
                    this.file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Returns file
         *
         * @return file
         */
        public File getFile() {
            return file;
        }
    }

}

