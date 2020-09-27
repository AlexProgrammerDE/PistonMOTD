package me.alexprogrammerde.PistonMOTD.bukkit;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class PlaceholderUtilsBukkit {
    private static final HashMap<String, String> placeholders = new HashMap<>();

    public static String parseText(String text, int online, int max) {
        String returnedstring = text;

        returnedstring = returnedstring.replaceAll("%online%", String.valueOf(online));
        returnedstring = returnedstring.replaceAll("%max%", String.valueOf(max));
        returnedstring = returnedstring.replaceAll("%newline%", "\n");

        for (String str : placeholders.keySet()) {
            returnedstring = returnedstring.replaceAll(str, placeholders.get(str));
        }

        returnedstring = ChatColor.translateAlternateColorCodes('&', returnedstring);

        return returnedstring;
    }

    public static void addPlaceholder(@Nonnull String key, @Nonnull String value) {
        placeholders.put(key, value);
    }

    public static void removePlaceholder(@Nonnull String key) {
        placeholders.remove(key);
    }
}
