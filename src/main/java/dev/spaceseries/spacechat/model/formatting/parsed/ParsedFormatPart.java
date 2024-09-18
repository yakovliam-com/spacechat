package dev.spaceseries.spacechat.model.formatting.parsed;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.spaceseries.spacechat.util.version.PlayerProtocol;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;

public class ParsedFormatPart {

    private final Component textComponent;
    private final Component lineComponent;
    private final int lineProtocol;

    protected ParsedFormatPart(JsonElement element) {
        if (element.isJsonPrimitive()) {
            this.textComponent = GsonComponentSerializer.gson().deserialize(element.getAsString());
            this.lineComponent = null;
            this.lineProtocol = -1;
        } else {
            final JsonObject json = element.getAsJsonObject();
            final JsonElement textElement = json.get("text");
            final JsonElement lineElement = json.get("line");
            final JsonElement lineProtocol = json.get("lineProtocol");
            this.textComponent = textElement == null ? null : GsonComponentSerializer.gson().deserialize(textElement.getAsString());
            this.lineComponent = lineElement == null ? null : GsonComponentSerializer.gson().deserialize(lineElement.getAsString());
            this.lineProtocol = lineProtocol == null ? -1 : lineProtocol.getAsInt();
        }
    }

    public ParsedFormatPart(Component textComponent, Component lineComponent, int lineProtocol) {
        this.textComponent = textComponent;
        this.lineComponent = lineComponent;
        this.lineProtocol = lineProtocol;
    }

    public JsonElement asJson() {
        if (lineComponent == null) {
            return new JsonPrimitive(GsonComponentSerializer.gson().serialize(textComponent));
        } else {
            final JsonObject json = new JsonObject();
            if (textComponent != null) {
                json.addProperty("text", GsonComponentSerializer.gson().serialize(textComponent));
            }
            json.addProperty("line", GsonComponentSerializer.gson().serialize(lineComponent));
            json.addProperty("lineProtocol", lineProtocol);
            return json;
        }
    }

    public Component getComponent() {
        return getComponent(null);
    }

    public Component getComponent(Player player) {
        if (lineComponent != null) {
            if (player == null || lineProtocol < 0 || PlayerProtocol.getProtocol(player) >= lineProtocol) {
                return lineComponent;
            }
        }
        return textComponent;
    }

    public Component getTextComponent() {
        return textComponent;
    }

    public Component getLineComponent() {
        return lineComponent;
    }

    public int getLineProtocol() {
        return lineProtocol;
    }
}
