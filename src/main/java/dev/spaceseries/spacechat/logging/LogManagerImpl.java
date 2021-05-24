package dev.spaceseries.spacechat.logging;

import dev.spaceseries.spaceapi.config.impl.Configuration;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.logging.wrap.LogChatWrap;
import dev.spaceseries.spacechat.logging.wrap.LogToType;
import dev.spaceseries.spacechat.logging.wrap.LogType;
import dev.spaceseries.spacechat.logging.wrap.LogWrapper;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.logging.Level;

import static dev.spaceseries.spacechat.config.Config.*;

public class LogManagerImpl implements LogManager {

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
                    if (LOGGING_CHAT_LOG_TO_STORAGE.get(getConfig()) && t instanceof LogChatWrap) {
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
        SpaceChat.getInstance().getLogger().log(Level.INFO, s);
    }

    /**
     * Log to storage
     *
     * @param data The data
     */
    private void storage(LogWrapper data) {
        // get storage manager and log
        SpaceChat.getInstance().getStorageManager().getCurrent().log(data, true);
    }

    /**
     * Gets the main configuration from the main class
     *
     * @return The main configuration
     */
    private Configuration getConfig() {
        return SpaceChat.getInstance().getSpaceChatConfig().getConfig();
    }
}
