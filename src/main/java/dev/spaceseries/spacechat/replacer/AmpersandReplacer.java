package dev.spaceseries.spacechat.replacer;

import org.bukkit.entity.Player;

public class AmpersandReplacer implements Replacer {

    /**
     * Replaces & with \u00A7 &
     *
     * @param input            input
     * @param applicablePlayer player
     * @return applied text
     */
    @Override
    public String apply(String input, Player applicablePlayer) {
        return input.replace("&", "\u00A7");
    }
}

