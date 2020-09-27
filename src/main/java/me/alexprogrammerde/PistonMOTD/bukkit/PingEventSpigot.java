package me.alexprogrammerde.PistonMOTD.bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.meta.MapMeta;

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
            event.setMotd(PlaceholderUtilsBukkit.parseText(list.get((int) Math.round(Math.random() * (list.size() - 1))), event.getNumPlayers(), event.getMaxPlayers()));
        }

        if (plugin.getConfig().getBoolean("overridemax.activated")) {
            event.setMaxPlayers(plugin.getConfig().getInt("overridemax.value"));
        }
    }
}
