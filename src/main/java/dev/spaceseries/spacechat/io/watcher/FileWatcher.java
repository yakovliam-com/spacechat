package dev.spaceseries.spacechat.io.watcher;

import com.google.common.io.Files;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import org.bukkit.Bukkit;

import java.util.concurrent.CompletableFuture;

public class FileWatcher {

    /**
     * Construct file watcher
     */
    public FileWatcher(SpaceChatPlugin plugin) {
        DirectoryWatcher fileWatcher = new DirectoryWatcher.Builder()
                .addDirectories(plugin.getDataFolder().getPath())
                .setFilter((p) -> {
                    String ext = Files.getFileExtension(p.toFile().getName());
                    return ext.equalsIgnoreCase("yaml") || ext.equalsIgnoreCase("yml");
                })
                .setPreExistingAsCreated(true)
                .build(plugin, (event, path) -> {
                    if (event != DirectoryWatcher.Event.ENTRY_MODIFY) return;

                    // send message in console
                    plugin.getLogger().info("Detected a change in SpaceChat's configurations, so we're reloading to automatically apply the changes.");

                    // run async task
                    CompletableFuture.runAsync(() -> {
                        try {
                            // reload configurations
                            plugin.loadConfigs();

                            // reload formats
                            plugin.loadFormats();

                            // reload channels
                            plugin.loadChannels();

                            // reload storage
                            plugin.loadStorage();

                            // load messages
                            plugin.loadMessages();

                            // load dynamic connections
                            plugin.loadSyncServices();
                        } catch (Exception e) {
                            Messages.getInstance(plugin).reloadFailure.message(Bukkit.getConsoleSender());
                            e.printStackTrace();
                            return;
                        }
                        Messages.getInstance(plugin).reloadSuccess.message(Bukkit.getConsoleSender());
                    });
                });

        // do watch task
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                fileWatcher.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
