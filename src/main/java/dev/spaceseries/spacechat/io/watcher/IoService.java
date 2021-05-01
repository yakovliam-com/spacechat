package dev.spaceseries.spacechat.io.watcher;

/**
 * Interface definition for services.
 */
public interface IoService {
    /**
     * Starts the service. This method blocks until the service has completely started.
     */
    void start() throws Exception;

    /**
     * Stops the service. This method blocks until the service has completely shut down.
     */
    void stop();
}