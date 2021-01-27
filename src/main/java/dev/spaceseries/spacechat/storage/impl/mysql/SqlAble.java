package dev.spaceseries.spacechat.storage.impl.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * If extended, the child class can call an execution method
 */
public abstract class SqlAble {

    protected void execute(Connection connection, String sql, Object... replacements) {

        // try catch
        try {
            // prepare
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            int i = 1;
            // replace
            for (Object replacement : replacements) {

                // if string
                if (replacement instanceof String) {
                    preparedStatement.setString(i, (String) replacement);
                }

                // if int
                else if (replacement instanceof Integer) {
                    preparedStatement.setInt(i, (Integer) replacement);
                }

                // if double
                else if (replacement instanceof Double) {
                    preparedStatement.setDouble(i, (Double) replacement);
                }

                // if float
                else if (replacement instanceof Float) {
                    preparedStatement.setFloat(i, (Float) replacement);
                }

                i++;
            }

            // execute
            preparedStatement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    protected ResultSet executeQuery(Connection connection, String sql, Object... replacements) {

        // try catch
        try {
            // prepare
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            int i = 1;
            // replace
            for (Object replacement : replacements) {

                // if string
                if (replacement instanceof String) {
                    preparedStatement.setString(i, (String) replacement);
                }

                // if int
                else if (replacement instanceof Integer) {
                    preparedStatement.setInt(i, (Integer) replacement);
                }

                // if double
                else if (replacement instanceof Double) {
                    preparedStatement.setDouble(i, (Double) replacement);
                }

                // if float
                else if (replacement instanceof Float) {
                    preparedStatement.setFloat(i, (Float) replacement);
                }

                i++;
            }

            // execute
            return preparedStatement.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
