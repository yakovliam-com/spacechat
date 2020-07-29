package dev.spaceseries.spacechat.logging.wrap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class LogWrapper {

    /**
     * The type of log
     */
    @Getter
    @Setter
    private LogType logType;
}
