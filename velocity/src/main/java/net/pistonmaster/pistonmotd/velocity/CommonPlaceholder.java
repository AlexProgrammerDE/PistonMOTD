package net.pistonmaster.pistonmotd.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import lombok.RequiredArgsConstructor;
import net.pistonmaster.pistonmotd.api.PlaceholderParser;

@RequiredArgsConstructor
public class CommonPlaceholder implements PlaceholderParser {
    private final ProxyServer proxy;

    @Override
    public String parseString(String text) {
        text = text.replace("%online%", String.valueOf(proxy.getPlayerCount()));
        text = text.replace("%max%", String.valueOf(proxy.getConfiguration().getShowMaxPlayers()));
        text = text.replace("%newline%", "\n");

        return text;
    }
}
