package dev.spaceseries.spacechat.model;

import java.util.List;

public class Format {

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
     * The list of format parts
     */
    private List<FormatPart> formatParts;

    /**
     * Construct format
     *
     * @param handle      handle
     * @param priority    priority
     * @param permission  permission
     * @param formatParts format parts
     */
    public Format(String handle, Integer priority, String permission, List<FormatPart> formatParts) {
        this.handle = handle;
        this.priority = priority;
        this.permission = permission;
        this.formatParts = formatParts;
    }

    /**
     * Construct format
     */
    public Format() {
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
     * Gets format parts
     *
     * @return format parts
     */
    public List<FormatPart> getFormatParts() {
        return formatParts;
    }

    /**
     * Sets format parts
     *
     * @param formatParts format parts
     */
    public void setFormatParts(List<FormatPart> formatParts) {
        this.formatParts = formatParts;
    }
}
