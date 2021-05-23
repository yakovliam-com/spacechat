package dev.spaceseries.spacechat.sync;

public abstract class ServerSyncService {


    /**
     * Service manager
     */
    private final ServerSyncServiceManager serviceManager;

    /**
     * Construct server sync service
     *
     * @param serviceManager service manager
     */
    public ServerSyncService(ServerSyncServiceManager serviceManager) {
        this.serviceManager = serviceManager;
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
