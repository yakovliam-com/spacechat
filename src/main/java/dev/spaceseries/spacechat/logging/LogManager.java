package dev.spaceseries.spacechat.logging;

import dev.spaceseries.api.config.impl.Configuration;
import dev.spaceseries.spacechat.SpaceChat;

import java.util.logging.Level;
import java.util.logging.Logger;

import static dev.spaceseries.spacechat.configuration.Config.*;

public class LogManager implements ILogManager {

    @Override
    public <T> void log(T t, LogType logType) {
        switch (logType) {
            case CHAT:
                if(LOGGING_CHAT_LOG_TO_CONSOLE.get(getConfig())) {
                    global((String) t);
                }
                break;
        }

    }

    /**
     * Logs chat
     * @param s The chat message
     */
    private void global(String s) {
        Logger.getGlobal().log(Level.INFO, s);
    }

    /**
     * Gets the main configuration from the main class
     * @return The main configuration
     */
    private Configuration getConfig(){
        return SpaceChat.getInstance().getSpaceChatConfig().getConfig();
    }
}
