package dev.spaceseries.spacechat.listener;

import dev.spaceseries.spacechat.SpaceChat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    /**
     * Listens for chat messages
     * At the MONITOR priority (runs LAST) to accommodate for plugins that block chat (mutes, anti-bots, etc)
     *
     * @param event The event
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerAsyncChat(AsyncPlayerChatEvent event) {
        // cancel chat event altogether
        event.setCancelled(true);

        // get chat format manager, send chat packet
        SpaceChat.getInstance().getChatFormatManager().send(event.getPlayer(), event.getMessage());
    }
}
