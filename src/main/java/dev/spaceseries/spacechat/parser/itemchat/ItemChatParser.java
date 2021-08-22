package dev.spaceseries.spacechat.parser.itemchat;

import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.api.config.generic.adapter.ConfigurationAdapter;
import dev.spaceseries.spacechat.api.wrapper.Pair;
import dev.spaceseries.spacechat.config.SpaceChatConfigKeys;
import dev.spaceseries.spacechat.parser.Parser;
import dev.spaceseries.spacechat.util.nbt.NBTUtil;
import me.pikamug.localelib.LocaleManager;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Namespaced;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.translation.TranslationRegistry;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ItemChatParser extends Parser<Pair<Player, Component>, Component> {

    /**
     * Configuration
     */
    private final ConfigurationAdapter configuration;

    /**
     * Locale manager
     */
    private final LocaleManager localeManager;

    /**
     * Item Chat parser
     *
     * @param plugin plugin
     */
    public ItemChatParser(SpaceChatPlugin plugin) {
        super(plugin);
        this.configuration = plugin.getSpaceChatConfig().getAdapter();
        this.localeManager = new LocaleManager();
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
        if (!SpaceChatConfigKeys.ITEM_CHAT_ENABLED.get(configuration) || !player.hasPermission(SpaceChatConfigKeys.PERMISSIONS_USE_ITEM_CHAT.get(configuration))) {
            return message;
        }

        // get item in hand
        ItemStack itemStack = player.getItemInHand();

        // if null or air, return
        if (itemStack.getType().equals(Material.AIR) || itemStack.getAmount() <= 0) {
            return message;
        }

        // get item key
        String itemKey = localeManager.queryMaterial(itemStack.getType());

        // get display name
        Component name = itemStack.hasItemMeta() ?
                Objects.requireNonNull(itemStack.getItemMeta()).hasDisplayName() ? LegacyComponentSerializer.legacySection().deserialize(itemStack.getItemMeta().getDisplayName()) : Component.translatable(itemKey) :
                Component.translatable(itemKey);

        // replacement config for %item% and %amount%
        TextReplacementConfig nameReplacementConfig = TextReplacementConfig.builder()
                .matchLiteral("%name%").replacement(TextComponent.ofChildren(name))
                .build();

        TextReplacementConfig amountReplacementConfig = TextReplacementConfig.builder()
                .matchLiteral("%amount%")
                .replacement(Integer.toString(itemStack.getAmount()))
                .build();

        // convert lore (if exists)
        TextComponent.Builder loreBuilder = null;

        // if using custom lore, use that instead
        if (SpaceChatConfigKeys.ITEM_CHAT_WITH_LORE_USE_CUSTOM.get(configuration)) {
            List<String> lore = SpaceChatConfigKeys.ITEM_CHAT_WITH_LORE_CUSTOM.get(configuration);
            loreBuilder = Component.text();

            for (Iterator<String> it = lore.iterator(); it.hasNext(); ) {
                loreBuilder.append(LegacyComponentSerializer.legacyAmpersand().deserialize(it.next())
                        .replaceText(nameReplacementConfig)
                        .replaceText(amountReplacementConfig));
                if (it.hasNext()) {
                    loreBuilder.append(Component.newline());
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

        // create hover event
        HoverEvent<?> hoverEvent;

        if (loreBuilder != null) {
            hoverEvent = HoverEvent.showText(loreBuilder.build());
        } else {
            // show item

            // get namespaced key
            NBTCompound compound = NBTUtil.compoundFromItemStack(itemStack);
            String compoundId = compound != null ? compound.getString("id") : null;
            Key key;

            if (compoundId != null) {
                key = Key.key(compoundId);
            } else {
                try {
                    Material material = itemStack.getType();
                    NamespacedKey namespacedKey = material.getKey();
                    key = Key.key(namespacedKey.getNamespace(), namespacedKey.getKey());
                } catch (NoSuchMethodError ignored) {
                    key = Key.key(itemStack.getType().name());
                }
            }

            hoverEvent = HoverEvent.showItem(key, itemStack.getAmount(), NBTUtil.fromItemStack(itemStack));
        }

        itemMessage = itemMessage.hoverEvent(hoverEvent);

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
