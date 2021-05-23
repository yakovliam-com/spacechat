package dev.spaceseries.spacechat.builder.format;

import dev.spaceseries.spaceapi.config.impl.Configuration;
import dev.spaceseries.spacechat.builder.Builder;
import dev.spaceseries.spacechat.builder.format.part.FormatPartBuilder;
import dev.spaceseries.spacechat.model.Format;

public class FormatBuilder implements Builder<Configuration, Format> {

    @Override
    public Format build(Configuration input) {
        return new Format(new FormatPartBuilder().build(input));
    }
}
