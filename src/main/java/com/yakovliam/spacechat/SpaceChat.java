package com.yakovliam.spacechat;

import com.yakovliam.spaceapi.abstraction.plugin.BukkitPlugin;
import com.yakovliam.spaceapi.abstraction.plugin.Plugin;
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

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // initialize space api
        plugin = new BukkitPlugin(this);
    }

    @Override
    public void onDisable() {
    }
}
