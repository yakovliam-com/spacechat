package dev.spaceseries.spacechat.model.action;

import me.clip.placeholderapi.PlaceholderAPI;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;

public class ClickAction {

    /**
     * The type of action for the click action
     */
    private ClickActionType clickActionType;

    /**
     * The value applied to the click action type when the
     * event is fired
     */
    private String value;

    /**
     * Construct click action
     *
     * @param clickActionType click action type
     * @param value           value
     */
    public ClickAction(ClickActionType clickActionType, String value) {
        this.clickActionType = clickActionType;
        this.value = value;
    }

    /**
     * Construct click action
     */
    public ClickAction() {
    }

    /**
     * Converts the click action to a BungeeCord / Spigot click event
     *
     * @param player The player
     * @return The click event
     */
    public ClickEvent toClickEvent(Player player) {
        // parse action
        ClickEvent.Action action = ClickEvent.Action.valueOf(clickActionType.toString().toUpperCase());

        // build & return
        return ClickEvent.clickEvent(action, PlaceholderAPI.setPlaceholders(player, value));
    }

    /**
     * Returns click action type
     *
     * @return click action type
     */
    public ClickActionType getClickActionType() {
        return clickActionType;
    }

    /**
     * Sets click action type
     *
     * @param clickActionType click action type
     */
    public void setClickActionType(ClickActionType clickActionType) {
        this.clickActionType = clickActionType;
    }

    /**
     * Returns value
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets value
     *
     * @param value value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
