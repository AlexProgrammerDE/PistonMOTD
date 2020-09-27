package me.alexprogrammerde.PistonMOTD.bungee;

import me.alexprogrammerde.PistonMOTD.api.PlaceholderParser;
import net.md_5.bungee.api.ProxyServer;

public class ServerPlaceholder implements PlaceholderParser {
    private final String server;

    public ServerPlaceholder(String server) {
        this.server = server;
    }

    @Override
    public String parseString(String text) {
        String returnedstring = text;

        returnedstring = returnedstring.replaceAll("%online_" + server + "%", String.valueOf(ProxyServer.getInstance().getServerInfo(server).getPlayers().size()));

        return returnedstring;
    }
}
