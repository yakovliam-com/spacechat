package dev.spaceseries.spacechat.builder.channel;

import dev.spaceseries.spaceapi.config.generic.adapter.ConfigurationAdapter;
import dev.spaceseries.spaceapi.util.Pair;
import dev.spaceseries.spaceapi.util.Trio;
import dev.spaceseries.spacechat.builder.Builder;
import dev.spaceseries.spacechat.builder.format.FormatBuilder;
import dev.spaceseries.spacechat.model.Channel;
import dev.spaceseries.spacechat.model.formatting.Format;

public class ChannelBuilder implements Builder<Trio<String, String, ConfigurationAdapter>, Channel> {

    @Override
    public Channel build(Trio<String, String, ConfigurationAdapter> input) {
        ConfigurationAdapter adapter = input.getRight();
        String handle = input.getMid();
        String path = input.getLeft();

        // get permission
        String permission = adapter.getString(path + ".permission", null);

        // build format
        Format format = new FormatBuilder().build(new Pair<>(path + ".format", adapter));

        return new Channel(handle, permission, format);
    }
}
