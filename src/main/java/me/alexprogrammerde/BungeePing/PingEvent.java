package me.alexprogrammerde.BungeePing;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class PingEvent implements Listener {

    Configuration config;
    int online;
    int max;
    Plugin plugin;
    BufferedImage img;

    public PingEvent(Configuration config, Plugin plugin) {
        this.config = config;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        if (config.getBoolean("overrideonline")) {
            online = config.getInt("online");
        } else {
            online = event.getResponse().getPlayers().getOnline();
        }

        if (config.getBoolean("overridemax")) {
            max = config.getInt("max");
        } else {
            max = event.getResponse().getPlayers().getMax();
        }

        ServerPing.PlayerInfo one = new ServerPing.PlayerInfo(config.getString("text"), "1");
        ServerPing.PlayerInfo[] info = {one};
        ServerPing.Players players = new ServerPing.Players(max, online, info);

        ServerPing ping = new ServerPing(event.getResponse().getVersion(), players, event.getResponse().getDescriptionComponent(), event.getResponse().getFaviconObject());
        event.setResponse(ping);
    }
}
