package dev.spaceseries.spacechat.parser;

import dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component;
import dev.spaceseries.spaceapi.util.Pair;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.parser.itemchat.ItemChatParser;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class MessageParser {

    /**
     * Plugin
     */
    private final SpaceChat plugin;

    /**
     * Available parsers
     */
    private final List<Parser<Pair<Player, Component>, Component>> parsers;

    /**
     * Construct message parser
     */
    public MessageParser(SpaceChat plugin) {
        this.plugin = plugin;
        this.parsers = Collections.singletonList(
                new ItemChatParser(plugin)
        );
    }

    /**
     * Parse
     *
     * @param player player
     * @param input  message input
     * @return component
     */
    public Component parse(Player player, Component input) {
        // loop through parsers
        for (Parser<Pair<Player, Component>, Component> parser : parsers) {
            input = parser.parse(new Pair<>(player, input));
        }
        return input;
    }
}
