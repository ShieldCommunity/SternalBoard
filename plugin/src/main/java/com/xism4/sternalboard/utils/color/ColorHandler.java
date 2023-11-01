package com.xism4.sternalboard.utils.color;

public abstract class ColorHandler {

    private static ColorHandler INSTANCE = null;

    public static ColorHandler get() {
        if (INSTANCE == null) {
            INSTANCE = create();
        }
        return INSTANCE;
    }

    private static ColorHandler create() {
        try {
            Class.forName("net.md_5.bungee.api.ChatColor");

            return new BungeeColor();
        } catch (ClassNotFoundException e) {
            return new BukkitColor();
        }
    }

    public abstract String execute(String text);

    public static String convert(String text) {
        return get().execute(text);
    }

}