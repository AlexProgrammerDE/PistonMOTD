package me.alexprogrammerde.pistonmotd.bungee;

import me.alexprogrammerde.pistonmotd.api.PlaceholderParser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;

public class CommonPlaceholder implements PlaceholderParser {
    @Override
    public String parseString(String text) {
        String returnedString = text;

        // TODO: Remove legacy placeholders
        returnedString = returnedString.replaceAll("%real_players%", "%online%");
        returnedString = returnedString.replaceAll("%displayed_players%", "%online%");
        returnedString = returnedString.replaceAll("%real_max%", "%max%");
        returnedString = returnedString.replaceAll("%displayed_max%", "%max%");

        returnedString = returnedString.replaceAll("%online%", String.valueOf(ProxyServer.getInstance().getOnlineCount()));
        returnedString = returnedString.replaceAll("%max%", String.valueOf(ProxyServer.getInstance().getConfig().getPlayerLimit()));
        returnedString = returnedString.replaceAll("%newline%", "\n");

        returnedString = ChatColor.translateAlternateColorCodes('&', returnedString);

        return returnedString;
    }
}
