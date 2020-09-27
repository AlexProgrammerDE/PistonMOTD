package me.alexprogrammerde.PistonMOTD.bungee;

import net.md_5.bungee.api.ChatColor;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class PlaceholderUtilsBungee {
    private static final HashMap<String, String> placeholders = new HashMap<>();

    public static String parseText(String text, int online, int max) {
        String returnedstring = text;

        // TODO: Remove legacy placeholders
        returnedstring = returnedstring.replaceAll("%real_players%", "%online%");
        returnedstring = returnedstring.replaceAll("%displayed_players%", "%online%");
        returnedstring = returnedstring.replaceAll("%real_max%", "%max%");
        returnedstring = returnedstring.replaceAll("%displayed_max%", "%max%");

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
