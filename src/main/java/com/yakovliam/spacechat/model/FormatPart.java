package com.yakovliam.spacechat.model;

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
     * The extra for the format part
     */
    @Getter
    @Setter
    private Extra extra;
}
