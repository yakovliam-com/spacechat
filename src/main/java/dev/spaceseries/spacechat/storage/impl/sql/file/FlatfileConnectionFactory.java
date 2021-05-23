package dev.spaceseries.spacechat.storage.impl.sql.file;

import dev.spaceseries.spacechat.storage.impl.ConnectionFactory;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;

/**
 * Abstract {@link ConnectionFactory} using a file based database driver.
 */
abstract class FlatfileConnectionFactory implements ConnectionFactory {
    /** Format used for formatting database file size. */
    protected static final DecimalFormat FILE_SIZE_FORMAT = new DecimalFormat("#.##");

    /** The current open connection, if any */
    private NonClosableConnection connection;
    /** The path to the database file */
    private final Path file;

    FlatfileConnectionFactory(Path file) {
        this.file = file;
    }

    /**
     * Creates a connection to the database.
     *
     * @param file the database file
     * @return the connection
     * @throws SQLException if any error occurs
     */
    protected abstract Connection createConnection(Path file) throws SQLException;

    @Override
    public synchronized Connection getConnection() throws SQLException {
        NonClosableConnection connection = this.connection;
        if (connection == null || connection.isClosed()) {
            connection = new NonClosableConnection(createConnection(this.file));
            this.connection = connection;
        }
        return connection;
    }

    @Override
    public void shutdown() {
        if (this.connection != null) {
            try {
                this.connection.shutdown();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * Gets the path of the file the database driver actually ends up writing to.
     *
     * @return the write file
     */
    protected Path getWriteFile() {
        return this.file;
    }
}