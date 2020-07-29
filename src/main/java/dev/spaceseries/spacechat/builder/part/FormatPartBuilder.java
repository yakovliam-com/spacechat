package dev.spaceseries.spacechat.builder.part;

import dev.spaceseries.api.config.impl.Configuration;
import dev.spaceseries.spacechat.builder.IBuilder;
import dev.spaceseries.spacechat.builder.extra.ExtraBuilder;
import dev.spaceseries.spacechat.model.FormatPart;

import java.util.ArrayList;
import java.util.List;

public class FormatPartBuilder implements IBuilder<Configuration, List<FormatPart>> {

    /**
     * Builds an V (output) from a K (input)
     *
     * @param input The input
     */
    @Override
    public List<FormatPart> build(Configuration input) {
        // create list
        List<FormatPart> formatPartList = new ArrayList<>();

        // loop through all keys in the root input
        for (String handle : input.getKeys()) {
            // create format part
            FormatPart formatPart = new FormatPart();

            // get section from handle (key)
            Configuration section = input.getSection(handle);


            // get text
            String text = section.getString("text");

            // set text
            formatPart.setText(text);

            // if extra exists, parse
            if (section.contains("extra")) {
                // set extra
                formatPart.setExtra(new ExtraBuilder().build(section.getSection("extra")));
            }

            // if it contains "line" (singular minimessage compatibility)
            if (section.contains("line")) {
                // set line
                formatPart.setLine(section.getString("line"));
            }

            // add format part
            formatPartList.add(formatPart);
        }

        // return
        return formatPartList;
    }
}