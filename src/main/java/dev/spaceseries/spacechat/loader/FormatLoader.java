package dev.spaceseries.spacechat.loader;

import dev.spaceseries.spacechat.SpaceChatPlugin;

public abstract class FormatLoader<T> implements Loader<FormatManager<T>> {

    /**
     * The format section
     */
    protected final String formatsSection;

    /**
     * Plugin
     */
    private final SpaceChatPlugin plugin;

    /**
     * Construct format loader
     *
     * @param plugin         plugin
     * @param formatsSection section
     */
    public FormatLoader(SpaceChatPlugin plugin, String formatsSection) {
        this.plugin = plugin;
        this.formatsSection = formatsSection;
    }

    /**
     * Gets plugin
     * @return plugin
     */
    protected SpaceChatPlugin getPlugin() {
        return plugin;
    }

    /**
     * Loads formats
     */
    @Override
    public abstract void load(FormatManager<T> formatManager);
}
