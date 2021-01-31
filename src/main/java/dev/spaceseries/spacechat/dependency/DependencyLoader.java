package dev.spaceseries.spacechat.dependency;

import me.bristermitten.pdm.PluginDependencyManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class DependencyLoader {

    /**
     * Construct dependency loader
     */
    public DependencyLoader() {
    }

    /**
     * Load dependencies
     */
    public void load(Plugin plugin) {
        plugin.getLogger().info("Loading dependencies, please wait...");
        PluginDependencyManager dependencyManager = PluginDependencyManager.of(plugin);
        dependencyManager.loadAllDependencies().join();
        plugin.getLogger().info("Dependencies successfully loaded.");
    }
}
