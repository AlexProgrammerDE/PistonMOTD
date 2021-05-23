package net.pistonmaster.pistonmotd.bukkit;

import net.pistonmaster.pistonmotd.api.PlaceholderParser;
import org.bukkit.Bukkit;

public class CommonPlaceholder implements PlaceholderParser {
    @Override
    public String parseString(String text) {
        String returnedString = text;

        returnedString = returnedString.replace("%online%", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));
        returnedString = returnedString.replace("%max%", String.valueOf(Bukkit.getServer().getMaxPlayers()));
        returnedString = returnedString.replace("%newline%", "\n");

        return returnedString;
    }
}
