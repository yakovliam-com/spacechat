package dev.spaceseries.spacechat.model.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;

@AllArgsConstructor
@NoArgsConstructor
public class ClickAction {

    /**
     * The type of action for the click action
     */
    @Getter
    @Setter
    private ClickActionType clickActionType;

    /**
     * The value applied to the click action type when the
     * event is fired
     */
    @Getter
    @Setter
    private String value;

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
}
