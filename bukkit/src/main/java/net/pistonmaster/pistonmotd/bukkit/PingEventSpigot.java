package net.pistonmaster.pistonmotd.bukkit;

import io.papermc.lib.PaperLib;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.kyori.PistonSerializersRelocated;
import net.pistonmaster.pistonmotd.shared.utils.MOTDUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;

@RequiredArgsConstructor
public class PingEventSpigot implements Listener {
    private final PistonMOTDBukkit plugin;

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        if (plugin.getConfig().getBoolean("motd.activated")) {
            List<String> motd = plugin.getConfig().getStringList("motd.text");

            boolean supportsHex = PaperLib.getMinecraftVersion() >= 16;
            Component motdComponent = PistonSerializersRelocated.unusualSectionRGB.deserialize(MOTDUtil.getMOTD(motd, supportsHex, PlaceholderUtil::parseText));

            if (supportsHex) {
                event.setMotd(PistonSerializersRelocated.unusualSectionRGB.serialize(motdComponent));
            } else {
                event.setMotd(PistonSerializersRelocated.section.serialize(motdComponent));
            }
        }

        if (plugin.getConfig().getBoolean("overridemax.activated")) {
            event.setMaxPlayers(plugin.getConfig().getInt("overridemax.value"));
        }

        if (plugin.getConfig().getBoolean("icons")) {
            event.setServerIcon(plugin.favicons.get(plugin.random.nextInt(0, plugin.favicons.size())));
        }
    }
}
