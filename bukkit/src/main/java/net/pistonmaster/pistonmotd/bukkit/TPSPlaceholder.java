package net.pistonmaster.pistonmotd.bukkit;

import net.pistonmaster.pistonmotd.api.PlaceholderParser;
import org.bukkit.Bukkit;

public class TPSPlaceholder implements PlaceholderParser {
  @Override
  public String parseString(String text) {
    String tps = String.valueOf(Math.min(Math.round(Bukkit.getTPS()[0] * 100.0) / 100.0, 20.0));
    text = text.replace("%tps%", tps);
    text = text.replace("<tps>", tps);

    return text;
  }
}
