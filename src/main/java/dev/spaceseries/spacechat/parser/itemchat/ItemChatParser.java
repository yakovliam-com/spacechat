package dev.spaceseries.spacechat.parser.itemchat;

import dev.spaceseries.spaceapi.config.generic.adapter.ConfigurationAdapter;
import dev.spaceseries.spaceapi.util.Pair;
import dev.spaceseries.spacechat.SpaceChat;
import dev.spaceseries.spacechat.config.SpaceChatConfig;
import dev.spaceseries.spacechat.config.SpaceChatConfigKeys;
import dev.spaceseries.spacechat.parser.Parser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.*;
import java.util.stream.Collectors;

public class ItemChatParser extends Parser<Pair<Player, Component>, Component> {

    private final ConfigurationAdapter configuration;

    public ItemChatParser(SpaceChat plugin) {
        super(plugin);
        this.configuration = plugin.getSpaceChatConfig().getAdapter();
    }

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
        if (!SpaceChatConfigKeys.ITEM_CHAT_ENABLED.get(configuration) || !player.hasPermission(SpaceChatConfigKeys.PERMISSIONS_USE_ITEM_CHAT.get(configuration)))
            return message;

        // get item in hand
        ItemStack itemStack = player.getItemInHand();

        // if null or air, return
        if (itemStack.getType().equals(Material.AIR) || itemStack.getAmount() <= 0) return message;

        // get display name
        String name = itemStack.hasItemMeta() ?
                Objects.requireNonNull(itemStack.getItemMeta()).hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : WordUtils.capitalize(itemStack.getType().name().replace("_", " ").toLowerCase()) :
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
        if (SpaceChatConfigKeys.ITEM_CHAT_WITH_LORE_USE_CUSTOM.get(configuration)) {
            List<String> lore = SpaceChatConfigKeys.ITEM_CHAT_WITH_LORE_CUSTOM.get(configuration);

            for (Iterator<String> it = lore.iterator(); it.hasNext(); ) {
                loreBuilder.append(LegacyComponentSerializer.legacyAmpersand().deserialize(it.next())
                        .replaceText(nameReplacementConfig)
                        .replaceText(amountReplacementConfig));
                if (it.hasNext()) {
                    loreBuilder.append(Component.newline());
                }
            }

        } else { // not using custom lore, so just parse the lore regularly

            // always append name to lore first
            loreBuilder.append(LegacyComponentSerializer.legacySection().deserialize(name));

            boolean hasLore = itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore();
            boolean hasEnchantments = itemStack.getEnchantments().size() >= 1 || (itemStack.hasItemMeta() && itemStack.getItemMeta() instanceof EnchantmentStorageMeta);

            // if either lore or enchantments, append a newline
            if (hasEnchantments || hasLore) loreBuilder.append(Component.newline());


            // append enchantments
            if (hasEnchantments) {
                Map<Enchantment, Integer> enchantmentMap;

                // if it is enchantment storage meta
                if (itemStack.hasItemMeta() && itemStack.getItemMeta() instanceof EnchantmentStorageMeta) {
                    // get meta
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
                    // set map
                    enchantmentMap = meta.getStoredEnchants();
                } else { // else regular enchantments
                    enchantmentMap = itemStack.getEnchantments();
                }

                // credits to PlanetTeamSpeak#4157 for informing me about translatable locale components
                List<TextComponent> enchantments = enchantmentMap.entrySet().stream()
                        .filter(entry -> "minecraft".equalsIgnoreCase(entry.getKey().getKey().getNamespace()))
                        .map(entry -> Component.empty().style(Style.style(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
                                .append(Component.translatable("enchantment.minecraft." + entry.getKey().getKey().getKey()))
                                .append(entry.getKey().getMaxLevel() > 1 ? Component.space()
                                        .append(Component.translatable("enchantment.level." + entry.getValue())) : Component.empty()))
                        .collect(Collectors.toList());

                // loop through enchantments and apply to lore
                for (Iterator<TextComponent> it = enchantments.iterator(); it.hasNext(); ) {
                    loreBuilder.append(it.next());

                    // if there's a next enchantment, append a newline
                    if (it.hasNext()) {
                        loreBuilder.append(Component.newline());
                    }
                }
            } else if (hasLore) {
                List<String> lore = itemStack.getItemMeta().getLore();

                // loop through lore
                for (Iterator<String> it = lore.iterator(); it.hasNext(); ) {
                    loreBuilder.append(LegacyComponentSerializer.legacySection().deserialize(it.next()));

                    // if not the end, append a newline
                    if (it.hasNext()) {
                        loreBuilder.append(Component.newline());
                    }
                }
            }
        }

        // create a new component for the ACTUAL item message replacement (e.g. [Enchanted Sword x1]
        Component itemMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(SpaceChatConfigKeys.ITEM_CHAT_WITH_CHAT.get(configuration))
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

        // keep track of the count
        for (String s : SpaceChatConfigKeys.ITEM_CHAT_REPLACE_ALIASES.get(configuration)) {
            message = message.replaceText(b -> {
                if (SpaceChatConfigKeys.ITEM_CHAT_MAX_PER_MESSAGE.get(configuration) != -1)
                    b.times(SpaceChatConfigKeys.ITEM_CHAT_MAX_PER_MESSAGE.get(configuration)).matchLiteral(s).replacement(finalItemMessage);
                else
                    b.matchLiteral(s).replacement(finalItemMessage);
            });
        }

        return message;
    }
}
