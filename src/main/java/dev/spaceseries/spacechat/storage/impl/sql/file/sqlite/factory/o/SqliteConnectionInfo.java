package dev.spaceseries.spacechat.storage.impl.sql.file.sqlite.factory.o;

import dev.spaceseries.spaceapi.lib.hikari.HikariConfig;
import dev.spaceseries.spaceapi.lib.hikari.HikariDataSource;

public final class SqliteConnectionInfo {

    /**
     * The database path
     */
    private final String path;

    /**
     * The hikari config
     */
    private final HikariConfig config;

    /**
     * The hikari data source
     */
    private final HikariDataSource dataSource;

    /**
     * Initializes new connection info
     */
    public SqliteConnectionInfo(String path) {
        this.path = path;
        // build config
        config = new HikariConfig();

        // create jdbc url
        String jdbc = "jdbc:sqlite:" + path;


        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl(jdbc);

        // create data source
        dataSource = new HikariDataSource(config);
    }

    /**
     * Returns database path
     *
     * @return database path
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns config
     *
     * @return config
     */
    public HikariConfig getConfig() {
        return config;
    }

    /**
     * Returns datasource
     *
     * @return datasource
     */
    public HikariDataSource getDataSource() {
        return dataSource;
    }
}
