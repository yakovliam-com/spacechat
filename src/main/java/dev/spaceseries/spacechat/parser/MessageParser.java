package dev.spaceseries.spacechat.parser;

import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.api.wrapper.Pair;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class MessageParser {

    /**
     * Available parsers
     */
    private final List<Parser<Pair<Player, Component>, Component>> parsers;

    /**
     * Construct message parser
     */
    public MessageParser(SpaceChatPlugin plugin) {
        this.parsers = Collections.emptyList();
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
