package com.yakovliam.spacechat.configuration;

import com.yakovliam.spaceapi.config.obj.Config;
import com.yakovliam.spacechat.SpaceChat;

public class FormatsConfig extends Config {

    public FormatsConfig() {
        super(SpaceChat.getInstance().getPlugin(), "formats.yml");
    }
}
