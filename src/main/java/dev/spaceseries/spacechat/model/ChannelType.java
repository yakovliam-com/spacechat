package dev.spaceseries.spacechat.model;

public enum ChannelType {

    NORMAL("channels");

    private final String sectionKey;

    ChannelType(String sectionKey) {
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
