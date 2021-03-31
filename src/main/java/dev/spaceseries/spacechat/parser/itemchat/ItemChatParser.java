package dev.spaceseries.spacechat.parser.itemchat;

import dev.spaceseries.spaceapi.lib.adventure.adventure.text.Component;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.TextComponent;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.TextReplacementConfig;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.event.HoverEvent;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.format.NamedTextColor;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.format.TextDecoration;
import dev.spaceseries.spaceapi.lib.adventure.adventure.text.serializer.legacy.LegacyComponentSerializer;
import dev.spaceseries.spaceapi.util.Pair;
import dev.spaceseries.spacechat.configuration.Config;
import dev.spaceseries.spacechat.parser.Parser;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.List;

import static dev.spaceseries.spacechat.configuration.Config.*;

public class ItemChatParser implements Parser<Pair<Player, Component>, Component> {

    /**
     * Parse message to component
     *
     * @param playerStringPair pair
     * @return component
     */
    @Override
    public Component parse(Pair<Player, Component> playerStringPair) {
        Player player = playerStringPair.getLeft();
        Component message = playerStringPair.getRight();

        // if not enabled, return
        if (!ITEM_CHAT_ENABLED.get(Config.get()) || !player.hasPermission(PERMISSIONS_USE_ITEM_CHAT.get(Config.get())))
            return message;

        // get item in hand
        ItemStack itemStack = player.getItemInHand();

        // if null or air, return
        if (itemStack.getType().equals(Material.AIR) || itemStack.getAmount() <= 0) return message;

        // get display name
        String name = itemStack.hasItemMeta() ?
                itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : WordUtils.capitalize(itemStack.getType().name().replace("_", " ").toLowerCase()) :
                WordUtils.capitalize(itemStack.getType().name().replace("_", " ").toLowerCase());

        // replacement config for %item% and %amount%
        TextReplacementConfig nameReplacementConfig = TextReplacementConfig.builder()
                .matchLiteral("%name%").replacement(TextComponent.ofChildren(LegacyComponentSerializer.legacySection().deserialize(name))
                        .colorIfAbsent(NamedTextColor.WHITE))
                .build();

        TextReplacementConfig amountReplacementConfig = TextReplacementConfig.builder()
                .matchLiteral("%amount%")
                .replacement(Integer.toString(itemStack.getAmount()))
                .build();

        // convert lore (if exists)
        TextComponent.Builder loreBuilder = Component.text();

        // if using custom lore, use that instead
        if (ITEM_CHAT_WITH_LORE_USE_CUSTOM.get(Config.get())) {
            List<String> lore = ITEM_CHAT_WITH_LORE_CUSTOM.get(Config.get());

            for (Iterator<String> it = lore.iterator(); it.hasNext(); ) {
                loreBuilder.append(LegacyComponentSerializer.legacyAmpersand().deserialize(it.next())
                        .replaceText(nameReplacementConfig)
                        .replaceText(amountReplacementConfig));
                if (it.hasNext()) {
                    loreBuilder.append(Component.newline());
                }
            }

        } else { // not using custom lore, so just parse the lore regularly
            // append lore
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
                List<String> lore = itemStack.getItemMeta().getLore();

                for (Iterator<String> it = lore.iterator(); it.hasNext(); ) {
                    loreBuilder.append(LegacyComponentSerializer.legacySection().deserialize(it.next()));
                    if (it.hasNext()) {
                        loreBuilder.append(Component.newline());
                    }
                }
            } else {
                // append name to lore instead since the lore doesn't exist/length is 0
                loreBuilder.append(LegacyComponentSerializer.legacySection().deserialize(name));
            }
        }

        // create a new component for the ACTUAL item message replacement (e.g. [Enchanted Sword x1]
        Component itemMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(ITEM_CHAT_WITH_CHAT.get(get()))
                .replaceText(nameReplacementConfig)
                .replaceText(amountReplacementConfig)
                // remove all decoration from parent components above
                .decoration(TextDecoration.BOLD, TextDecoration.State.NOT_SET)
                .decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET)
                .decoration(TextDecoration.OBFUSCATED, TextDecoration.State.NOT_SET)
                .decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.NOT_SET)
                .decoration(TextDecoration.UNDERLINED, TextDecoration.State.NOT_SET);

        // set hover
        itemMessage = itemMessage.hoverEvent(HoverEvent.showText(loreBuilder.build()));

        Component finalItemMessage = itemMessage;

        // replace [item] (and other aliases) with the item message
        for (String s : ITEM_CHAT_REPLACE_ALIASES.get(get())) {
            message = message.replaceText(b ->
                    b.matchLiteral(s).replacement(finalItemMessage));
        }

        return message;
    }
}
