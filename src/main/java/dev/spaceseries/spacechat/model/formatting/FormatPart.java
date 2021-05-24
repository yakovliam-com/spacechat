package dev.spaceseries.spacechat.model.formatting;

public class FormatPart {

    /**
     * The part's text
     */
    private String text;

    /**
     * The part's line
     * Can be null, usually not used unless the format
     * is ONLY one line
     */
    private String line;

    /**
     * The extra for the format part
     */
    private Extra extra;

    /**
     * Construct format part
     *
     * @param text  text
     * @param line  line
     * @param extra extra
     */
    public FormatPart(String text, String line, Extra extra) {
        this.text = text;
        this.line = line;
        this.extra = extra;
    }

    /**
     * Construct format part
     */
    public FormatPart() {
    }

    /**
     * Returns text
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets text
     *
     * @param text text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns line
     *
     * @return line
     */
    public String getLine() {
        return line;
    }

    /**
     * Sets line
     *
     * @param line line
     */
    public void setLine(String line) {
        this.line = line;
    }

    /**
     * Returns extra
     *
     * @return extra
     */
    public Extra getExtra() {
        return extra;
    }

    /**
     * Sets extra
     *
     * @param extra extra
     */
    public void setExtra(Extra extra) {
        this.extra = extra;
    }
}
