package dev.spaceseries.spacechat.builder.chatformat;

import dev.spaceseries.spaceapi.config.impl.Configuration;
import dev.spaceseries.spacechat.builder.Builder;
import dev.spaceseries.spacechat.builder.format.FormatBuilder;
import dev.spaceseries.spacechat.model.formatting.ChatFormat;
import dev.spaceseries.spacechat.model.formatting.Format;

public class ChatFormatBuilder implements Builder<Configuration, ChatFormat> {

    /**
     * Builds a format from a Configuration
     *
     * @param input The input
     * @return The returned format
     */
    @Override
    public ChatFormat build(Configuration input) {
        // get handle
        String handle = input.getName();

        // get priority
        Integer priority = input.getInt("priority");

        // get permission node
        String permission = input.getString("permission");

        // get parts
        Format parts = new FormatBuilder().build(input.getSection("format"));

        // return
        return new ChatFormat(handle, priority, permission, parts);
    }
}
