package dev.spaceseries.spacechat.storage.impl.sql.mysql;

import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.logging.wrap.LogChatWrap;
import dev.spaceseries.spacechat.logging.wrap.LogType;
import dev.spaceseries.spacechat.logging.wrap.LogWrapper;
import dev.spaceseries.spacechat.model.Channel;
import dev.spaceseries.spacechat.model.User;
import dev.spaceseries.spacechat.storage.Storage;
import dev.spaceseries.spacechat.storage.impl.sql.mysql.factory.MysqlConnectionManager;
import dev.spaceseries.spacechat.util.date.DateUtil;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static dev.spaceseries.spacechat.config.Config.STORAGE_MYSQL_TABLES_CHAT_LOGS;

public class MysqlStorage extends Storage {

    public static final String LOG_CHAT_CREATION_STATEMENT = "CREATE TABLE IF NOT EXISTS `%s` (\n" +
            "`uuid` TEXT NOT NULL,\n" +
            "`name` TEXT,\n" +
            "`message` TEXT,\n" +
            "`date` TEXT NOT NULL,\n" +
            "`id` INT NOT NULL AUTO_INCREMENT,\n" +
            "PRIMARY KEY (`id`)\n" +
            ");";
    private final String LOG_CHAT = "INSERT INTO " + STORAGE_MYSQL_TABLES_CHAT_LOGS.get(plugin.getSpaceChatConfig().getConfig()) + " (uuid, name, message, date) VALUES(?, ?, ?, ?);";
    public static final String USERS_CREATION_STATEMENT = "CREATE TABLE IF NOT EXISTS `%s` (\n" +
            "`uuid` TEXT NOT NULL,\n" +
            "`username` TEXT NOT NULL,\n" +
            "`date` TEXT NOT NULL,\n" +
            "`id` INT NOT NULL AUTO_INCREMENT,\n" +
            "PRIMARY KEY (`id`)\n" +
            ");";
    private final String CREATE_USER = "INSERT INTO " + Config.STORAGE_MYSQL_TABLES_USERS.get(plugin.getSpaceChatConfig().getConfig()) + " (uuid, username, date) VALUES(?, ?, ?);";
    private final String SELECT_USER = "SELECT * FROM " + Config.STORAGE_MYSQL_TABLES_USERS.get(plugin.getSpaceChatConfig().getConfig()) + " WHERE uuid=?;";
    private final String SELECT_USER_USERNAME = "SELECT * FROM " + Config.STORAGE_MYSQL_TABLES_USERS.get(plugin.getSpaceChatConfig().getConfig()) + " WHERE username=?;";
    private final String UPDATE_USER = "UPDATE " + Config.STORAGE_MYSQL_TABLES_USERS.get(plugin.getSpaceChatConfig().getConfig()) + " SET username=? WHERE uuid=?;";
    public static final String USERS_SUBSCRIBED_CHANNELS_CREATION_STATEMENT = "CREATE TABLE IF NOT EXISTS `%s` (\n" +
            "`uuid` TEXT NOT NULL,\n" +
            "`channel` TEXT NOT NULL,\n" +
            "`id` INT NOT NULL AUTO_INCREMENT,\n" +
            "PRIMARY KEY (`id`)\n" +
            ");";
    private final String SELECT_SUBSCRIBED_CHANNELS = "SELECT channel FROM " + Config.STORAGE_MYSQL_TABLES_SUBSCRIBED_CHANNELS.get(plugin.getSpaceChatConfig().getConfig()) + " WHERE uuid=?;";
    private final String DELETE_SUBSCRIBED_CHANNEL = "DELETE FROM " + Config.STORAGE_MYSQL_TABLES_SUBSCRIBED_CHANNELS.get(plugin.getSpaceChatConfig().getConfig()) + " WHERE uuid=? AND channel=?;";
    private final String INSERT_SUBSCRIBED_CHANNEL = "INSERT INTO " + Config.STORAGE_MYSQL_TABLES_SUBSCRIBED_CHANNELS.get(plugin.getSpaceChatConfig().getConfig()) + " (uuid, channel) VALUES(?, ?);";

    /**
     * The connection manager
     */
    private final MysqlConnectionManager mysqlConnectionManager;

    /**
     * Initializes new mysql storage
     */
    public MysqlStorage(SpaceChat plugin) {
        super(plugin);
        // initialize new connection manager
        mysqlConnectionManager = new MysqlConnectionManager(plugin);
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
                User user = new User(plugin, uuid, Bukkit.getOfflinePlayer(uuid).getName(), new Date(), new ArrayList<>());
                createUser(user);
                return user;
            }

            // build user and return
            String username = resultSet.getString("username");
            Date date = DateUtil.fromString(resultSet.getString("date"));

            // get channels that are subscribed
            List<Channel> subscribedChannels = getSubscribedChannels(uuid);

            return new User(plugin, uuid, username, date, subscribedChannels);
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
        try (Connection connection = mysqlConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SUBSCRIBED_CHANNELS)) {
            // replace
            preparedStatement.setString(1, uuid.toString());

            // execute
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Channel> channels = new ArrayList<>();

            while (resultSet.next()) {
                String channelHandle = resultSet.getString("channel");

                Channel channel = plugin.getChannelManager().get(channelHandle, null);
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

            // get channels that are subscribed
            List<Channel> subscribedChannels = getSubscribedChannels(uuid);

            return new User(plugin, uuid, username, date, subscribedChannels);
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

            // delete remaining channels that shouldn't be there
            List<Channel> serverSideSubscribedList = getSubscribedChannels(user.getUuid());

            serverSideSubscribedList.forEach(serverSideSubscribedChannel -> {
                if (user.getSubscribedChannels().stream()
                        .noneMatch(c -> c.getHandle().equals(serverSideSubscribedChannel.getHandle()))) {
                    deleteChannelRow(user.getUuid(), serverSideSubscribedChannel);
                }
            });

            user.getSubscribedChannels().forEach(channel -> {
                if (serverSideSubscribedList.stream()
                        .anyMatch(c -> c.getHandle().equals(channel.getHandle()))) {
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
        try (Connection connection = mysqlConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SUBSCRIBED_CHANNEL)) {
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
        try (Connection connection = mysqlConnectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SUBSCRIBED_CHANNEL)) {
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
            Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        else
            Bukkit.getScheduler().runTask(plugin, task);
    }
}
