package net.pistonmaster.pistonmotd.bungee;

import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.pistonmaster.pistonmotd.api.PlaceholderParser;

@RequiredArgsConstructor
public class ServerPlaceholder implements PlaceholderParser {
    private final String server;

    @Override
    public String parseString(String text) {
        return text.replace("%online_" + server + "%", String.valueOf(ProxyServer.getInstance().getServerInfo(server).getPlayers().size()));
    }
}
