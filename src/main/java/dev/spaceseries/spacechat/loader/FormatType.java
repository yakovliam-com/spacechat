package dev.spaceseries.spacechat.loader;

import lombok.Getter;

public enum FormatType {

    CHAT("chat"),
    JOIN("join"),
    LEAVE("leave");

    @Getter
    private final String sectionKey;

    FormatType(String sectionKey) {
        this.sectionKey = sectionKey;
    }
}
