package me.alexprogrammerde.pistonmotd.bungee;

import me.alexprogrammerde.pistonmotd.api.PlaceholderParser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonPlaceholder implements PlaceholderParser {
    @Override
    public String parseString(String text) {
        String returnedString = text;

        // Legacy placeholders. Don't touch them!
        returnedString = returnedString.replaceAll("%real_players%", "%online%");
        returnedString = returnedString.replaceAll("%displayed_players%", "%online%");
        returnedString = returnedString.replaceAll("%real_max%", "%max%");
        returnedString = returnedString.replaceAll("%displayed_max%", "%max%");

        returnedString = returnedString.replaceAll("%online%", String.valueOf(ProxyServer.getInstance().getOnlineCount()));
        returnedString = returnedString.replaceAll("%max%", String.valueOf(ProxyServer.getInstance().getConfig().getPlayerLimit()));
        returnedString = returnedString.replaceAll("%newline%", "\n");

        returnedString = format(returnedString);

        returnedString = ChatColor.translateAlternateColorCodes('&', returnedString);

        return returnedString;
    }

    // Got it from here: https://www.spigotmc.org/threads/448771/
    private final Pattern pattern = Pattern.compile("(?<!\\\\)(#[a-fA-F0-9]{6})");

    private String format(String message) {
        Matcher matcher = pattern.matcher(message); // Creates a matcher with the given pattern & message

        while (matcher.find()) { // Searches the message for something that matches the pattern
            String color = message.substring(matcher.start(), matcher.end()); // Extracts the color from the message
            message = message.replace(color, "" + ChatColor.of(color)); // Places the color in the message
        }

        return message; // Returns the message
    }
}
