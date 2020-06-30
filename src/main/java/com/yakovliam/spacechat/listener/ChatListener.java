package com.yakovliam.spacechat.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    /**
     * Listens for chat messages
     * At the HIGHEST priority (runs LAST) to accommodate for plugins that block chat (mutes, anti-bots, etc)
     *
     * @param event The event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAsyncChat(AsyncPlayerChatEvent event) {
        // cancel chat event altogether
        event.setCancelled(true);


    }
}
