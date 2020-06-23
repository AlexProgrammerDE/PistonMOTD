package me.alexprogrammerde.BungeePing;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PingEvent implements Listener {

    String text;

    public PingEvent(String text) {
        this.text = text;
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        ServerPing.PlayerInfo one = new ServerPing.PlayerInfo(text, "1");
        ServerPing.PlayerInfo[] info = {one};
        ServerPing.Players players = new ServerPing.Players(event.getResponse().getPlayers().getMax(), event.getResponse().getPlayers().getOnline(), info);
        ServerPing ping = new ServerPing(event.getResponse().getVersion(), players, event.getResponse().getDescriptionComponent(), event.getResponse().getFaviconObject());
        event.setResponse(ping);
    }
}
