package net.pistonmaster.pistonmotd.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.pistonmaster.pistonmotd.api.PlaceholderParser;

public class ServerPlaceholder implements PlaceholderParser {
    private final String server;

    protected ServerPlaceholder(String server) {
        this.server = server;
    }

    @Override
    public String parseString(String text) {
        String returnedString = text;

        returnedString = returnedString.replaceAll("%online_" + server + "%", String.valueOf(ProxyServer.getInstance().getServerInfo(server).getPlayers().size()));

        return returnedString;
    }
}
