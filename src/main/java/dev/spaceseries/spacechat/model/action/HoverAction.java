package dev.spaceseries.spacechat.model.action;

import com.google.common.base.Joiner;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.List;

public class HoverAction {

    /**
     * The list of lines in the
     * hover action
     */
    private List<String> lines;

    /**
     * Construct hover action
     *
     * @param lines lines
     */
    public HoverAction(List<String> lines) {
        this.lines = lines;
    }

    /**
     * Construct hover action
     */
    public HoverAction() {
    }

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

    /**
     * Returns lines
     *
     * @return lines
     */
    public List<String> getLines() {
        return lines;
    }

    /**
     * Sets lines
     *
     * @param lines lines
     */
    public void setLines(List<String> lines) {
        this.lines = lines;
    }
}
