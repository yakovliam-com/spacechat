package dev.spaceseries.spacechat.storage.impl.sql.file.sqlite;

import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.logging.wrap.LogChatWrap;
import dev.spaceseries.spacechat.logging.wrap.LogType;
import dev.spaceseries.spacechat.logging.wrap.LogWrapper;
import dev.spaceseries.spacechat.model.Channel;
import dev.spaceseries.spacechat.model.User;
import dev.spaceseries.spacechat.storage.Storage;
import dev.spaceseries.spacechat.storage.impl.sql.file.sqlite.factory.SqliteConnectionManager;
import dev.spaceseries.spacechat.util.date.DateUtil;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SqliteStorage implements Storage {

    public static final String LOG_CHAT_CREATION_STATEMENT = "CREATE TABLE IF NOT EXISTS `%s` (\n" +
            "`uuid` TEXT NOT NULL,\n" +
            "`name` TEXT,\n" +
            "`message` TEXT,\n" +
            "`date` TEXT NOT NULL,\n" +
            "`id` INTEGER PRIMARY KEY AUTOINCREMENT\n" +
            ");";
    private static final String LOG_CHAT = "INSERT INTO " + Config.STORAGE_SQLITE_TABLES_CHAT_LOGS.get(Config.get()) + " (uuid, name, message, date) VALUES(?, ?, ?, ?)";
    public static final String USERS_CREATION_STATEMENT = "CREATE TABLE IF NOT EXISTS `%s` (\n" +
            "`uuid` TEXT NOT NULL,\n" +
            "`username` TEXT NOT NULL,\n" +
            "`date` TEXT NOT NULL,\n" +
            "`id` INTEGER PRIMARY KEY AUTOINCREMENT\n" +
            ");";
    private static final String CREATE_USER = "INSERT INTO " + Config.STORAGE_SQLITE_TABLES_USERS.get(Config.get()) + " (uuid, username, date) VALUES(?, ?, ?)";
    private static final String SELECT_USER = "SELECT * FROM " + Config.STORAGE_SQLITE_TABLES_USERS.get(Config.get()) + " WHERE uuid=?";
    private static final String SELECT_USER_USERNAME = "SELECT * FROM " + Config.STORAGE_SQLITE_TABLES_USERS.get(Config.get()) + " WHERE username=?";
    private static final String UPDATE_USER = "UPDATE " + Config.STORAGE_SQLITE_TABLES_USERS.get(Config.get()) + " SET username=? WHERE uuid=?";
    public static final String USERS_SUBSCRIBED_CHANNELS_CREATION_STATEMENT = "CREATE TABLE IF NOT EXISTS `%s` (\n" +
            "`uuid` TEXT NOT NULL,\n" +
            "`channel` TEXT NOT NULL,\n" +
            "`id` INTEGER PRIMARY KEY AUTOINCREMENT\n" +
            ");";
    private static final String SELECT_SUBSCRIBED_CHANNELS = "SELECT channel FROM " + Config.STORAGE_MYSQL_TABLES_SUBSCRIBED_CHANNELS.get(Config.get()) + " WHERE uuid=?;";
    private static final String DELETE_SUBSCRIBED_CHANNEL = "DELETE FROM " + Config.STORAGE_MYSQL_TABLES_SUBSCRIBED_CHANNELS.get(Config.get()) + " WHERE uuid=? AND channel=?;";
    private static final String INSERT_SUBSCRIBED_CHANNEL = "INSERT INTO " + Config.STORAGE_MYSQL_TABLES_SUBSCRIBED_CHANNELS.get(Config.get()) + " (uuid, channel) VALUES(?, ?);";

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
        this.sqliteConnectionManager = new SqliteConnectionManager(sqliteStorageFile);
        sqliteConnectionManager.init();
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
        try (Connection connection = sqliteConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER)) {
            // replace
            preparedStatement.setString(1, uuid.toString());

            // execute
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                // create new user
                User user = new User(uuid, Bukkit.getOfflinePlayer(uuid).getName(), new Date(), new ArrayList<>());
                createUser(user);
                return user;
            }

            // build user and return
            String username = resultSet.getString("username");
            Date date = DateUtil.fromString(resultSet.getString("date"));

            // get channels that are subscribed
            List<Channel> subscribedChannels = getSubscribedChannels(uuid);

            return new User(uuid, username, date, subscribedChannels);
        } catch (SQLException throwables) {
            throwables.printStackTrace();

            return null;
        }
    }

    /**
     * Returns subscribed channels
     *
     * @return channels
     */
    private List<Channel> getSubscribedChannels(UUID uuid) {
        try (Connection connection = sqliteConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SUBSCRIBED_CHANNELS)) {
            // replace
            preparedStatement.setString(1, uuid.toString());

            // execute
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Channel> channels = new ArrayList<>();

            while (resultSet.next()) {
                String channelHandle = resultSet.getString("channel");

                Channel channel = SpaceChat.getInstance().getChannelManager().get(channelHandle, null);
                if (channel != null) {
                    channels.add(channel);
                }
            }

            return channels;
        } catch (SQLException throwables) {
            throwables.printStackTrace();

            return new ArrayList<>();
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
        try (Connection connection = sqliteConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_USERNAME)) {
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

            // get channels that are subscribed
            List<Channel> subscribedChannels = getSubscribedChannels(uuid);

            return new User(uuid, username, date, subscribedChannels);
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
        try (Connection connection = sqliteConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USER)) {
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
        try (Connection connection = sqliteConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER)) {
            // replace
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getUuid().toString());

            // execute
            preparedStatement.execute();

            // delete remaining channels that shouldn't be there
            List<Channel> serverSideSubscribedList = getSubscribedChannels(user.getUuid());

            serverSideSubscribedList.forEach(serverSideSubscribedChannel -> {
                if (user.getSubscribedChannels().stream().noneMatch(c -> c.getHandle().equals(serverSideSubscribedChannel.getHandle()))) {
                    deleteChannelRow(user.getUuid(), serverSideSubscribedChannel);
                }
            });

            user.getSubscribedChannels().forEach(channel -> {
                if (serverSideSubscribedList.stream().anyMatch(c -> c.getHandle().equals(channel.getHandle()))) {
                    return;
                }
                insertChannelRow(user.getUuid(), channel);
            });

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Inserts a channel row
     *
     * @param uuid    uuid
     * @param channel channel
     */
    private void insertChannelRow(UUID uuid, Channel channel) {
        // create prepared statement
        try (Connection connection = sqliteConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SUBSCRIBED_CHANNEL)) {
            // replace
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, channel.getHandle());

            // execute
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Deletes a channel row
     *
     * @param uuid    uuid
     * @param channel channel
     */
    private void deleteChannelRow(UUID uuid, Channel channel) {
        // create prepared statement
        try (Connection connection = sqliteConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SUBSCRIBED_CHANNEL)) {
            // replace
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, channel.getHandle());

            // execute
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void close() {
        this.sqliteConnectionManager.shutdown();
    }

    /**
     * Logs chat to the SqLite database
     *
     * @param async async
     * @param data  The data
     */
    private void logChat(LogChatWrap data, boolean async) {
        Runnable task = () -> {
            // create prepared statement
            try (Connection connection = sqliteConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(LOG_CHAT)) {
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

