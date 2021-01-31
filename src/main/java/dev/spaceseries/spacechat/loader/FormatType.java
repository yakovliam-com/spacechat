package dev.spaceseries.spacechat.loader;

public enum FormatType {

    CHAT("chat"),
    JOIN("join"),
    LEAVE("leave");

    private final String sectionKey;

    FormatType(String sectionKey) {
        this.sectionKey = sectionKey;
    }

    /**
     * Returns section key
     *
     * @return section key
     */
    public String getSectionKey() {
        return sectionKey;
    }
}
