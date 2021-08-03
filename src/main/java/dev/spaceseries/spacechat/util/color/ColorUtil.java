package dev.spaceseries.spacechat.util.color;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtil {

    private static final Pattern URL = Pattern.compile("^(?:(https?)://)?([-\\w_.]{2,}\\.[a-z]{2,4})(/\\S*)?$");
    public static final Pattern HEX_PATTERN = Pattern.compile("&\\(#([A-Fa-f0-9]{6})\\)");
    public static final char COLOR_CHAR = '\u00A7';
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-FK-ORX]");

    /**
     * Converts string into baseComponents
     *
     * @param message message
     * @return basecomponent array
     */
    @Deprecated
    public static BaseComponent[] fromLegacyText(String message) {
        return fromLegacyText(message, ChatColor.WHITE);
    }

    /**
     * Converts string into baseComponents
     *
     * @param message      message
     * @param defaultColor the default color
     * @return basecomponent array
     */
    @Deprecated
    public static BaseComponent[] fromLegacyText(String message, ChatColor defaultColor) {
        List<BaseComponent> components = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        TextComponent component = new TextComponent();
        Matcher matcher = URL.matcher(message);

        for (int i = 0; i < message.length(); ++i) {
            char c = message.charAt(i);
            TextComponent old;
            if (c == 167) {
                ++i;
                if (i >= message.length()) {
                    break;
                }

                c = message.charAt(i);
                if (c >= 'A' && c <= 'Z') {
                    c = (char) (c + 32);
                }

                ChatColor format;
                if (c == 'x' && i + 12 < message.length()) {
                    StringBuilder hex = new StringBuilder("#");

                    for (int j = 0; j < 6; ++j) {
                        hex.append(message.charAt(i + 2 + j * 2));
                    }

                    try {
                        format = ChatColor.of(hex.toString());
                    } catch (IllegalArgumentException var11) {
                        format = null;
                    }

                    i += 12;
                } else {
                    format = ChatColor.getByChar(c);
                }

                if (format != null) {
                    if (builder.length() > 0) {
                        old = component;
                        component = new TextComponent(component);
                        old.setText(builder.toString());
                        builder = new StringBuilder();
                        components.add(old);
                    }

                    if (format == ChatColor.BOLD) {
                        component.setBold(true);
                    } else if (format == ChatColor.ITALIC) {
                        component.setItalic(true);
                    } else if (format == ChatColor.UNDERLINE) {
                        component.setUnderlined(true);
                    } else if (format == ChatColor.STRIKETHROUGH) {
                        component.setStrikethrough(true);
                    } else if (format == ChatColor.MAGIC) {
                        component.setObfuscated(true);
                    } else if (format == ChatColor.RESET) {
                        r(component);
                        component.setColor(defaultColor);
                        component.setBold(false);
                    } else {
                        component = new TextComponent();
                        component.setColor(format);
                    }
                }
            } else {
                int pos = message.indexOf(32, i);
                if (pos == -1) {
                    pos = message.length();
                }

                if (matcher.region(i, pos).find()) {
                    if (builder.length() > 0) {
                        old = component;
                        component = new TextComponent(component);
                        old.setText(builder.toString());
                        builder = new StringBuilder();
                        components.add(old);
                    }

                    old = component;
                    component = new TextComponent(component);
                    String urlString = message.substring(i, pos);
                    component.setText(urlString);
                    component.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(ClickEvent.Action.OPEN_URL, urlString.startsWith("http") ? urlString : "http://" + urlString));
                    components.add(component);
                    i += pos - i - 1;
                    component = old;
                } else {
                    builder.append(c);
                }
            }
        }

        component.setText(builder.toString());
        components.add(component);
        return components.toArray(new BaseComponent[0]);
    }

    /**
     * Resets a component
     *
     * @param component component
     */
    @Deprecated
    private static void r(TextComponent component) {
        component.setItalic(false);
        component.setUnderlined(false);
        component.setStrikethrough(false);
        component.setObfuscated(false);
        component.setBold(false);
    }

    /**
     * Translates from ampersand
     *
     * @param message message
     * @return translated
     */
    @Deprecated
    public static String translateFromAmpersand(String message) {
        try {
            Matcher matcher = HEX_PATTERN.matcher(message);
            StringBuffer buffer = new StringBuffer(message.length() + 32);

            while (matcher.find()) {
                String group = matcher.group(1);
                matcher.appendReplacement(buffer, ChatColor.of("#" + group).toString());
            }

            return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
        } catch (NoSuchMethodError var4) {
            return ChatColor.translateAlternateColorCodes('&', message);
        }
    }

    /**
     * Strips color
     *
     * @param input input
     * @return stripped string
     */
    public static String stripColor(final String input) {
        if (input == null) {
            return null;
        }

        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
}