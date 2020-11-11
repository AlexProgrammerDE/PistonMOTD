package me.alexprogrammerde.pistonmotd.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import me.alexprogrammerde.pistonmotd.api.PlaceholderParser;

public class CommonPlaceholder implements PlaceholderParser {
    private final ProxyServer proxy;

    protected CommonPlaceholder(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public String parseString(String text) {
        String returnedString = text;
        returnedString = returnedString.replaceAll("%online%", String.valueOf(proxy.getPlayerCount()));
        returnedString = returnedString.replaceAll("%max%", String.valueOf(proxy.getConfiguration().getShowMaxPlayers()));
        returnedString = returnedString.replaceAll("%newline%", "\n");

        return returnedString;
    }
}
