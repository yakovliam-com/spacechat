package dev.spaceseries.spacechat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class FormatPart {

    /**
     * The part's text
     */
    @Getter
    @Setter
    private String text;

    /**
     * The part's line
     * Can be null, usually not used unless the format
     * is ONLY one line
     */
    @Getter
    @Setter
    private String line;

    /**
     * The extra for the format part
     */
    @Getter
    @Setter
    private Extra extra;
}
