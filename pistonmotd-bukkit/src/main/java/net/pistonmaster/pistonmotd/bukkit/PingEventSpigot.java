package net.pistonmaster.pistonmotd.bukkit;

import io.papermc.lib.PaperLib;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.utils.PistonSerializers;
import net.kyori.adventure.text.Component;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PingEventSpigot implements Listener {
    private final PistonMOTDBukkit plugin;

    protected PingEventSpigot(PistonMOTDBukkit plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        if (plugin.getConfig().getBoolean("motd.activated")) {
            List<String> motd = plugin.getConfig().getStringList("motd.text");

            Component motdComponent = PistonSerializers.unusualSectionRGB.deserialize(PlaceholderUtil.parseText(motd.get(ThreadLocalRandom.current().nextInt(0,  motd.size()))));

            if (PaperLib.getMinecraftVersion() >= 16) {
                event.setMotd(PistonSerializers.unusualSectionRGB.serialize(motdComponent));
            } else {
                event.setMotd(PistonSerializers.section.serialize(motdComponent));
            }
        }

        if (plugin.getConfig().getBoolean("overridemax.activated")) {
            event.setMaxPlayers(plugin.getConfig().getInt("overridemax.value"));
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
