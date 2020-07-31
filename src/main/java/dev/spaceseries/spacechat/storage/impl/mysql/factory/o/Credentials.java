package dev.spaceseries.spacechat.storage.impl.mysql.factory.o;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Credentials {

    /**
     * The username
     */
    @Getter
    private final String username;

    /**
     * The password
     */
    @Getter
    private final String password;
}
