package com.xism4.sternalboard.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Pattern;

public final class TextUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("(#|&#)([A-Fa-f0-9]{6})");
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();
    private static final int SERVER_VERSION = Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]);

    public static String colorize(String text) {
        if (text == null || text.isEmpty()) return "";

        boolean containsLegacyFormat = text.contains("&") || text.contains("§");
        if (containsLegacyFormat && SERVER_VERSION < 16) {
            return ChatColor.translateAlternateColorCodes('&', text);
        }

        text = transformLegacyHex(text);
        text = containsLegacyFormat ? formatMiniMessage(text) : text;
        return LEGACY_SERIALIZER.serialize(MINI_MESSAGE.deserialize(text));
    }

    public static @NotNull Component deserialize(final @NotNull String message) {
        return MINI_MESSAGE.deserialize(formatMiniMessage(message));
    }

    public static @NotNull Component deserialize(final @NotNull String message, final @NotNull TagResolver... placeholders) {
        return MINI_MESSAGE.deserialize(formatMiniMessage(message), placeholders);
    }

    public static void sendMessage(final @NotNull Audience audience, final @NotNull String message) {
        audience.sendMessage(deserialize(message));
    }

    public static void sendMessage(final @NotNull Audience audience, final @NotNull String message, final @NotNull TagResolver... placeholders) {
        audience.sendMessage(deserialize(message, placeholders));
    }

    public static void sendMessages(final @NotNull Audience audience, final @NotNull List<String> message) {
        for (final String line : message) {
            sendMessage(audience, line);
        }
    }

    public static @NotNull String deserializeToJson(final @NotNull String text) {
        return GsonComponentSerializer.gson().serialize(deserialize(text));
    }

    public static @NotNull String deserializeToJson(final @NotNull String text, final @NotNull TagResolver... placeholders) {
        return GsonComponentSerializer.gson().serialize(deserialize(text, placeholders));
    }

    // Workaround to support both legacy and minimessage format.
    private static @NotNull String formatMiniMessage(String text) {
        // Parse legacy color format.
        text = ChatColor.translateAlternateColorCodes('&', text);
        // Parse legacy to minimessage format.
        TextComponent textComponent = LEGACY_SERIALIZER.deserialize(text);
        // Set component to serialized format.
        String serialized = MINI_MESSAGE.serialize(textComponent.compact());
        // Remove backslashes to normalize mini-message format.
        return serialized.replace("\\", "");
    }

    public static String transformLegacyHex(String text) {
        return HEX_PATTERN.matcher(text).replaceAll(matcher -> "<color:#" + matcher.group(2) + ">");
    }

    public static String processPlaceholders(final Player player, String text) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            return colorize(text);
        }
        return colorize(PlaceholderAPI.setPlaceholders(player, text));
    }
}
