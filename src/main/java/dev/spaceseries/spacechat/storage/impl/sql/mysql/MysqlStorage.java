package dev.spaceseries.spacechat.storage.impl.sql.mysql;

import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.logging.wrap.LogChatWrap;
import dev.spaceseries.spacechat.logging.wrap.LogType;
import dev.spaceseries.spacechat.logging.wrap.LogWrapper;
import dev.spaceseries.spacechat.model.User;
import dev.spaceseries.spacechat.storage.Storage;
import dev.spaceseries.spacechat.storage.impl.sql.mysql.factory.MysqlConnectionManager;
import dev.spaceseries.spacechat.util.date.DateUtil;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static dev.spaceseries.spacechat.config.Config.STORAGE_MYSQL_TABLES_CHAT_LOGS;

public class MysqlStorage implements Storage {

    public static final String LOG_CHAT_CREATION_STATEMENT = "CREATE TABLE IF NOT EXISTS `%s` (\n" +
            "`uuid` TEXT NOT NULL,\n" +
            "`name` TEXT,\n" +
            "`message` TEXT,\n" +
            "`date` TEXT NOT NULL,\n" +
            "`id` INT NOT NULL AUTO_INCREMENT,\n" +
            "PRIMARY KEY (`id`)\n" +
            ");";
    private static final String LOG_CHAT = "INSERT INTO " + STORAGE_MYSQL_TABLES_CHAT_LOGS.get(Config.get()) + " (uuid, name, message, date) VALUES(?, ?, ?, ?)";
    public static final String USERS_CREATION_STATEMENT = "CREATE TABLE IF NOT EXISTS `%s` (\n" +
            "`uuid` TEXT NOT NULL,\n" +
            "`username` TEXT NOT NULL,\n" +
            "`date` TEXT NOT NULL,\n" +
            "`id` INT NOT NULL AUTO_INCREMENT,\n" +
            "PRIMARY KEY (`id`)\n" +
            ");";
    private static final String CREATE_USER = "INSERT INTO " + Config.STORAGE_MYSQL_TABLES_USERS.get(Config.get()) + " (uuid, username, date) VALUES(?, ?, ?)";
    private static final String SELECT_USER = "SELECT * FROM " + Config.STORAGE_MYSQL_TABLES_USERS.get(Config.get()) + " WHERE uuid=?";
    private static final String SELECT_USER_USERNAME = "SELECT * FROM " + Config.STORAGE_MYSQL_TABLES_USERS.get(Config.get()) + " WHERE username=?";
    private static final String UPDATE_USER = "UPDATE " + Config.STORAGE_MYSQL_TABLES_USERS.get(Config.get()) + " SET username=? WHERE uuid=?";

    /**
     * The connection manager
     */
    private final MysqlConnectionManager mysqlConnectionManager;

    /**
     * Initializes new mysql storage
     */
    public MysqlStorage() {
        // initialize new connection manager
        mysqlConnectionManager = new MysqlConnectionManager();
        this.mysqlConnectionManager.init();
    }

    @Override
    public void log(LogWrapper data, boolean async) {
        // if chat
        if (data.getLogType() == LogType.CHAT) {
            logChat((LogChatWrap) data, async);
        }
    }

    /**
     * Gets a user
     *
     * @param uuid uuid
     * @return user
     */
    @Override
    public User getUser(UUID uuid) {
        // create prepared statement
        try (Connection connection = mysqlConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER)) {
            // replace
            preparedStatement.setString(1, uuid.toString());

            // execute
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                // create new user
                User user = new User(uuid, Bukkit.getOfflinePlayer(uuid).getName(), new Date());
                createUser(user);
                return user;
            }

            // build user and return
            String username = resultSet.getString("username");
            Date date = DateUtil.fromString(resultSet.getString("date"));

            return new User(uuid, username, date);
        } catch (SQLException throwables) {
            throwables.printStackTrace();

            return null;
        }
    }

    /**
     * Gets a user by their username
     *
     * @param username username
     * @return user
     */
    @Override
    public User getUser(String username) {
        // create prepared statement
        try (Connection connection = mysqlConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_USERNAME)) {
            // replace
            preparedStatement.setString(1, username);

            // execute
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            // build user and return
            UUID uuid = UUID.fromString(resultSet.getString("uuid"));
            Date date = DateUtil.fromString(resultSet.getString("date"));

            return new User(uuid, username, date);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a new user in the database
     *
     * @param user user
     */
    private void createUser(User user) {
        // create prepared statement
        try (Connection connection = mysqlConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USER)) {
            // replace
            preparedStatement.setString(1, user.getUuid().toString());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, DateUtil.toString(user.getDate()));

            // execute
            preparedStatement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Updates a user
     *
     * @param user user
     */
    @Override
    public void updateUser(User user) {
        // create prepared statement
        try (Connection connection = mysqlConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER)) {
            // replace
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getUuid().toString());

            // execute
            preparedStatement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void close() {
        this.mysqlConnectionManager.shutdown();
    }

    /**
     * Logs chat to the MySQL database
     *
     * @param data  The data
     * @param async async
     */
    private void logChat(LogChatWrap data, boolean async) {
        Runnable task = () -> {
            // create prepared statement
            try (Connection connection = mysqlConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(LOG_CHAT)) {
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
        };


        if (async)
            Bukkit.getScheduler().runTaskAsynchronously(SpaceChat.getInstance(), task);
        else
            Bukkit.getScheduler().runTask(SpaceChat.getInstance(), task);
    }
}
