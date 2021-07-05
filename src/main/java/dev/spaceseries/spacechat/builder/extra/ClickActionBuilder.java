package dev.spaceseries.spacechat.builder.extra;

import dev.spaceseries.spaceapi.config.generic.adapter.ConfigurationAdapter;
import dev.spaceseries.spaceapi.util.Pair;
import dev.spaceseries.spacechat.builder.Builder;
import dev.spaceseries.spacechat.model.formatting.action.ClickAction;
import dev.spaceseries.spacechat.model.formatting.action.ClickActionType;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class ClickActionBuilder implements Builder<Pair<String, ConfigurationAdapter>, ClickAction> {

    /**
     * Builds an V (output) from a K (input)
     *
     * @param input The input
     */
    @Override
    public ClickAction build(Pair<String, ConfigurationAdapter> input) {
        ConfigurationAdapter adapter = input.getRight();
        String path = input.getLeft();

        // create object
        ClickAction clickAction = new ClickAction();

        // get the action type
        String actionTypeString = adapter.getString(path + ".action", null);
        ClickActionType clickActionType = null;

        try {
            // get action type from string
            clickActionType = ClickActionType.valueOf(actionTypeString.toUpperCase());
        } catch (Exception ignored) {
            Bukkit.getLogger().log(Level.SEVERE, "Error while parsing Extra " + path + ": " + actionTypeString + " is not a valid extra action");
        }

        // set action type
        clickAction.setClickActionType(clickActionType);

        // get value
        String value = adapter.getString(path + ".value", null);

        // set value
        clickAction.setValue(value);

        // return
        return clickAction;
    }
}
