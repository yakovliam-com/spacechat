package dev.spaceseries.spacechat;

import dev.spaceseries.api.abstraction.plugin.BukkitPlugin;
import dev.spaceseries.api.abstraction.plugin.Plugin;
import dev.spaceseries.spacechat.command.SpaceChatCommand;
import dev.spaceseries.spacechat.configuration.Config;
import dev.spaceseries.spacechat.configuration.FormatsConfig;
import dev.spaceseries.spacechat.configuration.LangConfig;
import dev.spaceseries.spacechat.listener.ChatListener;
import dev.spaceseries.spacechat.logging.LogManager;
import dev.spaceseries.spacechat.manager.ChatFormatManager;
import dev.spaceseries.spacechat.storage.StorageManager;
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
     * The main plugin config
     */
    @Getter
    private Config spaceChatConfig;

    /**
     * The plugin's language configuration
     */
    @Getter
    private LangConfig langConfig;

    /**
     * The chat format manager
     */
    @Getter
    private ChatFormatManager chatFormatManager;

    /**
     * The log manager
     */
    @Getter
    private LogManager logManager;

    /**
     * The storage manager
     */
    @Getter
    private StorageManager storageManager;

    /**
     * Runs on load
     */
    @Override
    public void onLoad() {
        instance = this;
    }

    /**
     * Runs on enable
     */
    @Override
    public void onEnable() {
        // initialize space api
        plugin = new BukkitPlugin(this);

        // load configs
        loadConfigs();

        // load formats
        loadFormats();

        // load storage
        loadStorage();

        // initialize log manager
        logManager = new LogManager();

        // initialize commands
        new SpaceChatCommand();

        // register chat listener
        this.getServer().getPluginManager().registerEvents(new ChatListener(), this);

        // initialize metrics
        new MetricsHandler();
    }

    /**
     * Reloads configurations
     */
    public void loadConfigs() {
        // initialize configs
        spaceChatConfig = new Config();
        formatsConfig = new FormatsConfig();
        langConfig = new LangConfig();
    }

    /**
     * Loads formats
     */
    public void loadFormats() {
        // initialize chat format manager (also loads all formats!)
        chatFormatManager = new ChatFormatManager();
    }

    /**
     * Loads storage manager
     */
    public void loadStorage() {
        // initialize storage
        storageManager = new StorageManager();
    }

}
