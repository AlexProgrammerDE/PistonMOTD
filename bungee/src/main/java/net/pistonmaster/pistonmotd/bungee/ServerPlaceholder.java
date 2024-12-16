package net.pistonmaster.pistonmotd.bungee;

import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.pistonmaster.pistonmotd.api.PlaceholderParser;

import java.util.Map;

@RequiredArgsConstructor
public class ServerPlaceholder implements PlaceholderParser {
    private final ProxyServer proxyServer;

    @Override
    public String parseString(String text) {
        for (Map.Entry<String, ServerInfo> entry : proxyServer.getServers().entrySet()) {
            text = text.replace("%online_" + entry.getKey() + "%", String.valueOf(entry.getValue().getPlayers().size()));
            text = text.replace("<online_" + entry.getKey() + ">", String.valueOf(entry.getValue().getPlayers().size()));
        }
        return text;
    }
}
