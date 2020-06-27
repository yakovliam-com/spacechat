package com.yakovliam.spacechat.model.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class ClickAction {

    /**
     * The type of action for the click action
     */
    @Getter
    @Setter
    private ClickActionType clickActionType;

    /**
     * The value applied to the click action type when the
     * event is fired
     */
    @Getter
    @Setter
    private String value;
}
