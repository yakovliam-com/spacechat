package dev.spaceseries.spacechat.parser.itemchat;

import com.saicone.ezlib.Dependencies;
import com.saicone.ezlib.Dependency;
import com.saicone.ezlib.Repository;
import com.saicone.rtag.item.ItemObject;
import com.saicone.rtag.tag.TagBase;
import com.saicone.rtag.tag.TagCompound;
import dev.spaceseries.spacechat.SpaceChatPlugin;
import dev.spaceseries.spacechat.api.config.generic.adapter.ConfigurationAdapter;
import dev.spaceseries.spacechat.config.SpaceChatConfigKeys;
import dev.spaceseries.spacechat.parser.Parser;
import me.pikamug.localelib.LocaleManager;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Dependencies({
        @Dependency(value = "com.github.PikaMug:LocaleLib:338b52b0dc",
                repository = @Repository(url = "https://jitpack.io"),
                relocate = {"me.pikamug.localelib", "{package}.lib.localelib"}
        ),
        @Dependency(value = "com.saicone.rtag:rtag-item:1.3.1",
                repository = @Repository(url = "https://jitpack.io"),
                relocate = {"com.saicone.rtag", "{package}.lib.rtag"}
        )
})
public class ItemChatParser extends Parser {

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

    @Override
    public Component parse(Player player, Component message) {

        boolean containsItemChatAliases = false;
        for (String s : SpaceChatConfigKeys.ITEM_CHAT_REPLACE_ALIASES.get(configuration)) {
            if (componentContains(message, s)) {
                containsItemChatAliases = true;
            }
        }

        if (!containsItemChatAliases) {
            return message;
        }

        // if not enabled, return
        if (!SpaceChatConfigKeys.ITEM_CHAT_ENABLED.get(configuration)) {
            return message;
        }

        if (!player.hasPermission(SpaceChatConfigKeys.PERMISSIONS_USE_ITEM_CHAT.get(configuration))) {
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
                .matchLiteral("%name%").replacement(name)
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
            try {
                hoverEvent = getItemHoverEvent(itemStack);
            } catch (Throwable t) {
                hoverEvent = HoverEvent.showText(Component.text());
            }
        }

        itemMessage = itemMessage.hoverEvent(hoverEvent);

        Component finalItemMessage = itemMessage;

        // replace [item] (and other aliases) with the item message

        // keep track of the count
        for (String s : SpaceChatConfigKeys.ITEM_CHAT_REPLACE_ALIASES.get(configuration)) {
            message = message.replaceText(b -> {
                if (SpaceChatConfigKeys.ITEM_CHAT_MAX_PER_MESSAGE.get(configuration) != -1) {
                    b.times(SpaceChatConfigKeys.ITEM_CHAT_MAX_PER_MESSAGE.get(configuration)).matchLiteral(s).replacement(finalItemMessage);
                } else {
                    b.matchLiteral(s).replacement(finalItemMessage);
                }
            });
        }

        return message;
    }

    /**
     * Check if component contains text
     *
     * @param component Component
     * @param s text
     * @return true if contains
     */
    public boolean componentContains(Component component, String s) {
        if (component instanceof TextComponent && ((TextComponent) component).content().contains(s)) {
            return true;
        }
        for (Component child : component.children()) {
            if (componentContains(child, s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Parse item to hover event
     *
     * @param item ItemStack to parse
     * @return     A hover event that represent the item
     * @throws Throwable if any error occurs on reflected method invoking.
     */
    public HoverEvent<?> getItemHoverEvent(ItemStack item) throws Throwable {
        final Object compound = ItemObject.save(ItemObject.asNMSCopy(item));
        if (compound == null) {
            throw new NullPointerException("Item compound cannot be null");
        }

        // This can be simplified with Rtag.INSTANCE.get(compound, "id");
        // but consumes a bit more
        Object id = TagBase.getValue(TagCompound.get(compound, "id"));

        Key key;
        if (id != null) {
            key = Key.key(String.valueOf(id));
        } else {
            try {
                Material material = item.getType();
                NamespacedKey namespacedKey = material.getKey();
                key = Key.key(namespacedKey.getNamespace(), namespacedKey.getKey());
            } catch (NoSuchMethodError ignored) {
                key = Key.key(item.getType().name());
            }
        }

        Object tag = TagCompound.get(compound, "tag");
        final Set<String> allowedTags;
        if (tag != null && !(allowedTags = SpaceChatConfigKeys.ITEM_CHAT_ALLOWED_TAGS.get(configuration)).isEmpty()) {
            TagCompound.getValue(tag).entrySet().removeIf(entry -> !allowedTags.contains(entry.getKey()));
        }
        return HoverEvent.showItem(key, item.getAmount(), tag == null ? null : BinaryTagHolder.binaryTagHolder(tag.toString()));
    }
}