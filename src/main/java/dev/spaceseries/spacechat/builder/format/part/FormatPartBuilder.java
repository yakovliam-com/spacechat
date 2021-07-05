package dev.spaceseries.spacechat.builder.format.part;

import dev.spaceseries.spaceapi.config.generic.adapter.ConfigurationAdapter;
import dev.spaceseries.spaceapi.util.Pair;
import dev.spaceseries.spacechat.builder.Builder;
import dev.spaceseries.spacechat.builder.extra.ExtraBuilder;
import dev.spaceseries.spacechat.model.formatting.FormatPart;

import java.util.ArrayList;
import java.util.List;

public class FormatPartBuilder implements Builder<Pair<String, ConfigurationAdapter>, List<FormatPart>> {

    /**
     * Builds an V (output) from a K (input)
     *
     * @param input The input
     */
    @Override
    public List<FormatPart> build(Pair<String, ConfigurationAdapter> input) {
        ConfigurationAdapter adapter = input.getRight();
        String path = input.getLeft();

        // create list
        List<FormatPart> formatPartList = new ArrayList<>();

        // loop through all keys in the root input
        for (String handle : adapter.getKeys(path, new ArrayList<>())) {
            // create format part
            FormatPart formatPart = new FormatPart();

            // get text
            String text = adapter.getString(path + "." + handle + ".text", null);

            // set text
            formatPart.setText(text);

            List<String> extraKeys = adapter.getKeys(path + "." + handle + ".extra", null);

            // if extra exists, parse
            if (extraKeys != null) {
                // set extra
                formatPart.setExtra(new ExtraBuilder().build(new Pair<>(path + "." + handle + ".extra", adapter)));
            }

            String line = adapter.getString(path + "." + handle + ".line", null);

            // if it contains "line" (singular minimessage compatibility)
            if (line != null) {
                // set line
                formatPart.setLine(line);
            }

            // add format part
            formatPartList.add(formatPart);
        }

        // return
        return formatPartList;
    }
}
