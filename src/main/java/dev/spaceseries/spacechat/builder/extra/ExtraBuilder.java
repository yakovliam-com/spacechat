package dev.spaceseries.spacechat.builder.extra;

import dev.spaceseries.spaceapi.config.generic.adapter.ConfigurationAdapter;
import dev.spaceseries.spaceapi.util.Pair;
import dev.spaceseries.spacechat.builder.Builder;
import dev.spaceseries.spacechat.model.formatting.Extra;

import java.util.List;

public class ExtraBuilder implements Builder<Pair<String, ConfigurationAdapter>, Extra> {

    /**
     * Builds an V (output) from a K (input)
     *
     * @param input The input
     * @return The extra
     */
    @Override
    public Extra build(Pair<String, ConfigurationAdapter> input) {
        ConfigurationAdapter adapter = input.getRight();
        String path = input.getLeft();

        // create object
        Extra extra = new Extra();

        List<String> clickKeys = adapter.getKeys(path + ".click", null);

        // check if "click" exists
        if (clickKeys != null) {
            // use click builder to set extra
            extra.setClickAction(new ClickActionBuilder().build(new Pair<>(path + ".click", adapter)));
        }

        List<String> hoverKeys = adapter.getKeys(path + ".hover", null);

        // check if "hover" exists
        if (hoverKeys != null) {
            // use hover builder to set extra
            extra.setHoverAction(new HoverActionBuilder().build(new Pair<>(path + ".hover", adapter)));
        }

        // return
        return extra;
    }
}
