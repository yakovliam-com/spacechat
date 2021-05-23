package dev.spaceseries.spacechat.storage.impl.sql.file.sqlite.factory;

import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.storage.impl.sql.file.SqliteConnectionFactory;
import dev.spaceseries.spacechat.storage.impl.sql.file.sqlite.SqliteStorage;
import dev.spaceseries.spacechat.storage.impl.sql.mysql.SqlHelper;

import java.sql.SQLException;

import static dev.spaceseries.spacechat.config.Config.*;

public final class SqliteConnectionManager extends SqliteConnectionFactory {

    /**
     * Initializes Mysql Connection Manager
     */
    public SqliteConnectionManager(SqliteStorage.SqliteStorageFile sqliteStorageFile) {
        super(sqliteStorageFile.getFile().toPath());
    }


    @Override
    public void init() {
        System.out.println("\n\nIn sqlite connection manager init\n\n");
        super.init();

        // If not exists, create chat logging table
        try {
            SqlHelper.execute(getConnection(), String.format(SqliteStorage.LOG_CHAT_CREATION_STATEMENT, STORAGE_SQLITE_TABLES_CHAT_LOGS.get(Config.get())));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}