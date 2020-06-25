package me.alexprogrammerde.BungeePing;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class PingEvent implements Listener {

    Configuration config;
    int online;
    int max;
    Plugin plugin;
    BufferedImage img;
    ServerPing.Players players;
    BaseComponent motd;
    int i = 0;

    Array array;
    ServerPing.PlayerInfo[] info = {};

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

        if (config.getBoolean("playercounter.activated")) {
            int i = 0;

            for (String str : config.getStringList("playercounter.text")) {

                info = addInfo(info, new ServerPing.PlayerInfo(str, String.valueOf(i)));
                i++;
            }

            players = new ServerPing.Players(max, online, info);
        } else {
            players = new ServerPing.Players(max, online, event.getResponse().getPlayers().getSample());
        }

        if (config.getBoolean("motd.activated")) {
            List list = config.getList("motd.text");
            motd = new TextComponent(list.get((int) Math.round(Math.random() * (list.size() - 1))).toString());
        } else {
            motd = event.getResponse().getDescriptionComponent();
        }

        ServerPing ping = new ServerPing(event.getResponse().getVersion(), players, motd, event.getResponse().getFaviconObject());
        event.setResponse(ping);

        event.getConnection().getUniqueId();
    }

    public static ServerPing.PlayerInfo[] addInfo(ServerPing.PlayerInfo[] arr, ServerPing.PlayerInfo info)
    {
        int i;

        // create a new array of size n+1
        ServerPing.PlayerInfo[] newarr = new ServerPing.PlayerInfo[arr.length + 1];

        // insert the elements from
        // the old array into the new array
        // insert all elements till n
        // then insert x at n+1
        for (i = 0; i < arr.length; i++)
            newarr[i] = arr[i];

        newarr[arr.length] = info;

        return newarr;
    }
}
