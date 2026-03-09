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
    private static final int SERVER_VERSION = Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]);

    private static final MiniMessage MINI_MESSAGE = SERVER_VERSION >= 16 ? MiniMessage.miniMessage() : null;
    private static final LegacyComponentSerializer LEGACY_SERIALIZER =
            SERVER_VERSION >= 16 ? LegacyComponentSerializer.legacySection() : null;

    public static String colorize(String text) {
        if (text == null || text.isEmpty()) return "";

        if (SERVER_VERSION < 16) {
            return ChatColor.translateAlternateColorCodes('&', text);
        }

        boolean containsLegacy = text.contains("&") || text.contains("§"); //skip

        text = transformLegacyHex(text);
        text = containsLegacy ? formatMiniMessage(text) : text;

        return LEGACY_SERIALIZER.serialize(MINI_MESSAGE.deserialize(text));
    }

    public static @NotNull Component deserialize(@NotNull String message) {
        if (SERVER_VERSION < 16) {
            return Component.text(ChatColor.translateAlternateColorCodes('&', message));
        }
        return MINI_MESSAGE.deserialize(formatMiniMessage(message));
    }

    public static @NotNull Component deserialize(@NotNull String message, @NotNull TagResolver... placeholders) {
        if (SERVER_VERSION < 16) {
            return Component.text(ChatColor.translateAlternateColorCodes('&', message));
        }
        return MINI_MESSAGE.deserialize(formatMiniMessage(message), placeholders);
    }

    public static void sendMessage(@NotNull Audience audience, @NotNull String message) {
        audience.sendMessage(deserialize(message));
    }

    @SuppressWarnings("Unused")
    public static void sendMessage(@NotNull Audience audience, @NotNull String message, @NotNull TagResolver... placeholders) {
        audience.sendMessage(deserialize(message, placeholders));
    }

    @SuppressWarnings("Unused")
    public static void sendMessages(@NotNull Audience audience, @NotNull List<String> message) {
        for (String line : message) {
            sendMessage(audience, line);
        }
    }

    @SuppressWarnings("Unused")
    public static @NotNull String deserializeToJson(@NotNull String text) {
        return GsonComponentSerializer.gson().serialize(deserialize(text));
    }

    @SuppressWarnings("Unused")
    public static @NotNull String deserializeToJson(@NotNull String text, @NotNull TagResolver... placeholders) {
        return GsonComponentSerializer.gson().serialize(deserialize(text, placeholders));
    }

    // Workaround to support both legacy and minimessage format.
    private static @NotNull String formatMiniMessage(String text) {
        // Parse legacy color format.
        text = ChatColor.translateAlternateColorCodes('&', text);
        // Parse legacy to minimessage format.
        TextComponent component = LEGACY_SERIALIZER.deserialize(text);
        // Set component to serialized format.
        String serialized = MINI_MESSAGE.serialize(component.compact());
        // Remove backslashes to normalize mini-message format.
        return serialized.replace("\\", "");
    }

    public static String transformLegacyHex(String text) {
        if (SERVER_VERSION < 16) return text;
        return HEX_PATTERN.matcher(text).replaceAll(m -> "<color:#" + m.group(2) + ">");
    }

    public static String processPlaceholders(final Player player, String text) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            return colorize(text);
        }
        return colorize(PlaceholderAPI.setPlaceholders(player, text));
    }
}
