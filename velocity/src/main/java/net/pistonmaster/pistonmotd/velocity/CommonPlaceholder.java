package net.pistonmaster.pistonmotd.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import net.pistonmaster.pistonmotd.api.PlaceholderParser;

public class CommonPlaceholder implements PlaceholderParser {
    private final ProxyServer proxy;

    protected CommonPlaceholder(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public String parseString(String text) {
        text = text.replace("%online%", String.valueOf(proxy.getPlayerCount()));
        text = text.replace("%max%", String.valueOf(proxy.getConfiguration().getShowMaxPlayers()));
        text = text.replace("%newline%", "\n");

        return text;
    }
}
