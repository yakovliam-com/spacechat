package dev.spaceseries.spacechat.logging;

import dev.spaceseries.spacechat.logging.wrap.LogToType;
import dev.spaceseries.spacechat.logging.wrap.LogType;

public interface LogManager {

    <T> void log(T t, LogType logType, LogToType logToType);

}
