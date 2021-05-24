package dev.spaceseries.spacechat;

import dev.spaceseries.spaceapi.abstraction.plugin.BukkitPlugin;
import dev.spaceseries.spacechat.command.*;
import dev.spaceseries.spacechat.config.ChannelsConfig;
import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.config.FormatsConfig;
import dev.spaceseries.spacechat.config.LangConfig;
import dev.spaceseries.spacechat.external.papi.SpaceChatExpansion;
import dev.spaceseries.spacechat.internal.dependency.DependencyInstantiation;
import dev.spaceseries.spacechat.io.watcher.FileWatcher;
import dev.spaceseries.spacechat.listener.ChatListener;
import dev.spaceseries.spacechat.listener.JoinQuitListener;
import dev.spaceseries.spacechat.logging.LogManagerImpl;
import dev.spaceseries.spacechat.manager.ChannelManager;
import dev.spaceseries.spacechat.manager.ChatFormatManager;
import dev.spaceseries.spacechat.internal.space.SpacePlugin;
import dev.spaceseries.spacechat.storage.StorageManager;
import dev.spaceseries.spacechat.sync.ServerSyncServiceManager;
import dev.spaceseries.spacechat.util.version.VersionUtil;
import org.bstats.bukkit.Metrics;
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
     * Server sync service manager
     */
    private ServerSyncServiceManager serverSyncServiceManager;

    /**
     * Channel manager
     */
    private ChannelManager channelManager;

    /**
     * Channels config
     */
    private ChannelsConfig channelsConfig;

    /**
     * Dependency instantiation
     */
    private DependencyInstantiation dependencyInstantiation;

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
        this.dependencyInstantiation = new DependencyInstantiation();
        dependencyInstantiation.startTasks();

        // initialize space api
        plugin = new SpacePlugin(this);

        // load configs
        loadConfigs();

        // load formats
        loadFormats();

        // load channels
        loadChannels();

        // load storage
        loadStorage();

        // load connection managers
        loadSyncServices();

        // initialize log manager
        logManagerImpl = new LogManagerImpl();

        // initialize commands
        new SpaceChatCommand();
        new ChannelCommand();
        new IgnoreCommand();
        new BroadcastCommand();
        new BroadcastMinimessageCommand();

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

        // initialize file watcher
        new FileWatcher();
    }

    @Override
    public void onDisable() {
        // stop redis supervisor
        if (serverSyncServiceManager != null)
            serverSyncServiceManager.end();

        // close the storage connection pool (if applicable)
        if (storageManager != null)
            storageManager.getCurrent().close();
    }

    /**
     * Reloads configurations
     */
    public void loadConfigs() {
        // initialize configs
        spaceChatConfig = new Config();
        formatsConfig = new FormatsConfig();
        channelsConfig = new ChannelsConfig();
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
     * Loads channels
     */
    public void loadChannels() {
        this.channelManager = new ChannelManager();
    }

    /**
     * Loads storage manager
     */
    public void loadStorage() {
        // initialize storage
        storageManager = new StorageManager();
    }

    /**
     * Loads connection managers
     */
    public void loadSyncServices() {
        // stop sync service supervisor
        if (serverSyncServiceManager != null)
            serverSyncServiceManager.end();

        // initialize
        this.serverSyncServiceManager = new ServerSyncServiceManager();
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
     * Returns channels config
     *
     * @return channels config
     */
    public ChannelsConfig getChannelsConfig() {
        return channelsConfig;
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
     * Returns channel manager
     *
     * @return channel manager
     */
    public ChannelManager getChannelManager() {
        return channelManager;
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
     * Returns server sync service manager
     *
     * @return server sync service manager
     */
    public ServerSyncServiceManager getServerSyncServiceManager() {
        return serverSyncServiceManager;
    }

    /**
     * Returns dependency instantiation
     *
     * @return dependency instantiation
     */
    public DependencyInstantiation getDependencyInstantiation() {
        return dependencyInstantiation;
    }
}
