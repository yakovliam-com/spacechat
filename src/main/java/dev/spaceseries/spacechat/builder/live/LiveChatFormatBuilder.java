package dev.spaceseries.spacechat.builder.live;

import dev.spaceseries.api.text.mini.MiniMessageParser;
import dev.spaceseries.api.util.ColorUtil;
import dev.spaceseries.api.util.Trio;
import dev.spaceseries.spacechat.builder.IBuilder;
import dev.spaceseries.spacechat.model.Extra;
import dev.spaceseries.spacechat.model.Format;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

public class LiveChatFormatBuilder implements IBuilder<Trio<Player, Format, String>, BaseComponent[]> {

    /**
     * Builds an array of baseComponents from a message, player, and format
     *
     * @param input The trio of inputs
     * @return The array of baseComponents
     */
    @Override
    public BaseComponent[] build(Trio<Player, Format, String> input) {
        // get input parameters
        Player player = input.getLeft();
        Format format = input.getMid();
        String message = input.getRight();

        // create component builder for message
        ComponentBuilder componentBuilder = new ComponentBuilder("");

        // loop through format parts
        format.getFormatParts().forEach(formatPart -> {
            // create component builder
            ComponentBuilder partComponentBuilder = new ComponentBuilder("");
            // if the part has "line", it is a SINGLE MiniMessage...in that case, just parse & return (continues to next part if exists, which it shouldn't)
            if (formatPart.getLine() != null) {
                partComponentBuilder.append(
                        MiniMessageParser.parseFormat(
                                PlaceholderAPI.setPlaceholders(
                                        player, ColorUtil.translateFromAmpersand(formatPart.getLine()), false)
                                , "chat_message",
                                message)
                );
                // append partComponentBuilder to main builder
                componentBuilder.append(partComponentBuilder.create());
                return;
            }

            String text = formatPart.getText();

            // replace placeholders
            text = PlaceholderAPI.setPlaceholders(player, text, false);

            // build text from legacy (and replace <chat_message> with the actual message)
            BaseComponent[] parsedText = ColorUtil.fromLegacyText(
                    ColorUtil.translateFromAmpersand(
                            text.replace("<chat_message>", message)
                    )
            );
            /* Retaining events for MULTIPLE components */

            // loop through parsedText components, applying events to all
            for (BaseComponent baseComponent : parsedText) {
                // create SUB componentBuilder
                ComponentBuilder subComponentBuilder = new ComponentBuilder(baseComponent);

                // parse extra (if applicable)
                if (formatPart.getExtra() != null) {
                    Extra extra = formatPart.getExtra();

                    // if contains click action
                    if (extra.getClickAction() != null) {
                        // append to builder
                        subComponentBuilder.event(extra.getClickAction().toClickEvent(player));
                    }

                    // if contains hover action
                    if (extra.getHoverAction() != null) {
                        // append to builder
                        subComponentBuilder.event(extra.getHoverAction().toHoverEvent(player));
                    }
                }

                // add SINGLE parsed text to part component builder
                partComponentBuilder.append(subComponentBuilder.create(), ComponentBuilder.FormatRetention.FORMATTING);
            }

            // append build partComponentBuilder to main componentBuilder
            // this was supposed to be FORMATTING retention, but the new ChatColor class is NOT stable enough to support it....it has to be NONE
            componentBuilder.append(partComponentBuilder.create(), ComponentBuilder.FormatRetention.FORMATTING);
        });

        // return built component builder
        return componentBuilder.create();
    }
}
