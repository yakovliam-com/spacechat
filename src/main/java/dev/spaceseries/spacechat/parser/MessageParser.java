package dev.spaceseries.spacechat.parser;

import dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component;
import dev.spaceseries.spaceapi.util.Pair;
import dev.spaceseries.spacechat.parser.itemchat.ItemChatParser;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class MessageParser {

    /**
     * Available parsers
     */
    private static final List<Parser<Pair<Player, Component>, Component>> parsers = Collections.singletonList(
            new ItemChatParser()
    );

    /**
     * Construct message parser
     */
    public MessageParser() {
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
