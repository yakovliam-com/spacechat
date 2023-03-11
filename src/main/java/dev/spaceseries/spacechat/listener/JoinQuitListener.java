package dev.spaceseries.spacechat.listener;

import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.model.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class JoinQuitListener implements Listener {

    private final SpaceChatPlugin plugin;

    public JoinQuitListener(SpaceChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        User user = plugin.getUserManager().get(event.getPlayer().getUniqueId());

        // update
        plugin.getUserManager().update(user);

        // remove replier
        plugin.getUserManager().getReplyTargetMap().remove(event.getPlayer().getName());

        // invalidate
        plugin.getUserManager().invalidate(user.getUuid(), user.getUsername());
    }

    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {

        List<String> ignoredList = plugin.getStorageManager().getCurrent().getIgnoreList(event.getName());

        plugin.getUserManager().getIgnoredList().put(event.getName(), ignoredList);

        // handle with user manager
        plugin.getUserManager().use(event.getUniqueId(), (user) -> {
            // if username not equal, update
            if (!event.getName().equals(user.getUsername())) {
                plugin.getUserManager().update(new User(plugin, user.getUuid(), event.getName(), user.getDate(), user.getSubscribedChannels()));
            }
        });
    }
}
