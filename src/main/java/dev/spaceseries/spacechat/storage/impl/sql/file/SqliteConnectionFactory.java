package dev.spaceseries.spacechat.storage.impl.sql.file;

import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.internal.dependency.IsolatedClassLoader;

import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class SqliteConnectionFactory extends FlatfileConnectionFactory {
    private Constructor<?> connectionConstructor;

    public SqliteConnectionFactory(Path file) {
        super(file);
    }

    @Override
    public void init() {
        IsolatedClassLoader classLoader = SpaceChat.getInstance().getDependencyInstantiation().getDependencyManagement().getIsolatedClassLoader();
        try {
            Class<?> connectionClass = classLoader.loadClass("org.sqlite.jdbc4.JDBC4Connection");
            this.connectionConstructor = connectionClass.getConstructor(String.class, String.class, Properties.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Connection createConnection(Path file) throws SQLException {
        try {
            return (Connection) this.connectionConstructor.newInstance("jdbc:sqlite:" + file.toString(), file.toString(), new Properties());
        } catch (ReflectiveOperationException e) {
            if (e.getCause() instanceof SQLException) {
                throw (SQLException) e.getCause();
            }
            throw new RuntimeException(e);
        }
    }
}