package com.yakovliam.spacechat.builder.extra;

import com.yakovliam.spaceapi.config.impl.Configuration;
import com.yakovliam.spacechat.builder.IBuilder;
import com.yakovliam.spacechat.model.Extra;
import com.yakovliam.spacechat.model.action.ClickAction;
import com.yakovliam.spacechat.model.action.ClickActionType;

import java.util.ArrayList;
import java.util.List;

public class ExtraBuilder implements IBuilder<Configuration, Extra> {

    /**
     * Builds an V (output) from a K (input)
     *
     * @param input The input (the root of the format part subpart (e.g. "prefix"))
     * @return The extra
     */
    @Override
    public Extra build(Configuration input) {
        // create object
        Extra extra = new Extra();

        // get list of extras
        List<Configuration> configurationList = (List<Configuration>) input.getList("extra", new ArrayList<Configuration>());

        // loop through list

        //todo

        return null;
    }
}
