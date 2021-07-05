package dev.spaceseries.spacechat.builder.extra;

import dev.spaceseries.spaceapi.config.generic.adapter.ConfigurationAdapter;
import dev.spaceseries.spaceapi.util.Pair;
import dev.spaceseries.spacechat.builder.Builder;
import dev.spaceseries.spacechat.model.formatting.action.HoverAction;

import java.util.Collections;
import java.util.List;

public class HoverActionBuilder implements Builder<Pair<String, ConfigurationAdapter>, HoverAction> {

    /**
     * Builds an V (output) from a K (input)
     *
     * @param input The input
     */
    @Override
    public HoverAction build(Pair<String, ConfigurationAdapter> input) {
        ConfigurationAdapter adapter = input.getRight();
        String path = input.getLeft();

        // create object
        HoverAction hoverAction = new HoverAction();

        // get lines
        List<String> lines = adapter.getStringList(path + ".lines", Collections.emptyList());

        // set lines
        hoverAction.setLines(lines);

        // return
        return hoverAction;
    }
}
