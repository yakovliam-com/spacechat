package dev.spaceseries.spacechat.model.action;

import dev.spaceseries.spaceapi.lib.google.common.base.Joiner;
import dev.spaceseries.spacechat.replacer.AmpersandReplacer;
import dev.spaceseries.spacechat.replacer.SectionReplacer;
import me.clip.placeholderapi.PlaceholderAPI;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.event.HoverEvent;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.List;

public class HoverAction {

    /**
     * Ampersand replacer
     */
    private static final AmpersandReplacer AMPERSAND_REPLACER = new AmpersandReplacer();

    /**
     * Section replacer
     */
    private static final SectionReplacer SECTION_REPLACER = new SectionReplacer();

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
     * Converts the hover action to a BungeeCord / Spigot hover event
     * <p>
     * Uses relational placeholders, instead of regular
     *
     * @param player The player
     * @param player2 The second player
     * @return The hover event
     */
    public HoverEvent<Component> toHoverEventRelational(Player player, Player player2) {
        // parse action
        HoverEvent.Action<Component> action = HoverEvent.Action.SHOW_TEXT;

        String line = Joiner.on("\n").join(lines);

        String text = SECTION_REPLACER.apply(PlaceholderAPI.setPlaceholders(player, AMPERSAND_REPLACER.apply(line, player)), player);
        // set relational placeholders
        text = SECTION_REPLACER.apply(PlaceholderAPI.setRelationalPlaceholders(player, player2, text), player);

        // build & return
        return HoverEvent.hoverEvent(action, LegacyComponentSerializer.legacyAmpersand().deserialize(text));
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
