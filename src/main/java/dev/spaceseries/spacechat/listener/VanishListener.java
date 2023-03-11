package dev.spaceseries.spacechat.listener;

import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.config.SpaceChatConfigKeys;
import net.ess3.api.events.VanishStatusChangeEvent;
import net.essentialsx.api.v2.events.AsyncUserDataLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.logging.Level;

public class VanishListener {

    private final SpaceChatPlugin plugin;

    public VanishListener(SpaceChatPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerEvents() {
        if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            plugin.getLogger().log(Level.INFO, "Using Essentials vanish system");
            plugin.getServer().getPluginManager().registerEvents(new EssentialsListener(), plugin);
        } else {
            plugin.getServer().getPluginManager().registerEvents(new CommandListener(), plugin);
        }
    }

    protected class EssentialsListener implements Listener {

        @EventHandler(ignoreCancelled = true)
        public void onVanishStatus(VanishStatusChangeEvent event) {
            plugin.getUserManager().vanish(event.getController().getName(), event.getValue());
        }

        @EventHandler(ignoreCancelled = true)
        public void onUserLoad(AsyncUserDataLoadEvent event) {
            plugin.getUserManager().vanish(event.getUser().getName(), event.getUser().isVanished());
        }
    }

    protected class CommandListener implements Listener {

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        public void onPreCommand(PlayerCommandPreprocessEvent event) {
            if (event.getPlayer().hasPermission(SpaceChatConfigKeys.PERMISSIONS_VANISH_COMMAND.get(plugin.getSpaceChatConfig().getAdapter()))) {
                plugin.getUserManager().vanish(event.getPlayer().getName());
            }
        }
    }
}
