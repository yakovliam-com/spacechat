package dev.spaceseries.spacechat.logging;

public interface ILogManager {

    <T> void log(T t, LogType logType);

}
