package net.pistonmaster.pistonmotd.bukkit;

import net.pistonmaster.pistonmotd.api.PlaceholderParser;
import org.bukkit.Bukkit;

public class CommonPlaceholder implements PlaceholderParser {
    @Override
    public String parseString(String text) {
        text = text.replace("%online%", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));
        text = text.replace("%max%", String.valueOf(Bukkit.getServer().getMaxPlayers()));
        text = text.replace("%newline%", "\n");

        return text;
    }
}
