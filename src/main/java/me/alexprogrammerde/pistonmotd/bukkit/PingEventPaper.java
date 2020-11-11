package me.alexprogrammerde.pistonmotd.bukkit;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import me.alexprogrammerde.pistonmotd.api.PlaceholderUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PingEventPaper implements Listener {
    private final PistonMOTDBukkit plugin;

    protected PingEventPaper(PistonMOTDBukkit plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPing(PaperServerListPingEvent event) {
        FileConfiguration config = plugin.getConfig();

        if (config.getBoolean("motd.activated")) {
            List<String> motd = config.getStringList("motd.text");
            event.setMotd(PlaceholderUtil.parseText(motd.get(ThreadLocalRandom.current().nextInt(0,  motd.size()))));
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

        if (plugin.getConfig().getBoolean("icons")) {
            File[] icons = plugin.icons.listFiles();

            List<File> validFiles = new ArrayList<>();

            if (icons != null && icons.length != 0) {
                for (File image : icons) {
                    if (FilenameUtils.getExtension(image.getPath()).equals("png")) {
                        validFiles.add(image);
                    }
                }

                try {
                    event.setServerIcon(plugin.getServer().loadServerIcon(validFiles.get((int) Math.round(Math.random() * (validFiles.size() - 1)))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
