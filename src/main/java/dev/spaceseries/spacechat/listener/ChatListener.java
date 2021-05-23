package dev.spaceseries.spacechat.listener;

import dev.spaceseries.spacechat.SpaceChat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    /**
     * Listens for chat messages
     * At the MONITOR priority (runs near LAST) to accommodate for plugins that block chat (mutes, anti-bots, etc)
     *
     * @param event The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerAsyncChat(AsyncPlayerChatEvent event) {
        if(event.isCancelled()) return;

        // clear recipients to "cancel"
        event.getRecipients().clear();

        // TODO if player has a current channel, then use the channel manager to send instead of the chat format manager

        // get chat format manager, send chat packet (this method also sets the format in console)
        SpaceChat.getInstance().getChatFormatManager().send(event, event.getMessage());
    }
}
