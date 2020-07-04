package dev.spaceseries.spacechat.builder.extra;

import dev.spaceseries.api.config.impl.Configuration;
import dev.spaceseries.spacechat.builder.IBuilder;
import dev.spaceseries.spacechat.model.Extra;

public class ExtraBuilder implements IBuilder<Configuration, Extra> {

    /**
     * Builds an V (output) from a K (input)
     *
     * @param input The input
     * @return The extra
     */
    @Override
    public Extra build(Configuration input) {
        // create object
        Extra extra = new Extra();

        // check if "click" exists
        if (input.contains("click")) {
            // use click builder to set extra
            extra.setClickAction(new ClickActionBuilder().build(input.getSection("click")));
        }

        // check if "hover" exists
        if (input.contains("hover")) {
            // use hover builder to set extra
            extra.setHoverAction(new HoverActionBuilder().build(input.getSection("hover")));
        }

        // return
        return extra;
    }
}
