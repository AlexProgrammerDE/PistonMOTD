package me.alexprogrammerde.PistonMOTD.bukkit;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import me.alexprogrammerde.PistonMOTD.api.PlaceholderUtil;
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
            event.setMotd(PlaceholderUtil.parseText(list.get((int) Math.round(Math.random() * (list.size() - 1)))));
        }

        if (config.getBoolean("extended.protocol.activated")) {
            event.setVersion(PlaceholderUtil.parseText(config.getString("extended.protocol.activated")));
        }

        if (config.getBoolean("extended.playercounter.activated")) {
            event.getPlayerSample().clear();

            int i = 0;

            for (String str : config.getStringList("extended.playercounter.text")) {
                event.getPlayerSample().add(i, Bukkit.createProfile(PlaceholderUtil.parseText(str)));
                i++;
            }
        }

        if (config.getBoolean("overridemax.activated")) {
            event.setMaxPlayers(config.getInt("overridemax.value"));
        }

        if (config.getBoolean("extended.overrideonline.activated")) {
            event.setNumPlayers(config.getInt("extended.overrideonline.value"));
        }

        if (config.getBoolean("extended.hideplayers")) {
            event.setHidePlayers(config.getBoolean("extended.hideplayers.value"));
        }
    }
}
