package me.alexprogrammerde.PistonMOTD.bukkit;

import me.alexprogrammerde.PistonMOTD.api.PlaceholderParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class CommonPlaceholder implements PlaceholderParser {
    @Override
    public String parseString(String text) {
        String returnedstring = text;

        returnedstring = returnedstring.replaceAll("%online%", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));
        returnedstring = returnedstring.replaceAll("%max%", String.valueOf(Bukkit.getServer().getMaxPlayers()));
        returnedstring = returnedstring.replaceAll("%newline%", "\n");

        returnedstring = ChatColor.translateAlternateColorCodes('&', returnedstring);

        return returnedstring;
    }
}
