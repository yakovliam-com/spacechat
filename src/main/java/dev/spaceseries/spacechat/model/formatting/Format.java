package dev.spaceseries.spacechat.model.formatting;

import java.util.List;

public class Format {

    /**
     * The list of format parts
     */
    private List<FormatPart> formatParts;

    private boolean conditional;

    /**
     * Construct format
     *
     * @param formatParts format parts
     */
    public Format(List<FormatPart> formatParts) {
        this.formatParts = formatParts;
        this.conditional = formatParts.stream().anyMatch(part -> part.getLineProtocol() > 0);
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
        this.conditional = formatParts.stream().anyMatch(part -> part.getLineProtocol() > 0);
    }

    public boolean isConditional() {
        return conditional;
    }
}
