package dev.spaceseries.spacechat.storage.impl.mysql.factory.o;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

public final class ConnectionInfo {

    /**
     * The url to connect to
     */
    @Getter
    private final String address;

    /**
     * The port
     */
    @Getter
    private final int port;

    /**
     * The database name
     */
    @Getter
    private final String database;

    /**
     * The connection credentials
     */
    @Getter
    private final Credentials credentials;

    /**
     * The hikari config
     */
    @Getter
    private HikariConfig config;

    /**
     * The hikari data source
     */
    @Getter
    private HikariDataSource dataSource;

    /**
     * Initializes new connection info
     */
    public ConnectionInfo(String address, int port, String database, Credentials credentials) {
        this.address = address;
        this.port = port;
        this.database = database;
        this.credentials = credentials;
        // build config
        config = new HikariConfig();

        // create jdbc url
        String jdbc = "jdbc:mysql://" + this.address + ":" + this.port + "/" + this.database;

        config.setJdbcUrl(jdbc);
        config.setUsername(this.credentials.getUsername());
        config.setPassword(this.credentials.getPassword());

        // create data source
        dataSource = new HikariDataSource(config);
    }
}
