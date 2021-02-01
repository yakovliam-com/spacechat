package dev.spaceseries.spacechat.builder.live;

import dev.spaceseries.api.util.Trio;
import dev.spaceseries.spacechat.builder.Builder;
import dev.spaceseries.spacechat.configuration.Config;
import dev.spaceseries.spacechat.model.Extra;
import dev.spaceseries.spacechat.model.Format;
import dev.spaceseries.spacechat.replacer.AmpersandReplacer;
import dev.spaceseries.spacechat.replacer.SectionReplacer;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import static dev.spaceseries.spacechat.configuration.Config.PERMISSIONS_USE_CHAT_COLORS;

public class LiveChatFormatBuilder implements Builder<Trio<Player, String, Format>, TextComponent> {

    /**
     * Ampersand replacer
     */
    private static final AmpersandReplacer ampersandReplacer = new AmpersandReplacer();

    /**
     * Section replacer
     */
    private static final SectionReplacer sectionReplacer = new SectionReplacer();

    /**
     * Builds an array of baseComponents from a message, player, and format
     *
     * @param input The trio of inputs
     * @return The array of baseComponents
     */
    @Override
    public TextComponent build(Trio<Player, String, Format> input) {
        // get input parameters
        Player player = input.getLeft();
        String message = input.getMid();
        Format format = input.getRight();

        // create component builder for message
        ComponentBuilder<TextComponent, TextComponent.Builder> componentBuilder = Component.text();

        // loop through format parts
        format.getFormatParts().forEach(formatPart -> {
            // create component builder
            ComponentBuilder<TextComponent, TextComponent.Builder> partComponentBuilder = Component.text();
            // if the part has "line", it is a SINGLE MiniMessage...in that case, just parse & return (continues to next part if exists, which it shouldn't)
            if (formatPart.getLine() != null) {
                partComponentBuilder.append(
                        MiniMessage.get()
                                .parse(sectionReplacer.apply(PlaceholderAPI.setPlaceholders(player, ampersandReplacer.apply(formatPart.getLine(), player)), player) // replace placeholders
                                        .replace("<chat_message>", // replace <chat_message>
                                                LegacyComponentSerializer
                                                        .legacySection()
                                                        .serialize(player.hasPermission(PERMISSIONS_USE_CHAT_COLORS.get(Config.get())) ? // if player has permission to use chat colors
                                                                LegacyComponentSerializer // color message
                                                                        .legacyAmpersand()
                                                                        .deserialize(message) :
                                                                Component.text(message)))) // else, just return the message (not colored)

                );
                // append partComponentBuilder to main builder
                componentBuilder.append(partComponentBuilder.build());
                return;
            }

            String text = formatPart.getText();

            // basically what I am doing here is converting & -> section, then replacing placeholders, then section -> &
            // this just bypasses PAPI's hacky way of coloring text which shouldn't even be implemented...
            text = sectionReplacer.apply(PlaceholderAPI.setPlaceholders(player, ampersandReplacer.apply(text, player)), player);

            // build text from legacy (and replace <chat_message> with the actual message)
            // and check permissions for chat colors
            Component parsedText = player.hasPermission(PERMISSIONS_USE_CHAT_COLORS.get(Config.get())) ? LegacyComponentSerializer.legacyAmpersand().deserialize(
                    text.replace("<chat_message>", message)) :
                    LegacyComponentSerializer.legacyAmpersand().deserialize(text).replaceText((b) -> b.match("<chat_message>")
                            .replacement(message));

            /* Retaining events for MULTIPLE components */

            // parse extra (if applicable)
            if (formatPart.getExtra() != null) {
                Extra extra = formatPart.getExtra();

                // if contains click action
                if (extra.getClickAction() != null) {
                    // apply
                    parsedText = parsedText.clickEvent(extra.getClickAction().toClickEvent(player));
                }

                // if contains hover action
                if (extra.getHoverAction() != null) {
                    // apply
                    parsedText = parsedText.hoverEvent(extra.getHoverAction().toHoverEvent(player));
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
