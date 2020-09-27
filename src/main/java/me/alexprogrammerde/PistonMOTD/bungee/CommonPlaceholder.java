package me.alexprogrammerde.PistonMOTD.bungee;

import me.alexprogrammerde.PistonMOTD.api.PlaceholderParser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;

public class CommonPlaceholder implements PlaceholderParser {
    @Override
    public String parseString(String text) {
        String returnedstring = text;

        // TODO: Remove legacy placeholders
        returnedstring = returnedstring.replaceAll("%real_players%", "%online%");
        returnedstring = returnedstring.replaceAll("%displayed_players%", "%online%");
        returnedstring = returnedstring.replaceAll("%real_max%", "%max%");
        returnedstring = returnedstring.replaceAll("%displayed_max%", "%max%");

        returnedstring = returnedstring.replaceAll("%online%", String.valueOf(ProxyServer.getInstance().getOnlineCount()));
        returnedstring = returnedstring.replaceAll("%max%", String.valueOf(ProxyServer.getInstance().getConfig().getPlayerLimit()));
        returnedstring = returnedstring.replaceAll("%newline%", "\n");

        returnedstring = ChatColor.translateAlternateColorCodes('&', returnedstring);

        return returnedstring;
    }
}
