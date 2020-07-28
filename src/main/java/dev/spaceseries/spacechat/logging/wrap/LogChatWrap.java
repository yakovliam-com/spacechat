package dev.spaceseries.spacechat.logging.wrap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
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
}
