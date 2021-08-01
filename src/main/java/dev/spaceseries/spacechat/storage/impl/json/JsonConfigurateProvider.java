package dev.spaceseries.spacechat.storage.impl.json;

import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.storage.StorageInitializationException;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonConfigurateProvider {

    private static final String FILE_NAME = "storage.json";

    /**
     * Root node
     */
    private BasicConfigurationNode root;

    /**
     * Loader
     */
    private final GsonConfigurationLoader loader;

    /**
     * SpaceChat plugin
     */
    private final SpaceChatPlugin plugin;

    /**
     * Json configurate provider
     */
    public JsonConfigurateProvider(SpaceChatPlugin plugin) {
        this.plugin = plugin;
        this.loader = GsonConfigurationLoader.builder()
                .indent(2)
                .path(resolveJsonFile(FILE_NAME))
                .build();
    }

    /**
     * Initializes the provider, and therefore the node
     */
    public void init() throws StorageInitializationException {
        try {
            this.root = this.loader.load();
        } catch (ConfigurateException e) {
            e.printStackTrace();
            throw new StorageInitializationException();
        }
    }

    /**
     * Returns root node
     *
     * @return root node
     */
    public BasicConfigurationNode provideRoot() {
        return this.root;
    }

    /**
     * Saves a node
     *
     * @param node node
     */
    public void save(BasicConfigurationNode node) {
        try {
            this.loader.save(node);
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resolves a configuration
     *
     * @param fileName file name
     * @return configuration path
     */
    private Path resolveJsonFile(String fileName) {
        Path file = this.plugin.getDataFolder().toPath().resolve(fileName);

        // if the file doesn't exist, create it based on the template in the resources dir
        if (!Files.exists(file)) {
            try {
                Files.createDirectories(file.getParent());
            } catch (IOException ignored) {
                // ignore
            }

            try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
                Files.copy(is, file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return file;
    }
}
