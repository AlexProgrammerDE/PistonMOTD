package net.pistonmaster.pistonmotd.bukkit;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlugin;
import net.pistonmaster.pistonmotd.shared.PistonStatusPing;
import net.pistonmaster.pistonmotd.shared.StatusFavicon;
import net.pistonmaster.pistonmotd.shared.StatusPingListener;
import net.pistonmaster.pistonmotd.shared.utils.PistonConstants;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.CachedServerIcon;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PingEventPaper implements Listener, StatusPingListener {
    private final PistonMOTDPlugin plugin;

    @EventHandler
    public void onPing(PaperServerListPingEvent event) {
        handle(wrap(event));
    }

    private PistonStatusPing wrap(PaperServerListPingEvent event) {
        return new PistonStatusPing() {
            @Override
            public void hidePlayers() throws UnsupportedOperationException {
                event.setHidePlayers(true);
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
            public void setOnline(int online) {
                event.setNumPlayers(online);
            }

            @Override
            public String getVersionName() {
                return event.getVersion();
            }

            @Override
            public void setVersionName(String name) {
                event.setVersion(name);
            }

            @Override
            public int getVersionProtocol() {
                return event.getProtocolVersion();
            }

            @Override
            public void setVersionProtocol(int protocol) {
                event.setProtocolVersion(protocol);
            }

            @Override
            public void clearSamples() throws UnsupportedOperationException {
                event.getPlayerSample().clear();
            }

            @Override
            public void addSample(UUID uuid, String name) throws UnsupportedOperationException {
                event.getPlayerSample().add(Bukkit.createProfile(uuid, name));
            }

            @Override
            public boolean supportsHex() {
                return getClientProtocol() == -1 || getClientProtocol() >= PistonConstants.MINECRAFT_1_16;
            }

            @Override
            public void setFavicon(StatusFavicon favicon) {
                event.setServerIcon((CachedServerIcon) favicon.getValue());
            }

            @Override
            public int getClientProtocol() throws UnsupportedOperationException {
                return event.getClient().getProtocolVersion();
            }

            @Override
            public Optional<InetSocketAddress> getClientVirtualHost() throws UnsupportedOperationException {
                return Optional.ofNullable(event.getClient().getVirtualHost());
            }
        };
    }
}
