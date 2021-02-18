package dev.spaceseries.spacechat.builder.format;

import dev.spaceseries.spaceapi.config.impl.Configuration;
import dev.spaceseries.spacechat.builder.Builder;
import dev.spaceseries.spacechat.builder.part.FormatPartBuilder;
import dev.spaceseries.spacechat.model.Format;

public class FormatBuilder implements Builder<Configuration, Format> {

    /**
     * Builds a format from a Configuration
     *
     * @param input The input
     * @return The returned format
     */
    @Override
    public Format build(Configuration input) {
        // create object
        Format format = new Format();

        // set handle
        format.setHandle(input.getName());

        // set priority
        format.setPriority(input.getInt("priority"));

        // set permission node
        format.setPermission(input.getString("permission"));

        // set format parts
        format.setFormatParts(new FormatPartBuilder().build(input.getSection("format")));

        // return
        return format;
    }
}
