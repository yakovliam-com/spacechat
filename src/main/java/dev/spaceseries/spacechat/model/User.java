package dev.spaceseries.spacechat.model;

import java.util.Date;
import java.util.UUID;

public final class User {

    /**
     * UUID
     */
    private final UUID uuid;

    /**
     * Username
     */
    private final String username;

    /**
     * Time/Instant that the user was created / joined for the first time
     */
    private final Date date;

    /**
     * @param uuid     uuid
     * @param username username
     * @param date     date
     */
    public User(UUID uuid, String username, Date date) {
        this.username = username;
        this.uuid = uuid;
        this.date = date;
    }

    /**
     * Returns username
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns uuid
     *
     * @return uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Returns date
     *
     * @return date
     */
    public Date getDate() {
        return date;
    }
}
