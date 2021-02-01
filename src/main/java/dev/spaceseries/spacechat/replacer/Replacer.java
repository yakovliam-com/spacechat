package dev.spaceseries.spacechat.replacer;

import org.bukkit.entity.Player;

public interface Replacer {

    /**
     * Apply replacement
     *
     * @param input            input
     * @param applicablePlayer player
     * @return applied text
     */
    String apply(String input, Player applicablePlayer);

}
