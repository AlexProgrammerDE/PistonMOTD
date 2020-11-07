package me.alexprogrammerde.pistonmotd.bukkit;

import io.papermc.lib.PaperLib;
import me.alexprogrammerde.pistonmotd.api.PlaceholderParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonPlaceholder implements PlaceholderParser {
    @Override
    public String parseString(String text) {
        String returnedString = text;

        returnedString = returnedString.replaceAll("%online%", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));
        returnedString = returnedString.replaceAll("%max%", String.valueOf(Bukkit.getServer().getMaxPlayers()));
        returnedString = returnedString.replaceAll("%newline%", "\n");

        if (PaperLib.getMinecraftVersion() > 15) {
            returnedString = format(returnedString);
        }

        returnedString = ChatColor.translateAlternateColorCodes('&', returnedString);

        return returnedString;
    }

    // Got it from here: https://www.spigotmc.org/threads/448771/
    private final Pattern pattern = Pattern.compile("(?<!\\\\)(#[a-fA-F0-9]{6})");

    private String format(String message) {
        Matcher matcher = pattern.matcher(message); // Creates a matcher with the given pattern & message

        while (matcher.find()) { // Searches the message for something that matches the pattern
            String color = message.substring(matcher.start(), matcher.end()); // Extracts the color from the message
            message = message.replace(color, "" + net.md_5.bungee.api.ChatColor.of(color)); // Places the color in the message
        }

        return message; // Returns the message
    }
}
