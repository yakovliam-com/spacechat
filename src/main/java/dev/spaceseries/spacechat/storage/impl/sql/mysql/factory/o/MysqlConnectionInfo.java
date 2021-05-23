package dev.spaceseries.spacechat.storage.impl.sql.mysql.factory.o;

import dev.spaceseries.spaceapi.lib.hikari.HikariConfig;
import dev.spaceseries.spaceapi.lib.hikari.HikariDataSource;

public final class MysqlConnectionInfo {

    /**
     * The url to connect to
     */
    private final String address;

    /**
     * The port
     */
    private final int port;

    /**
     * The database name
     */
    private final String database;

    /**
     * The connection credentials
     */
    private final MysqlCredentials credentials;

    /**
     * Use ssl?
     */
    private final boolean useSSL;

    /**
     * Verify server certificate?
     */
    private final boolean verifyServerCertificate;

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
    public MysqlConnectionInfo(String address, int port, String database, MysqlCredentials credentials, boolean useSSL, boolean verifyServerCertificate) {
        this.address = address;
        this.port = port;
        this.database = database;
        this.credentials = credentials;
        this.useSSL = useSSL;
        this.verifyServerCertificate = verifyServerCertificate;
        // build config
        config = new HikariConfig();

        // create jdbc url
        String jdbc = "jdbc:mysql://" + this.address + ":" + this.port + "/" + this.database;

        config.setJdbcUrl(jdbc);
        config.setUsername(this.credentials.getUsername());
        config.setPassword(this.credentials.getPassword());

        config.addDataSourceProperty("useSSL", this.useSSL);
        config.addDataSourceProperty("verifyServerCertificate", this.verifyServerCertificate);

        // create data source
        dataSource = new HikariDataSource(config);
    }

    /**
     * Returns address
     *
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns port
     *
     * @return port
     */
    public int getPort() {
        return port;
    }

    /**
     * Returns database
     *
     * @return database
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Returns credentials
     *
     * @return credentials
     */
    public MysqlCredentials getCredentials() {
        return credentials;
    }

    /**
     * Returns is using ssl
     *
     * @return is using ssl
     */
    public boolean isUseSSL() {
        return useSSL;
    }

    /**
     * Returns is verify server certificate
     *
     * @return is verify server certificate
     */
    public boolean isVerifyServerCertificate() {
        return verifyServerCertificate;
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
