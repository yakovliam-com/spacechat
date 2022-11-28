package dev.spaceseries.spacechat.parser;

import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.parser.itemchat.ItemChatParser;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class MessageParser {

    /**
     * Available parsers
     */
    private final List<Parser> parsers;

    /**
     * Construct message parser
     */
    public MessageParser(SpaceChatPlugin plugin) {
        this.parsers = Collections.singletonList(new ItemChatParser(plugin));
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
        for (Parser parser : parsers) {
            input = parser.parse(player, input);
        }
        return input;
    }
}
