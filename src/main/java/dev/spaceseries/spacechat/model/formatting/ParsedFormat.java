package dev.spaceseries.spacechat.model.formatting;

import com.google.gson.JsonElement;
import dev.spaceseries.spacechat.model.formatting.parsed.ConditionalParsedFormat;
import dev.spaceseries.spacechat.model.formatting.parsed.SimpleParsedFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;

public interface ParsedFormat {

    static ParsedFormat fromJson(JsonElement element) {
        if (element.isJsonArray()) {
            return new ConditionalParsedFormat(element.getAsJsonArray());
        } else {
            return new SimpleParsedFormat(GsonComponentSerializer.gson().deserialize(element.getAsString()));
        }
    }

    Component asComponent();

    Component asComponent(Player player);

    JsonElement asJson();
}
