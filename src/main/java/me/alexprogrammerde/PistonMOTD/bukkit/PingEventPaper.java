package me.alexprogrammerde.PistonMOTD.bukkit;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.util.List;

public class PingEventPaper implements Listener {
    PistonMOTDBukkit plugin;

    public PingEventPaper(PistonMOTDBukkit plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPing(PaperServerListPingEvent event) {
        FileConfiguration config = plugin.getConfig();

        if (config.getBoolean("motd.activated")) {
            List<String> list = config.getStringList("motd.text");
            event.setMotd(PlaceholderUtilsBukkit.parseText(list.get((int) Math.round(Math.random() * (list.size() - 1))), event.getNumPlayers(), event.getMaxPlayers()));
        }

        if (config.getBoolean("extended.protocol.activated")) {
            event.setVersion(PlaceholderUtilsBukkit.parseText(config.getString("extended.protocol.activated"), event.getNumPlayers(), event.getMaxPlayers()));
        }

        if (config.getBoolean("extended.playercounter.activated")) {
            event.getPlayerSample().clear();

            int i = 0;

            for (String str : config.getStringList("extended.playercounter.text")) {
                event.getPlayerSample().add(i, Bukkit.createProfile(PlaceholderUtilsBukkit.parseText(str, event.getNumPlayers(), event.getMaxPlayers())));
                i++;
            }
        }

        if (config.getBoolean("overridemax.activated")) {
            event.setMaxPlayers(config.getInt("overridemax.value"));
        }

        if (config.getBoolean("extended.overrideonline.activated")) {
            event.setNumPlayers(config.getInt("extended.overrideonline.value"));
        }

        if (config.getBoolean("extended.hideplayers.activated")) {
            event.setHidePlayers(config.getBoolean("extended.hideplayers.value"));
        }
    }
}
