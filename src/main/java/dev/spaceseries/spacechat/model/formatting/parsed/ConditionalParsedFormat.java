package dev.spaceseries.spacechat.model.formatting.parsed;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.spaceseries.spacechat.model.formatting.ParsedFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ConditionalParsedFormat implements ParsedFormat {

    private final List<ParsedFormatPart> formatParts;

    public ConditionalParsedFormat(JsonArray array) {
        this.formatParts = new ArrayList<>();
        for (JsonElement element : array) {
            this.formatParts.add(new ParsedFormatPart(element));
        }
    }

    public ConditionalParsedFormat(List<ParsedFormatPart> formatParts) {
        this.formatParts = formatParts;
    }

    public List<ParsedFormatPart> getFormatParts() {
        return formatParts;
    }

    public Component asComponent() {
        final ComponentBuilder<TextComponent, TextComponent.Builder> partComponentBuilder = Component.text();
        for (ParsedFormatPart formatPart : formatParts) {
            partComponentBuilder.append(formatPart.getComponent());
        }
        return partComponentBuilder.build();
    }

    @Override
    public Component asComponent(Player player) {
        final ComponentBuilder<TextComponent, TextComponent.Builder> partComponentBuilder = Component.text();
        for (ParsedFormatPart formatPart : formatParts) {
            partComponentBuilder.append(formatPart.getComponent(player));
        }
        return partComponentBuilder.build();
    }

    @Override
    public JsonElement asJson() {
        final JsonArray array = new JsonArray();
        for (ParsedFormatPart formatPart : formatParts) {
            array.add(formatPart.asJson());
        }
        return array;
    }

    public void clear() {
        formatParts.clear();
    }
}
