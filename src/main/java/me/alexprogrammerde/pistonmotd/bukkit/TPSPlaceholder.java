package me.alexprogrammerde.pistonmotd.bukkit;

import me.alexprogrammerde.pistonmotd.api.PlaceholderParser;
import org.bukkit.Bukkit;

public class TPSPlaceholder implements PlaceholderParser {
    @Override
    public String parseString(String text) {
        return text.replaceAll("%tps%", String.valueOf(Math.min( Math.round( Bukkit.getTPS()[0] * 100.0 ) / 100.0, 20.0 )));
    }
}
