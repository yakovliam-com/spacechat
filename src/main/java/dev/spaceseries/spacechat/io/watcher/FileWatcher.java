package dev.spaceseries.spacechat.io.watcher;

import com.google.common.io.Files;
import dev.spaceseries.spaceapi.command.BukkitSpaceCommandSender;
import dev.spaceseries.spacechat.Messages;
import dev.spaceseries.spacechat.SpaceChat;
import org.bukkit.Bukkit;

import java.util.concurrent.CompletableFuture;

public class FileWatcher {

    /**
     * Construct file watcher
     */
    public FileWatcher() {
        DirectoryWatcher fileWatcher = new DirectoryWatcher.Builder()
                .addDirectories(SpaceChat.getInstance().getDataFolder().getPath())
                .setFilter((p) -> {
                    String ext = Files.getFileExtension(p.toFile().getName());
                    return ext.equalsIgnoreCase("yaml") || ext.equalsIgnoreCase("yml");
                })
                .setPreExistingAsCreated(true)
                .build((event, path) -> {
                    if (event != DirectoryWatcher.Event.ENTRY_MODIFY) return;

                    // send message in console
                    SpaceChat.getInstance().getLogger().info("Detected a change in SpaceChat's configurations, so we're reloading to automatically apply the changes.");

                    // run async task
                    CompletableFuture.runAsync(() -> {
                        try {
                            // reload configurations
                            SpaceChat.getInstance().loadConfigs();

                            // reload formats
                            SpaceChat.getInstance().loadFormats();

                            // reload storage
                            SpaceChat.getInstance().loadStorage();

                            // load messages
                            SpaceChat.getInstance().loadMessages();

                            // load dynamic connections
                            SpaceChat.getInstance().loadConnectionManagers();
                        } catch (Exception e) {
                            Messages.getInstance().reloadFailure.msg(new BukkitSpaceCommandSender(Bukkit.getConsoleSender()));
                            e.printStackTrace();
                            return;
                        }
                        Messages.getInstance().reloadSuccess.msg(new BukkitSpaceCommandSender(Bukkit.getConsoleSender()));
                    });
                });

        // do watch task
        Bukkit.getScheduler().runTaskAsynchronously(SpaceChat.getInstance(), () -> {
            try {
                fileWatcher.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
