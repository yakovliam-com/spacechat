package dev.spaceseries.spacechat.builder.format;

import dev.spaceseries.spaceapi.config.generic.adapter.ConfigurationAdapter;
import dev.spaceseries.spaceapi.util.Pair;
import dev.spaceseries.spacechat.builder.Builder;
import dev.spaceseries.spacechat.builder.format.part.FormatPartBuilder;
import dev.spaceseries.spacechat.model.formatting.Format;

public class FormatBuilder implements Builder<Pair<String, ConfigurationAdapter>, Format> {

    @Override
    public Format build(Pair<String,ConfigurationAdapter> input) {
        return new Format(new FormatPartBuilder().build(input));
    }
}
