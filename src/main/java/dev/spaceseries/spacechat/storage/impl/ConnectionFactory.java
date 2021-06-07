package dev.spaceseries.spacechat.storage.impl;

import dev.spaceseries.spacechat.SpaceChat;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ConnectionFactory {

    protected final SpaceChat plugin;

    public ConnectionFactory(SpaceChat plugin) {
        this.plugin = plugin;
    }

    public abstract void init();

    public abstract void shutdown() throws Exception;

    public abstract Connection getConnection() throws SQLException;

}