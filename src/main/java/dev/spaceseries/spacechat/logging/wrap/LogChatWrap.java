package dev.spaceseries.spacechat.logging.wrap;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

public class LogChatWrap extends LogWrapper {

    /**
     * The sender's name
     */
    @Getter
    @Setter
    private String senderName;

    /**
     * The sender's UUID
     */
    @Getter
    @Setter
    private UUID senderUUID;

    /**
     * The actual message contents of the chat
     */
    @Getter
    @Setter
    private String message;

    /**
     * The date at which the chat message was sent
     */
    @Getter
    @Setter
    private Date at;

    /**
     * Creates a new log chat wrapper
     *
     * @param logType    The log type
     * @param senderName The sender name
     * @param senderUUID The sender uuid
     * @param message    The message
     * @param at         The time
     */
    public LogChatWrap(LogType logType, String senderName, UUID senderUUID, String message, Date at) {
        super(logType);

        this.senderName = senderName;
        this.senderUUID = senderUUID;
        this.message = message;
        this.at = at;
    }
}
