package dev.spaceseries.spacechat.listener;

import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.model.Channel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final SpaceChatPlugin plugin;

    public ChatListener(SpaceChatPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for chat messages
     * At the MONITOR priority (runs near LAST) to accommodate for plugins that block chat (mutes, anti-bots, etc)
     *
     * @param event The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerAsyncChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;

        // clear recipients to "cancel"
        event.getRecipients().clear();

        // get player's current channel
        Channel current = plugin.getServerSyncServiceManager().getDataService().getCurrentChannel(event.getPlayer().getUniqueId());

        // if not null, send through channel manager
        if (current != null && event.getPlayer().hasPermission(current.getPermission())) {
            plugin.getChannelManager().send(event, event.getMessage(), current);
            return;
        }

        // get chat format manager, send chat packet (this method also sets the format in console)
        plugin.getChatFormatManager().send(event, event.getMessage());
    }
}
