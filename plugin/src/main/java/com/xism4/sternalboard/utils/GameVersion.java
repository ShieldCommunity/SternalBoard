package com.xism4.sternalboard.utils;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameVersion {
    v1_7_R0,
    v1_7_R1,
    v1_7_R2,
    v1_7_R3,
    v1_8_R1,
    v1_8_R2,
    v1_8_R3,
    v1_9_R1,
    v1_9_R2,
    v1_10_R1,
    v1_11_R1,
    v1_12_R1,
    v1_13_R1,
    v1_13_R2,
    v1_14_R1,
    v1_15_R1,
    v1_16_R1,
    v1_16_R2,
    v1_16_R3,
    v1_16_R4,
    v1_16_R5,
    v1_17_R0,
    v1_17_R1,
    v1_18_R0,
    v1_18_R1,
    v1_18_R2,
    v1_19_R0,
    v1_19_R1,
    v1_19_R2,
    v1_19_R3,
    v1_20_R1,
    v1_20_R2,
    v1_20_R3;


    private static final GameVersion CURRENT_VERSION;

    static {
        CURRENT_VERSION = extractCurrentVersion();
    }

    private static GameVersion extractCurrentVersion() {
        String nmsVersionName = extractNMSVersion();
        if (nmsVersionName != null)
            try {
                return valueOf(nmsVersionName);
            } catch (IllegalArgumentException ignored) {
                return GameVersion.v1_19_R1;
            }
        return GameVersion.v1_19_R1;
    }

    private static String extractNMSVersion() {
        Matcher matcher = Pattern.compile("v\\d+_\\d+_R\\d+").matcher(Bukkit.getServer().getClass().getPackage().getName());
        if (matcher.find())
            return matcher.group();
        return null;
    }

    public static GameVersion getCurrent() {
        if (CURRENT_VERSION == null)
            throw new IllegalStateException("Current version not set");
        return CURRENT_VERSION;
    }

    public static boolean isGreaterEqualThan(GameVersion other) {
        return (getCurrent().ordinal() >= other.ordinal());
    }

    public static boolean isBetween(GameVersion from, GameVersion to) {
        return (from.ordinal() <= getCurrent().ordinal() && getCurrent().ordinal() <= to.ordinal());
    }
}