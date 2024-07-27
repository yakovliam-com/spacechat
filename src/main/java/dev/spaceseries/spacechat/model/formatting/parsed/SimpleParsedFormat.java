package dev.spaceseries.spacechat.model.formatting.parsed;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.spaceseries.spacechat.model.formatting.ParsedFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;

public class SimpleParsedFormat implements ParsedFormat {

    private final Component component;

    public SimpleParsedFormat(Component component) {
        this.component = component;
    }

    @Override
    public Component asComponent() {
        return component;
    }

    @Override
    public Component asComponent(Player player) {
        return component;
    }

    @Override
    public JsonElement asJson() {
        return new JsonPrimitive(GsonComponentSerializer.gson().serialize(component));
    }
}
