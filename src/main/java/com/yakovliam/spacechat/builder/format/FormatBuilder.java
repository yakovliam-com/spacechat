package com.yakovliam.spacechat.builder.format;

import com.yakovliam.spaceapi.config.impl.Configuration;
import com.yakovliam.spacechat.builder.IBuilder;
import com.yakovliam.spacechat.model.Format;

public class FormatBuilder implements IBuilder<Configuration, Format> {

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

        // set permissoni
        format.setPermission(input.getString("permission"));

        return null;
    }
}
