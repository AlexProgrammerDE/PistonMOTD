package me.alexprogrammerde.PistonMOTD.bukkit;

import me.alexprogrammerde.PistonMOTD.api.PlaceholderParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class CommonPlaceholder implements PlaceholderParser {
    @Override
    public String parseString(String text) {
        String returnedString = text;

        returnedString = returnedString.replaceAll("%online%", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));
        returnedString = returnedString.replaceAll("%max%", String.valueOf(Bukkit.getServer().getMaxPlayers()));
        returnedString = returnedString.replaceAll("%newline%", "\n");

        returnedString = ChatColor.translateAlternateColorCodes('&', returnedString);

        return returnedString;
    }
}
