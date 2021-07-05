package dev.spaceseries.spacechat.storage;

public class StorageInitializationException extends Exception {

    private static final String DEFAULT_MESSAGE = "We failed to initialize your storage medium. Perhaps you forgot to fill out important details in the configuration?";

    public StorageInitializationException(String message) {
        super(message);
    }

    public StorageInitializationException() {
        super(DEFAULT_MESSAGE);
    }
}
