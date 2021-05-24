package dev.spaceseries.spacechat.model;

import dev.spaceseries.spacechat.model.formatting.Format;

public class Channel {

    /**
     * The handle / identifier of the channel
     * <p>
     * Identical to the format handle
     */
    private String handle;

    /**
     * The applicable permission node for the format
     */
    private String permission;

    /**
     * Format itself
     */
    private Format format;

    /**
     * Construct channel
     *
     * @param handle     handle
     * @param permission permission
     * @param format     format
     */
    public Channel(String handle, String permission, Format format) {
        this.handle = handle;
        this.permission = permission;
        this.format = format;
    }

    /**
     * Returns handle
     *
     * @return handle
     */
    public String getHandle() {
        return handle;
    }

    /**
     * Sets handle
     *
     * @param handle handle
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * Returns permission
     *
     * @return permission
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Sets permission
     *
     * @param permission permission
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * Returns the format
     *
     * @return format
     */
    public Format getFormat() {
        return format;
    }

    /**
     * Sets the format
     *
     * @param format format
     */
    public void setFormat(Format format) {
        this.format = format;
    }

}
