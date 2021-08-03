package dev.spaceseries.spacechat.sync;

import dev.spaceseries.spacechat.SpaceChatPlugin;

public abstract class ServerSyncService {

    /**
     * SpaceChat plugin
     */
    protected final SpaceChatPlugin plugin;

    /**
     * Service manager
     */
    private final ServerSyncServiceManager serviceManager;

    /**
     * Construct server sync service
     *
     * @param plugin         plugin
     * @param serviceManager service manager
     */
    public ServerSyncService(SpaceChatPlugin plugin, ServerSyncServiceManager serviceManager) {
        this.serviceManager = serviceManager;
        this.plugin = plugin;
    }

    /**
     * Returns service manager
     *
     * @return manager
     */
    public ServerSyncServiceManager getServiceManager() {
        return serviceManager;
    }
}
