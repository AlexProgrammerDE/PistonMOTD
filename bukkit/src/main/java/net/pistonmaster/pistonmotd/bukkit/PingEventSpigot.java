package net.pistonmaster.pistonmotd.bukkit;

import io.papermc.lib.PaperLib;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.kyori.PistonSerializersRelocated;
import net.pistonmaster.pistonmotd.shared.PistonStatusPing;
import net.pistonmaster.pistonmotd.shared.StatusPingListener;
import net.pistonmaster.pistonmotd.shared.utils.MOTDUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;

@RequiredArgsConstructor
public class PingEventSpigot implements Listener, StatusPingListener {
    private final PistonMOTDBukkit plugin;

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        handle(wrap(event));
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

    private PistonStatusPing wrap(ServerListPingEvent event) {
        return new PistonStatusPing() {
            @Override
            public void setHidePlayers(boolean hidePlayers) throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Spigot does not support this method");
            }

            @Override
            public String getDescription() {
                return event.getMotd();
            }

            @Override
            public void setDescription(String description) {
                event.setMotd(description);
            }

            @Override
            public int getMax() {
                return event.getMaxPlayers();
            }

            @Override
            public void setMax(int max) {
                event.setMaxPlayers(max);
            }

            @Override
            public int getOnline() {
                return event.getNumPlayers();
            }

            @Override
            public void setOnline(int online) throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Spigot does not support this method");
            }

            @Override
            public String getVersionName() throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Spigot does not support this method");
            }

            @Override
            public void setVersionName(String name) throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Spigot does not support this method");
            }

            @Override
            public int getVersionProtocol() throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Spigot does not support this method");
            }

            @Override
            public void setVersionProtocol(int protocol) throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Spigot does not support this method");
            }

        };
    }
}
