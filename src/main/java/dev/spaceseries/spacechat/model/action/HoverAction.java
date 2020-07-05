package dev.spaceseries.spacechat.model.action;

import dev.spaceseries.api.text.mini.MiniMessageParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class HoverAction {

    /**
     * The list of lines in the
     * hover action
     */
    @Getter
    @Setter
    private List<String> lines;

    /**
     * Converts the hover action to a BungeeCord / Spigot hover event
     *
     * @param player The player
     * @return The hover event
     */
    public HoverEvent toHoverEvent(Player player) {
        // parse action
        HoverEvent.Action action = HoverEvent.Action.SHOW_TEXT;

        // parse text to value using MiniMessage lib
        ComponentBuilder componentBuilder = new ComponentBuilder("");

        // parse line and append
        int i = 0;
        for (String line : lines) {
            // append parsed & replace placeholders
            componentBuilder.append(
                    ChatColor.translateAlternateColorCodes('&',
                            PlaceholderAPI.setPlaceholders(player, line)
                    )
            );

            // if NOT last iteration
            if (i < lines.size() - 1) {
                componentBuilder.append("\n");
            }

            // incr
            i++;
        }

        // build & return
        return new HoverEvent(action, componentBuilder.create());
    }
}
