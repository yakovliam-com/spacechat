package dev.spaceseries.spacechat.model;

public class ChatFormat {

    /**
     * The handle of the format
     * Same as "identifier" meaning
     */
    private String handle;

    /**
     * The priority of the format
     */
    private Integer priority;

    /**
     * The applicable permission node for the format
     */
    private String permission;

    /**
     * Format itself
     */
    private Format format;

    /**
     * Construct chat format
     *
     * @param handle     handle
     * @param priority   priority
     * @param permission permission
     * @param format     format
     */
    public ChatFormat(String handle, Integer priority, String permission, Format format) {
        this.handle = handle;
        this.priority = priority;
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
     * Returns priority
     *
     * @return priority
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * Sets priority
     *
     * @param priority priority
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
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
