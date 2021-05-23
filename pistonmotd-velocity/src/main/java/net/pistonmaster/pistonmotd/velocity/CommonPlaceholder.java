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
        String returnedString = text;
        returnedString = returnedString.replace("%online%", String.valueOf(proxy.getPlayerCount()));
        returnedString = returnedString.replace("%max%", String.valueOf(proxy.getConfiguration().getShowMaxPlayers()));
        returnedString = returnedString.replace("%newline%", "\n");

        return returnedString;
    }
}
