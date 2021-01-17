package dev.spaceseries.spacechat.model.action;

import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
    public HoverEvent<Component> toHoverEvent(Player player) {
        // parse action
        HoverEvent.Action<Component> action = HoverEvent.Action.SHOW_TEXT;

        String line = PlaceholderAPI.setPlaceholders(player, Joiner.on("\n").join(lines));

        // build & return
        return HoverEvent.hoverEvent(action, LegacyComponentSerializer.legacyAmpersand().deserialize(line));
    }
}
