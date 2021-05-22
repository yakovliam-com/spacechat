package dev.spaceseries.spacechat.builder.live;

import dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.ComponentBuilder;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.TextComponent;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.minimessage.MiniMessage;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.serializer.legacy.LegacyComponentSerializer;
import dev.spaceseries.spaceapi.util.Quad;
import dev.spaceseries.spacechat.builder.Builder;
import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.model.Extra;
import dev.spaceseries.spacechat.model.Format;
import dev.spaceseries.spacechat.parser.MessageParser;
import dev.spaceseries.spacechat.replacer.AmpersandReplacer;
import dev.spaceseries.spacechat.replacer.SectionReplacer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import static dev.spaceseries.spacechat.config.Config.PERMISSIONS_USE_CHAT_COLORS;

public class RelationalLiveChatFormatBuilder implements Builder<Quad<Player, Player, String, Format>, TextComponent>, LiveChatFormatBuilder {

    /**
     * Ampersand replacer
     */
    private static final AmpersandReplacer AMPERSAND_REPLACER = new AmpersandReplacer();

    /**
     * Section replacer
     */
    private static final SectionReplacer SECTION_REPLACER = new SectionReplacer();

    /**
     * Builds an array of baseComponents from a message, player, and format
     *
     * @param input The trio of inputs
     * @return The array of baseComponents
     */
    @Override
    public TextComponent build(Quad<Player, Player, String, Format> input) {
        // get input parameters
        Player player = input.getA();
        Player player2 = input.getB();
        String messageString = input.getC();
        Format format = input.getD();

        // create component builder for message
        ComponentBuilder<TextComponent, TextComponent.Builder> componentBuilder = Component.text();

        // loop through format parts
        format.getFormatParts().forEach(formatPart -> {
            // create component builder
            ComponentBuilder<TextComponent, TextComponent.Builder> partComponentBuilder = Component.text();
            // if the part has "line", it is a SINGLE MiniMessage...in that case, just parse & return (continues to next part if exists, which it shouldn't)
            if (formatPart.getLine() != null) {
                // replace placeholders
                String mmWithPlaceholdersReplaced = SECTION_REPLACER.apply(PlaceholderAPI.setPlaceholders(player, AMPERSAND_REPLACER.apply(formatPart.getLine(), player)), player);
                // replace relational and apply section replacer
                mmWithPlaceholdersReplaced = SECTION_REPLACER.apply(PlaceholderAPI.setRelationalPlaceholders(player, player2, mmWithPlaceholdersReplaced), player);

                // get chat message (formatted)
                String chatMessage = LegacyComponentSerializer
                        .legacySection()
                        .serialize(player.hasPermission(PERMISSIONS_USE_CHAT_COLORS.get(Config.get())) ? // if player has permission to use chat colors
                                LegacyComponentSerializer // yes, the player has permission to use chat colors, so color message
                                        .legacyAmpersand()
                                        .deserialize(messageString) :
                                Component.text(messageString)); // no, the player doesn't have permission to use chat colors, so just return the message (not colored)

                // parse message
                Component message = new MessageParser().parse(player, Component.text(chatMessage));

                // parse miniMessage
                Component parsedMiniMessage = MiniMessage.get().parse(mmWithPlaceholdersReplaced);

                // replace chat message
                parsedMiniMessage = parsedMiniMessage.replaceText((text) -> text.match("<chat_message>").replacement(message));

                // parse MiniMessage into builder
                partComponentBuilder.append(parsedMiniMessage);
                // append partComponentBuilder to main builder
                componentBuilder.append(partComponentBuilder.build());
                return;
            }

            String text = formatPart.getText();

            // basically what I am doing here is converting & -> section, then replacing placeholders, then section -> &
            // this just bypasses PAPI's hacky way of coloring text which shouldn't even be implemented...
            text = SECTION_REPLACER.apply(PlaceholderAPI.setPlaceholders(player, AMPERSAND_REPLACER.apply(text, player)), player);
            // set relational placeholders
            text = SECTION_REPLACER.apply(PlaceholderAPI.setRelationalPlaceholders(player, player2, text), player);


            // build text from legacy (and replace <chat_message> with the actual message)
            // and check permissions for chat colors
            Component parsedText = player.hasPermission(PERMISSIONS_USE_CHAT_COLORS.get(Config.get())) ? LegacyComponentSerializer.legacyAmpersand().deserialize(
                    text.replace("<chat_message>", messageString)) :
                    LegacyComponentSerializer.legacyAmpersand().deserialize(text).replaceText((b) -> b.match("<chat_message>")
                            .replacement(messageString));

            // parse message
            parsedText = new MessageParser().parse(player, parsedText);

            /* Retaining events for MULTIPLE components */

            // parse extra (if applicable)
            if (formatPart.getExtra() != null) {
                Extra extra = formatPart.getExtra();

                // if contains click action
                if (extra.getClickAction() != null) {
                    // apply
                    parsedText = parsedText.clickEvent(extra.getClickAction().toClickEventRelational(player, player2));
                }

                // if contains hover action
                if (extra.getHoverAction() != null) {
                    // apply
                    parsedText = parsedText.hoverEvent(extra.getHoverAction().toHoverEventRelational(player, player2));
                }
            }

            partComponentBuilder.append(parsedText);

            // append build partComponentBuilder to main componentBuilder
            componentBuilder.append(partComponentBuilder.build());
        });

        // return built component builder
        return componentBuilder.build();
    }
}
