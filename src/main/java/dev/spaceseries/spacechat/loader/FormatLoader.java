package dev.spaceseries.spacechat.loader;

import dev.spaceseries.spaceapi.config.impl.Configuration;

public abstract class FormatLoader<T> implements Loader<FormatManager<T>> {

    /**
     * The format section
     */
    protected final Configuration formatSection;

    /**
     * Construct format loader
     *
     * @param formatSection section
     */
    public FormatLoader(Configuration formatSection) {
        this.formatSection = formatSection;
    }

    /**
     * Loads formats
     */
    @Override
    public abstract void load(FormatManager<T> formatManager);
}
