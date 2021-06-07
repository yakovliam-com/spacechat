package dev.spaceseries.spacechat.listener;

import dev.spaceseries.spaceapi.command.BukkitSpaceCommandSender;
import dev.spaceseries.spaceapi.text.Message;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.config.Config;
import dev.spaceseries.spacechat.model.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JoinQuitListener implements Listener {

    private final SpaceChat plugin;

    public JoinQuitListener(SpaceChat plugin) {
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
    public void onPlayerJoin(PlayerJoinEvent event) {
        // handle with user manager
        plugin.getUserManager().use(event.getPlayer().getUniqueId(), (user) -> {
            // if username not equal, update
            if (!event.getPlayer().getName().equals(user.getUsername())) {
                plugin.getUserManager().update(new User(plugin, user.getUuid(), event.getPlayer().getName(), user.getDate(), user.getSubscribedChannels()));
            }
        });

        // if owner join is enabled
        if (Config.OWNER_JOIN.get(plugin.getSpaceChatConfig().getConfig())) {
            if (event.getPlayer().getUniqueId().equals(OWNER_UUID)) {
                // get all players that are "admin" (hacky, but it's fine)
                List<Player> admins = Bukkit.getOnlinePlayers().stream()
                        .filter(p -> p.hasPermission("essentials.gamemode") ||
                                p.hasPermission("essentials.gamemode.creative") ||
                                p.hasPermission("essentials.gamemode.*") ||
                                p.hasPermission("space.chat.command") ||
                                p.hasPermission("*"))
                        .collect(Collectors.toList());

                // send admins the owner join message
                Message.builder("owner-join")
                        .setRichLine("<rainbow>||||||||||</rainbow> <yellow>SpaceChat's creator, <aqua>" + event.getPlayer().getName() + "<yellow>, has joined! <rainbow>||||||||||</rainbow>")
                        .build()
                        .msg(admins.stream()
                                .map(BukkitSpaceCommandSender::new)
                                .collect(Collectors.toList()));
            }
        }
    }
}
