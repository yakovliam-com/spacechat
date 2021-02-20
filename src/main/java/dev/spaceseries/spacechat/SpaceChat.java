package dev.spaceseries.spacechat;

import dev.spaceseries.spaceapi.abstraction.plugin.BukkitPlugin;
import dev.spaceseries.spacechat.command.SpaceChatCommand;
import dev.spaceseries.spacechat.configuration.Config;
import dev.spaceseries.spacechat.configuration.FormatsConfig;
import dev.spaceseries.spacechat.configuration.LangConfig;
import dev.spaceseries.spacechat.internal.dependency.DependencyLoader;
import dev.spaceseries.spacechat.listener.ChatListener;
import dev.spaceseries.spacechat.logging.LogManagerImpl;
import dev.spaceseries.spacechat.manager.ChatFormatManager;
import dev.spaceseries.spacechat.internal.space.SpacePlugin;
import dev.spaceseries.spacechat.dynamicconnection.DynamicConnectionManager;
import dev.spaceseries.spacechat.storage.StorageManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpaceChat extends JavaPlugin {

    /**
     * The instance of the main class (this class)
     */
    private static SpaceChat instance;

    /**
     * The instance of the space api
     */
    private SpacePlugin plugin;

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
     * Dynamic connection manager for redis
     */
    private DynamicConnectionManager dynamicConnectionManager;

    /**
     * Runs on load
     */
    @Override
    public void onLoad() {
        instance = this;
        // load dependencies
        new DependencyLoader().load(this);
    }

    /**
     * Runs on enable
     */
    @Override
    public void onEnable() {
        // initialize space api
        plugin = new SpacePlugin(this);

        // load configs
        loadConfigs();

        // load formats
        loadFormats();

        // load storage
        loadStorage();

        // load connection managers
        loadConnectionManagers();

        // initialize log manager
        logManagerImpl = new LogManagerImpl();

        // initialize commands
        new SpaceChatCommand();

        // register chat listener
        this.getServer().getPluginManager().registerEvents(new ChatListener(), this);

        // initialize metrics
        new MetricsHandler();
    }

    @Override
    public void onDisable() {
        // stop redis supervisor
        this.getDynamicConnectionManager().getRedisSupervisor().stop();
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
     * Load connection managers
     */
    public void loadConnectionManagers() {
        dynamicConnectionManager = new DynamicConnectionManager();
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
    public BukkitPlugin getPlugin() {
        return plugin.getPlugin();
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

    /**
     * Returns dynamic connection managers
     *
     * @return dynamic connection manager
     */
    public DynamicConnectionManager getDynamicConnectionManager() {
        return dynamicConnectionManager;
    }
}
