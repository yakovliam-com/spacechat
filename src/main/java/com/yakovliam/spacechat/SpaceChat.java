package com.yakovliam.spacechat;

import com.yakovliam.spaceapi.abstraction.plugin.BukkitPlugin;
import com.yakovliam.spaceapi.abstraction.plugin.Plugin;
import com.yakovliam.spacechat.configuration.FormatsConfig;
import com.yakovliam.spacechat.loader.FormatLoader;
import com.yakovliam.spacechat.manager.FormatManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpaceChat extends JavaPlugin {

    /**
     * The instance of the main class (this class)
     */
    @Getter
    private static SpaceChat instance;

    /**
     * The instance of the space api
     */
    @Getter
    private Plugin plugin;

    /**
     * The formats config
     */
    @Getter
    private FormatsConfig formatsConfig;

    /**
     * The chat manager
     */
    @Getter
    private FormatManager chatFormatManager;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // initialize space api
        plugin = new BukkitPlugin(this);

        // initialize configs
        formatsConfig = new FormatsConfig();

        // initialize chat format manager
        chatFormatManager = new FormatManager();

        // initialize format loader & load all
        new FormatLoader().loadAllFormats();
    }

    @Override
    public void onDisable() {
    }
}
