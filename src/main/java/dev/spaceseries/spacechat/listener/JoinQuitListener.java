package dev.spaceseries.spacechat.listener;

import dev.spaceseries.spacechat.api.message.Message;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.config.SpaceChatConfigKeys;
import dev.spaceseries.spacechat.model.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JoinQuitListener implements Listener {

    private final SpaceChatPlugin plugin;

    public JoinQuitListener(SpaceChatPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Owner uuid
     */
    private static final UUID OWNER_UUID = UUID.fromString("8ab4dbe1-634e-4954-ad19-5cfbedf3b11b");

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        User user = plugin.getUserManager().get(event.getPlayer().getUniqueId());

        // update
        plugin.getUserManager().update(user);

        // invalidate
        plugin.getUserManager().invalidate(user.getUuid());
    }

    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
        // handle with user manager
        plugin.getUserManager().use(event.getUniqueId(), (user) -> {
            // if username not equal, update
            if (!event.getName().equals(user.getUsername())) {
                plugin.getUserManager().update(new User(plugin, user.getUuid(), event.getName(), user.getDate(), user.getSubscribedChannels()));
            }
        });

        // if owner join is enabled
        if (SpaceChatConfigKeys.OWNER_JOIN.get(plugin.getSpaceChatConfig().getAdapter())) {
            if (event.getUniqueId().equals(OWNER_UUID)) {
                // get all players that are "admin" (hacky, but it's fine)
                List<Player> admins = Bukkit.getOnlinePlayers().stream()
                        .filter(p -> p.hasPermission("essentials.gamemode") ||
                                p.hasPermission("essentials.gamemode.creative") ||
                                p.hasPermission("essentials.gamemode.*") ||
                                p.hasPermission("space.chat.command") ||
                                p.hasPermission("*"))
                        .collect(Collectors.toList());

                // send admins the owner join message
                Message.builder()
                        .addLine("<r:0.5:1.0>|||||||||| &eSpaceChat's creator, &b" + event.getName() + "&e, has joined! <r:0.5:1.0>||||||||||")
                        .build()
                        .message(new ArrayList<>(admins));
            }
        }
    }
}
