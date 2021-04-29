package dev.spaceseries.spacechat;

import dev.spaceseries.spaceapi.abstraction.plugin.BukkitPlugin;
import dev.spaceseries.spacechat.command.SpaceChatCommand;
import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.config.FormatsConfig;
import dev.spaceseries.spacechat.config.LangConfig;
import dev.spaceseries.spacechat.external.papi.SpaceChatExpansion;
import dev.spaceseries.spacechat.internal.dependency.DependencyInstantiation;
import dev.spaceseries.spacechat.listener.ChatListener;
import dev.spaceseries.spacechat.listener.JoinQuitListener;
import dev.spaceseries.spacechat.logging.LogManagerImpl;
import dev.spaceseries.spacechat.manager.ChatFormatManager;
import dev.spaceseries.spacechat.internal.space.SpacePlugin;
import dev.spaceseries.spacechat.messaging.MessagingService;
import dev.spaceseries.spacechat.storage.StorageManager;
import dev.spaceseries.spacechat.util.metrics.Metrics;
import dev.spaceseries.spacechat.util.version.VersionUtil;
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
     * Messaging service
     */
    private MessagingService messagingService;

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
        this.getLogger().info(
                "Starting Dependency Tasks... This may take a while depending on your environment!");
        new DependencyInstantiation().startTasks();

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
        // register join listener
        this.getServer().getPluginManager().registerEvents(new JoinQuitListener(), this);

        // register placeholder expansion
        new SpaceChatExpansion().register();

        // initialize metrics
        new Metrics(this, 7508);

        // log initialization method
        this.getLogger().info("Detected that SpaceChat is running under " + VersionUtil.getServerBukkitVersion().toString());
    }

    @Override
    public void onDisable() {
        // stop redis supervisor
        this.messagingService.getSupervisor().stop();
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
        // if currently exists, stop first
        if (messagingService != null)
            messagingService.getSupervisor().stop();

        messagingService = new MessagingService();
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
     * Returns messaging service
     *
     * @return messaging service
     */
    public MessagingService getMessagingService() {
        return messagingService;
    }
}
