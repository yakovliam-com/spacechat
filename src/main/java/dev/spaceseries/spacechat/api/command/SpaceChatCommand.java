package dev.spaceseries.spacechat.api.command;

import co.aikar.commands.BaseCommand;
import dev.spaceseries.spacechat.SpaceChatPlugin;

public abstract class SpaceChatCommand extends BaseCommand {

    /**
     * Plugin
     */
    protected final SpaceChatPlugin plugin;

    /**
     * SpaceChat command
     *
     * @param plugin plugin
     */
    public SpaceChatCommand(SpaceChatPlugin plugin) {
        this.plugin = plugin;
    }

    protected SpaceChatPlugin getPlugin() {
        return plugin;
    }
}
