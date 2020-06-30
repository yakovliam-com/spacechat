package com.yakovliam.spacechat.configuration;

import com.yakovliam.spacechat.SpaceChat;

public class Config extends com.yakovliam.spaceapi.config.obj.Config {

    public Config() {
        super(SpaceChat.getInstance().getPlugin(), "config.yml");
    }
}
