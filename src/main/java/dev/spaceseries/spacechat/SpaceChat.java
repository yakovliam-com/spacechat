package dev.spaceseries.spacechat;

import dev.spaceseries.api.abstraction.plugin.BukkitPlugin;
import dev.spaceseries.api.abstraction.plugin.Plugin;
import dev.spaceseries.spacechat.command.SpaceChatCommand;
import dev.spaceseries.spacechat.configuration.Config;
import dev.spaceseries.spacechat.configuration.FormatsConfig;
import dev.spaceseries.spacechat.configuration.LangConfig;
import dev.spaceseries.spacechat.dependency.DependencyLoader;
import dev.spaceseries.spacechat.listener.ChatListener;
import dev.spaceseries.spacechat.logging.LogManagerImpl;
import dev.spaceseries.spacechat.manager.ChatFormatManager;
import dev.spaceseries.spacechat.storage.StorageManager;
import me.bristermitten.pdm.PluginDependencyManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpaceChat extends JavaPlugin {

    /**
     * The instance of the main class (this class)
     */
    private static SpaceChat instance;

    /**
     * The instance of the space api
     */
    private Plugin plugin;

    /**
     * The formats config
     */
    private FormatsConfig formatsConfig;

    /**
     * The main plugin config
     */
    private Config spaceChatConfig;

    /**
     * The plugin's language configuration
     */
    private LangConfig langConfig;

    /**
     * The chat format manager
     */
    private ChatFormatManager chatFormatManager;

    /**
     * The log manager
     */
    private LogManagerImpl logManagerImpl;

    /**
     * The storage manager
     */
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
        // load dependencies
        new DependencyLoader().load(this);

        // initialize space api
        plugin = new BukkitPlugin(this);

        // load configs
        loadConfigs();

        // load formats
        loadFormats();

        // load storage
        loadStorage();

        // initialize log manager
        logManagerImpl = new LogManagerImpl();

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

    /**
     * Loads messages
     */
    public void loadMessages() {
        Messages.renew();
    }

    public static SpaceChat getInstance() {
        return instance;
    }

    /**
     * Returns space plugin
     *
     * @return plugin
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Returns formats config
     *
     * @return formats config
     */
    public FormatsConfig getFormatsConfig() {
        return formatsConfig;
    }

    /**
     * Returns main configuration
     *
     * @return config
     */
    public Config getSpaceChatConfig() {
        return spaceChatConfig;
    }

    /**
     * Get lang configuration
     *
     * @return lang config
     */
    public LangConfig getLangConfig() {
        return langConfig;
    }

    /**
     * Returns chat format manager
     *
     * @return chat format manager
     */
    public ChatFormatManager getChatFormatManager() {
        return chatFormatManager;
    }

    /**
     * Returns log manager implementation
     *
     * @return log manager
     */
    public LogManagerImpl getLogManagerImpl() {
        return logManagerImpl;
    }

    /**
     * Returns storage manager
     *
     * @return storage manager
     */
    public StorageManager getStorageManager() {
        return storageManager;
    }
}
