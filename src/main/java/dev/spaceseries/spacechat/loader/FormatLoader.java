package dev.spaceseries.spacechat.loader;

import dev.spaceseries.spaceapi.config.impl.Configuration;

public abstract class FormatLoader<T> implements Loader<FormatManager<T>> {

    /**
     * The format section
     */
    protected final Configuration formatsSection;

    /**
     * Construct format loader
     *
     * @param formatsSection section
     */
    public FormatLoader(Configuration formatsSection) {
        this.formatsSection = formatsSection;
    }

    /**
     * Loads formats
     */
    @Override
    public abstract void load(FormatManager<T> formatManager);
}
