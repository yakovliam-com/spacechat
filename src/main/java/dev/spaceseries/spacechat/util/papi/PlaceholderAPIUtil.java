package dev.spaceseries.spacechat.util.papi;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.OfflinePlayer;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderAPIUtil {

    /**
     * Sets placeholders to text
     * I had to make this method in order to bypass the PlaceholderAPI bug that colors text even with the `colorize` boolean set to false
     *
     * @param player  player
     * @param text    text
     * @param pattern pattern
     * @return modified string
     */
    public static String setPlaceholders(OfflinePlayer player, String text, Pattern pattern) {
        Matcher m = pattern.matcher(text);
        Map<String, PlaceholderHook> hooks = PlaceholderAPI.getPlaceholders();

        while (m.find()) {
            String format = m.group(1);
            int index = format.indexOf("_");
            if (index > 0 && index < format.length()) {
                String identifier = format.substring(0, index).toLowerCase();
                String params = format.substring(index + 1);
                if (hooks.containsKey(identifier)) {
                    String value = hooks.get(identifier).onRequest(player, params);
                    if (value != null) {
                        text = text.replaceAll(Pattern.quote(m.group()), Matcher.quoteReplacement(value));
                    }
                }
            }
        }

        return text;
    }
}
