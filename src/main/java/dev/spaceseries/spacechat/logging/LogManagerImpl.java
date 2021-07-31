package dev.spaceseries.spacechat.logging;

import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.api.config.generic.adapter.ConfigurationAdapter;
import dev.spaceseries.spacechat.config.SpaceChatConfigKeys;
import dev.spaceseries.spacechat.logging.wrap.LogChatWrap;
import dev.spaceseries.spacechat.logging.wrap.LogToType;
import dev.spaceseries.spacechat.logging.wrap.LogType;
import dev.spaceseries.spacechat.logging.wrap.LogWrapper;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.logging.Level;

public class LogManagerImpl implements LogManager {

    private final SpaceChatPlugin plugin;

    public LogManagerImpl(SpaceChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public <T> void log(T t, LogType logType, LogToType logToType) {
        log(t, logType, logToType, null);
    }

    public <T, K> void log(T t, LogType logType, LogToType logToType, K optional) {
        if (logType == LogType.CHAT) {
            switch (logToType) {
                case CONSOLE:
                    if (t instanceof String && optional instanceof AsyncPlayerChatEvent) {
                        global((String) t, (AsyncPlayerChatEvent) optional);
                    } else if (t instanceof String && optional == null) {
                        global((String) t);
                    }
                    break;
                case STORAGE:
                    if (SpaceChatConfigKeys.LOGGING_CHAT_LOG_TO_STORAGE.get(getConfig()) && t instanceof LogChatWrap) {
                        storage((LogChatWrap) t);
                    }
                    break;
            }
        }
    }

    /**
     * Logs chat
     *
     * @param s The chat message
     * @param e The chat event
     */
    private void global(String s, AsyncPlayerChatEvent e) {
        // set & escape
        e.setFormat(s.replace("%", "%%"));
    }

    /**
     * Logs chat
     *
     * @param s The chat message
     */
    private void global(String s) {
        plugin.getLogger().log(Level.INFO, s);
    }

    /**
     * Log to storage
     *
     * @param data The data
     */
    private void storage(LogWrapper data) {
        // get storage manager and log
        plugin.getStorageManager().getCurrent().log(data, true);
    }

    /**
     * Gets the main configuration from the main class
     *
     * @return The main configuration
     */
    private ConfigurationAdapter getConfig() {
        return plugin.getSpaceChatConfig().getAdapter();
    }
}
