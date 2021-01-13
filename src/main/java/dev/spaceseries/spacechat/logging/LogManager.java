package dev.spaceseries.spacechat.logging;

import dev.spaceseries.api.config.impl.Configuration;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.logging.wrap.LogChatWrap;
import dev.spaceseries.spacechat.logging.wrap.LogToType;
import dev.spaceseries.spacechat.logging.wrap.LogType;
import dev.spaceseries.spacechat.logging.wrap.LogWrapper;

import java.util.logging.Level;
import java.util.logging.Logger;

import static dev.spaceseries.spacechat.configuration.Config.*;

public class LogManager implements ILogManager {

    @Override
    public <T> void log(T t, LogType logType, LogToType logToType) {
        if (logType == LogType.CHAT) {
            switch (logToType) {
                case CONSOLE:
                    if (LOGGING_CHAT_LOG_TO_CONSOLE.get(getConfig()) && t instanceof String) {
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
     */
    private void global(String s) {
        Logger.getGlobal().log(Level.INFO, s);
    }

    /**
     * Log to storage
     *
     * @param data The data
     */
    private void storage(LogWrapper data) {
        // get storage manager and log
        SpaceChat.getInstance().getStorageManager().getCurrent().log(data);
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
