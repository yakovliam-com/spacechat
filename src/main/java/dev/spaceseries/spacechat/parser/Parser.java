package dev.spaceseries.spacechat.parser;

import dev.spaceseries.spacechat.SpaceChatPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public abstract class Parser {

    protected final SpaceChatPlugin plugin;

    public Parser(SpaceChatPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Parse
     * @param player  player
     * @param message message
     * @return component
     */
    public abstract Component parse(Player player, Component message);
}
