package me.alexprogrammerde.PistonMOTD.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;

public class PingEventSpigot implements Listener {
    PistonMOTDBukkit plugin;

    public PingEventSpigot(PistonMOTDBukkit plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        if (plugin.getConfig().getBoolean("motd.activated")) {
            List<String> list = plugin.getConfig().getStringList("motd.text");
            event.setMotd(parseText(list.get((int) Math.round(Math.random() * (list.size() - 1))), event.getMaxPlayers(), event.getNumPlayers()));
        }

        if (plugin.getConfig().getBoolean("overridemax.activated")) {
            event.setMaxPlayers(plugin.getConfig().getInt("overridemax.value"));
        }
    }

    public String parseText(String text, int max, int online) {
        String returnedstring = text;

        returnedstring = returnedstring.replaceAll("%online%", String.valueOf(online));
        returnedstring = returnedstring.replaceAll("%max%", String.valueOf(max));
        returnedstring = returnedstring.replaceAll("%newline%", "\n");

        returnedstring = ChatColor.translateAlternateColorCodes('&', returnedstring);

        return returnedstring;
    }
}
