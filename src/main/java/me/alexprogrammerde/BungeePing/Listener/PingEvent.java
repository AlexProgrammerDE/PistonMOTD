package me.alexprogrammerde.BungeePing.Listener;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PingEvent implements Listener {

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        ServerPing.PlayerInfo one = new ServerPing.PlayerInfo(ChatColor.DARK_AQUA + "6b" + ChatColor.AQUA + "6t", "1");
        ServerPing.PlayerInfo two = new ServerPing.PlayerInfo(ChatColor.WHITE + "■ Discord: " + ChatColor.GREEN + "discord.gg/6BSqkFH\n Hello", "2");
        ServerPing.PlayerInfo three = new ServerPing.PlayerInfo(ChatColor.WHITE + "■ Website: " + ChatColor.GREEN + "6b6t.org", "2");
        ServerPing.PlayerInfo four = new ServerPing.PlayerInfo(ChatColor.GOLD + " The best 2b2t clone!", "3");
        ServerPing.PlayerInfo[] info = {one, two, three};
        ServerPing.Players players = new ServerPing.Players(event.getResponse().getPlayers().getMax(), event.getResponse().getPlayers().getOnline(), info);
        ServerPing ping = new ServerPing(event.getResponse().getVersion(), players, event.getResponse().getDescriptionComponent(), event.getResponse().getFaviconObject());
        event.setResponse(ping);
    }
}
