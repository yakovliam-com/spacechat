package com.yakovliam.spacechat.model.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class HoverAction {

    /**
     * The list of lines in the
     * hover action
     */
    @Getter
    @Setter
    private List<String> lines;
}
