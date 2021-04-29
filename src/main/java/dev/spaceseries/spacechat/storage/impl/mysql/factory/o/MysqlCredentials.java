package dev.spaceseries.spacechat.storage.impl.mysql.factory.o;

public class MysqlCredentials {

    /**
     * The username
     */
    private final String username;

    /**
     * The password
     */
    private final String password;

    /**
     * Returns username
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns password
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Construct credentials
     *
     * @param username username
     * @param password password
     */
    public MysqlCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
