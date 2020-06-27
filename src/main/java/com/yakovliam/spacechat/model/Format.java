package com.yakovliam.spacechat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class Format {

    /**
     * The handle of the format
     * Same as "identifier" meaning
     */
    @Getter
    @Setter
    private String handle;

    /**
     * The priority of the format
     */
    @Getter
    @Setter
    private Integer priority;

    /**
     * The applicable permission node for the format
     */
    @Getter
    @Setter
    private String permission;

    /**
     * The list of format parts
     */
    @Getter
    @Setter
    private List<FormatPart> formatParts;
}
