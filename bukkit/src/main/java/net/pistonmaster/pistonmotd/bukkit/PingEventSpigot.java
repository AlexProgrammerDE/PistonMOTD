package net.pistonmaster.pistonmotd.bukkit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlugin;
import net.pistonmaster.pistonmotd.shared.PistonStatusPing;
import net.pistonmaster.pistonmotd.shared.StatusFavicon;
import net.pistonmaster.pistonmotd.shared.StatusPingListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.util.CachedServerIcon;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@SuppressWarnings({"deprecation"})
public class PingEventSpigot implements Listener, StatusPingListener {
    private final PistonMOTDPlugin plugin;

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        handle(wrap(event));
    }

    private PistonStatusPing wrap(ServerListPingEvent event) {
        return new PistonStatusPing() {
            @Override
            public void hidePlayers() throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Spigot does not support this method");
            }

            @Override
            public String getDescriptionJson() {
                return GsonComponentSerializer.gson().serialize(LegacyComponentSerializer.legacySection().deserialize(event.getMotd()));
            }

            @Override
            public void setDescription(String descriptionJson) {
                event.setMotd(LegacyComponentSerializer.legacySection().serialize(GsonComponentSerializer.gson().deserialize(descriptionJson)));
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

            @Override
            public void clearSamples() throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Spigot does not support this method");
            }

            @Override
            public void addSample(UUID uuid, String name) throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Spigot does not support this method");
            }

            @Override
            public boolean supportsHex() {
                return false;
            }

            @Override
            public void setFavicon(StatusFavicon favicon) {
                event.setServerIcon((CachedServerIcon) favicon.getValue());
            }

            @Override
            public int getClientProtocol() throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Spigot does not support this method");
            }

            @Override
            public Optional<InetSocketAddress> getClientVirtualHost() throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Spigot does not support this method");
            }
        };
    }
}
