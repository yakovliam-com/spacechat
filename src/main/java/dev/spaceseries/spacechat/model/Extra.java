package dev.spaceseries.spacechat.model;

import dev.spaceseries.spacechat.model.action.ClickAction;
import dev.spaceseries.spacechat.model.action.HoverAction;

public class Extra {

    /**
     * The click action
     */
    private ClickAction clickAction;

    /**
     * The hover action
     */
    private HoverAction hoverAction;

    /**
     * Construct extra
     *
     * @param clickAction click action
     * @param hoverAction hover action
     */
    public Extra(ClickAction clickAction, HoverAction hoverAction) {
        this.clickAction = clickAction;
        this.hoverAction = hoverAction;
    }

    /**
     * Construct extra
     */
    public Extra() {
    }

    /**
     * Returns click action
     *
     * @return click action
     */
    public ClickAction getClickAction() {
        return clickAction;
    }

    /**
     * Sets click action
     *
     * @param clickAction click action
     */
    public void setClickAction(ClickAction clickAction) {
        this.clickAction = clickAction;
    }

    /**
     * Returns hover action
     *
     * @return hover action
     */
    public HoverAction getHoverAction() {
        return hoverAction;
    }

    /**
     * Sets hover action
     *
     * @param hoverAction hover action
     */
    public void setHoverAction(HoverAction hoverAction) {
        this.hoverAction = hoverAction;
    }
}
