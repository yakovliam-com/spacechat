package dev.spaceseries.spacechat.api.message;

import com.google.common.base.Joiner;
import dev.spaceseries.spaceapi.abstraction.server.Server;
import dev.spaceseries.spaceapi.command.SpaceCommandSender;
import dev.spaceseries.spaceapi.config.generic.adapter.ConfigurationAdapter;
import dev.spaceseries.spaceapi.config.generic.key.ConfigKey;
import dev.spaceseries.spaceapi.config.generic.key.ConfigKeyFactory;
import dev.spaceseries.spaceapi.util.ColorUtil;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Message {

    private static final char DELIMITER = '$';

    private final String ident;
    private final List<String> lines;
    private final List<Extra> extras;
    private final String richLine;
    private static AudienceProvider audienceProvider;

    public static void initAudience(AudienceProvider ap) {
        audienceProvider = ap;
    }

    public static AudienceProvider getAudienceProvider() {
        return audienceProvider;
    }

    private Message(String ident, List<String> lines, List<Extra> extras, String richLine) {
        this.ident = ident;
        this.lines = lines;
        this.extras = extras;
        this.richLine = richLine;
    }

    public String getIdent() {
        return ident;
    }

    public void msg(SpaceCommandSender sender, String... replacers) {
        msg(Collections.singletonList(sender), replacers);
    }

    public void msg(Iterable<SpaceCommandSender> senders, String... replacers) {
        // get component
        Component component = toComponent(replacers);

        for (SpaceCommandSender sender : senders) {
            if (!sender.isPlayer()) {
                audienceProvider.console().sendMessage(component);
            } else {
                audienceProvider.player(sender.getUuid()).sendMessage(component);
            }
        }
    }

    public static Builder fromConfigurationSection(ConfigurationAdapter configuration, String ident) {
        // create new builder
        Builder builder = Message.builder(ident);

        // if rich exists, return builder as-is but with rich
        String rich = configuration.getString(computeConfigurationPath(ident, "rich"), null);
        if (rich != null && !rich.isEmpty()) {
            // return builder with rich text
            return builder.setRichLine(rich);
        }

        // initialize empty list
        List<String> text = Collections.emptyList();

        // get text lines
        text = configuration.getStringList(computeConfigurationPath(ident, "text"), text);

        // set text in builder
        text.forEach(builder::addLine);

        // get list of keys
        Collection<String> extraKeys = configuration.getKeys(computeConfigurationPath(ident, "extras"), new ArrayList<>());

        // if no keys, return builder as-is
        if (extraKeys.size() <= 0) return builder;

        // initialize new extras list
        List<Extra> extras = new ArrayList<>();

        // loop through keys and create extras
        extraKeys.forEach(key -> {
            // get action
            String action = configuration.getString(computeConfigurationPath(ident, "extras." + "action"), null);

            // get content
            String content = configuration.getString(computeConfigurationPath(ident, "extras." + "content"), null);

            // (if applicable) get tooltip
            List<String> tooltip = configuration.getStringList(computeConfigurationPath(ident, "extras." + "tooltip"), null);

            // create new extra
            Extra extra = new Extra();

            // if there's an action, add it
            if (action != null && content != null) {
                Extra.ClickAction actionType;
                try {
                    actionType = Extra.ClickAction.valueOf(action.toUpperCase());

                    // set action
                    extra.withAction(actionType, content);
                } catch (Exception ignored) {
                }
            }

            // add tooltip (if not empty)
            if (!tooltip.isEmpty()) extra.withTooltip(tooltip);

            // add extra to extras list
            extras.add(extra);
        });

        // add extras to builder
        extras.forEach(builder::addExtra);

        // return builder
        return builder;
    }

    /**
     * Gets the configuration path for a specific config key (specifically message identification)
     *
     * @param ident ident
     * @param path  path
     * @return config path
     */
    private static String computeConfigurationPath(String ident, String path) {
        return ident + "." + path;
    }

    public void broadcast(String... replacers) {
        msg(Server.get().getOnlinePlayers().collect(Collectors.toList()), replacers);
    }

    public static Builder builder(String ident) {
        return new Builder(ident);
    }

    public Component toComponent(String... replacers) {
        ComponentBuilder<TextComponent, TextComponent.Builder> components = Component.text();

        // if rich text is present
        if (richLine != null && !richLine.isEmpty()) {
            // add the parsed lines to the components list
            components.append(MiniMessage.get().parse(richLine, replacers));
            // return as-is
            return components.build();
        }

        int count = 0;
        int loopCounter = 0;
        for (String text : lines) {
            text = ColorUtil.translateFromAmpersand(text);

            String left = null;
            for (String right : replacers) {
                if (left == null)
                    left = right;
                else {
                    text = text.replaceAll(Pattern.quote(left), Matcher.quoteReplacement(right));
                    left = null;
                }
            }

            ComponentBuilder<TextComponent, TextComponent.Builder> componentBuilder = Component.text();

            int i1, i2 = -1;
            do {
                i1 = text.indexOf(DELIMITER, i2 + 1);
                if (i1 != -1) {
                    componentBuilder.append(LegacyComponentSerializer.legacySection().deserialize(text.substring(i2 + 1, i1)));

                    i2 = text.indexOf(DELIMITER, i1 + 1);
                    if (i2 != -1) {
                        Component extras = LegacyComponentSerializer.legacySection().deserialize(text.substring(i1 + 1, i2));
                        ComponentBuilder<TextComponent, TextComponent.Builder> eventComponentBuilder = Component.text()
                                .append(extras);

                        Extra extra;

                        if (this.extras.size() <= count) {
                            extra = new Extra();
                        } else {
                            extra = this.extras.get(count++);
                        }

                        if (extra.tooltip != null) {
                            // join multi lines into one
                            String singleLineFromMultiLine = Joiner.on("\n").join(extra.tooltip.stream().iterator());

                            // create component from the line
                            Component component = Component.text()
                                    .append(LegacyComponentSerializer.legacyAmpersand().deserialize(singleLineFromMultiLine))
                                    .build();

                            // add hover event to main builder
                            eventComponentBuilder.hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, component));
                        }

                        if (extra.action != null) {
                            switch (extra.action) {
                                case RUN_COMMAND:
                                    eventComponentBuilder.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, extra.content));
                                    break;
                                case SUGGEST:
                                    eventComponentBuilder.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, extra.content));
                                    break;
                                case OPEN_URL:
                                    eventComponentBuilder.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, extra.content));
                                    break;
                            }
                        }

                        componentBuilder.append(eventComponentBuilder.build());
                    }

                }
            } while (i1 != -1 && i2 != -1);

            if (i1 == -1) {
                componentBuilder.append(LegacyComponentSerializer.legacySection().deserialize(text.substring(i2 + 1)));
            }

            components.append(loopCounter >= lines.size() - 1 ? componentBuilder.build() : componentBuilder.append(Component.newline()).build());

            loopCounter++;
        }

        return components.build();
    }

    public static class Extra {
        private List<String> tooltip;

        private ClickAction action;
        private String content;

        public Extra() {
        }

        public Extra withAction(ClickAction action, String content) {
            this.action = action;
            this.content = content;
            return this;
        }

        public Extra withTooltip(String... tooltip) {
            this.tooltip = Arrays.asList(tooltip);
            return this;
        }

        public Extra withTooltip(List<String> tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public enum ClickAction {
            OPEN_URL,
            RUN_COMMAND,
            SUGGEST
        }

        public List<String> getTooltip() {
            return tooltip;
        }

        public ClickAction getAction() {
            return action;
        }

        public String getContent() {
            return content;
        }
    }

    public static class Builder {

        private final String ident;

        private final List<String> lines = new ArrayList<>();
        private final List<Extra> extras = new ArrayList<>();
        private String richLine;

        public Builder(String ident) {
            this.ident = ident;
        }

        public Builder addLine(String line) {
            lines.add(line);
            return this;
        }

        public Builder addExtra(Extra extra) {
            extras.add(extra);
            return this;
        }

        public Builder setRichLine(String richLine) {
            this.richLine = richLine;
            return this;
        }

        public Message build() {
            return new Message(ident, lines, extras, richLine);
        }
    }

    public static class Global {
        public static final Message ACCESS_DENIED = Message.builder("access-denied")
                .addLine(ChatColor.RED + "Insufficient permissions!")
                .build();
    }
}
