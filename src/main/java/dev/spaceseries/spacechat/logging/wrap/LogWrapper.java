package dev.spaceseries.spacechat.logging.wrap;

public class LogWrapper {

    /**
     * The type of log
     */
    private LogType logType;

    /**
     * Construct log wrapper
     *
     * @param logType log type
     */
    public LogWrapper(LogType logType) {
        this.logType = logType;
    }

    /**
     * Returns log type
     *
     * @return log type
     */
    public LogType getLogType() {
        return logType;
    }

    /**
     * Sets log type
     *
     * @param logType log type
     */
    public void setLogType(LogType logType) {
        this.logType = logType;
    }
}
