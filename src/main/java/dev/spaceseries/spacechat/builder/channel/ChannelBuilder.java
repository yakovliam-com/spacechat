package dev.spaceseries.spacechat.builder.channel;

import dev.spaceseries.spaceapi.config.impl.Configuration;
import dev.spaceseries.spacechat.builder.Builder;
import dev.spaceseries.spacechat.builder.format.FormatBuilder;
import dev.spaceseries.spacechat.model.Channel;
import dev.spaceseries.spacechat.model.formatting.Format;

public class ChannelBuilder implements Builder<Configuration, Channel>  {

    @Override
    public Channel build(Configuration input) {
        // get handle
        String handle = input.getName();

        // get permission
        String permission = input.getString("permission");

        // build format
        Format format = new FormatBuilder().build(input.getSection("format"));

        return new Channel(handle, permission, format);
    }
}
