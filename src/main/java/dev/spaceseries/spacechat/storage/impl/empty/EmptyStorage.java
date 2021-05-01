package dev.spaceseries.spacechat.storage.impl.empty;

import dev.spaceseries.spacechat.logging.wrap.LogWrapper;
import dev.spaceseries.spacechat.storage.Storage;

public class EmptyStorage implements Storage {

    @Override
    public void log(LogWrapper data) {
    }

    @Override
    public void close() {

    }
}
