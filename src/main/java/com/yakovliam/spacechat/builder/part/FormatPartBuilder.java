package com.yakovliam.spacechat.builder.part;

import com.yakovliam.spaceapi.config.impl.Configuration;
import com.yakovliam.spacechat.builder.IBuilder;
import com.yakovliam.spacechat.builder.extra.ExtraBuilder;
import com.yakovliam.spacechat.model.FormatPart;

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

            // add format part
            formatPartList.add(formatPart);
        }

        // return
        return formatPartList;
    }
}
