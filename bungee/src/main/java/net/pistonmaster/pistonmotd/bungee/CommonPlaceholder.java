package net.pistonmaster.pistonmotd.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.pistonmaster.pistonmotd.api.PlaceholderParser;

public class CommonPlaceholder implements PlaceholderParser {
    @Override
    public String parseString(String text) {
        // Legacy placeholders. Don't touch them!
        text = text.replace("%real_players%", "%online%");
        text = text.replace("%displayed_players%", "%online%");
        text = text.replace("%real_max%", "%max%");
        text = text.replace("%displayed_max%", "%max%");

        text = text.replace("%online%", String.valueOf(ProxyServer.getInstance().getOnlineCount()));
        text = text.replace("%max%", String.valueOf(ProxyServer.getInstance().getConfig().getPlayerLimit()));
        text = text.replace("%newline%", "\n");

        return text;
    }
}
