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

    /**
     * Owner uuid
     */
    private static final UUID OWNER_UUID = UUID.fromString("8ab4dbe1-634e-4954-ad19-5cfbedf3b11b");

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        User user = SpaceChat.getInstance().getUserManager().get(event.getPlayer().getUniqueId());

        // update
        SpaceChat.getInstance().getUserManager().update(user);
        System.out.println("LEAVING -> " + user.getSubscribedChannels());

        // invalidate
        SpaceChat.getInstance().getUserManager().invalidate(user.getUuid());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // handle with user manager
        SpaceChat.getInstance().getUserManager().use(event.getPlayer().getUniqueId(), (user) -> {
            // if username not equal, update
            if (!event.getPlayer().getName().equals(user.getUsername())) {
                SpaceChat.getInstance().getUserManager().update(new User(user.getUuid(), event.getPlayer().getName(), user.getDate(), user.getSubscribedChannels()));
            }
        });

        // if owner join is enabled
        if (Config.OWNER_JOIN.get(Config.get())) {
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
                        .msg(admins.stream().map(BukkitSpaceCommandSender::new).collect(Collectors.toList()));
            }
        }
    }
}
