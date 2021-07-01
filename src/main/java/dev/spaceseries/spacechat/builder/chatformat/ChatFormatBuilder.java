package dev.spaceseries.spacechat.builder.chatformat;

import dev.spaceseries.spaceapi.config.generic.adapter.ConfigurationAdapter;
import dev.spaceseries.spaceapi.util.Pair;
import dev.spaceseries.spaceapi.util.Trio;
import dev.spaceseries.spacechat.builder.Builder;
import dev.spaceseries.spacechat.builder.format.FormatBuilder;
import dev.spaceseries.spacechat.model.formatting.ChatFormat;
import dev.spaceseries.spacechat.model.formatting.Format;

public class ChatFormatBuilder implements Builder<Trio<String, String, ConfigurationAdapter>, ChatFormat> {

    /**
     * Builds a format from a Configuration
     *
     * @param input The input
     * @return The returned format
     */
    @Override
    public ChatFormat build(Trio<String, String, ConfigurationAdapter> input) {
        ConfigurationAdapter adapter = input.getRight();

        // path
        String path = input.getLeft();

        // get handle
        String handle = input.getMid();

        // get priority
        Integer priority = adapter.getInteger(path + ".priority", -1);

        // get permission node
        String permission = adapter.getString(path + ".permission", null);

        // get parts
        Format parts = new FormatBuilder().build(new Pair<>(path + ".format", adapter));

        // return
        return new ChatFormat(handle, priority, permission, parts);
    }
}
