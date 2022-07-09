package net.pistonmaster.pistonmotd.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.pistonmaster.pistonmotd.kyori.PistonSerializersNormal;
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlugin;
import net.pistonmaster.pistonmotd.shared.PistonStatusPing;
import net.pistonmaster.pistonmotd.shared.StatusFavicon;
import net.pistonmaster.pistonmotd.shared.StatusPingListener;
import net.pistonmaster.pistonmotd.shared.utils.PistonConstants;

import java.net.InetSocketAddress;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PingEvent implements StatusPingListener {
    private final PistonMOTDPlugin plugin;

    @Subscribe
    public void onPing(ProxyPingEvent event) {
        ServerPing.Builder builder = event.getPing().asBuilder();
        handle(wrap(event, builder));
        event.setPing(builder.build());
    }

    private PistonStatusPing wrap(ProxyPingEvent event, ServerPing.Builder builder) {
        return new PistonStatusPing() {
            @Override
            public void setHidePlayers(boolean hidePlayers) throws UnsupportedOperationException {
                if (hidePlayers) {
                    builder.nullPlayers();
                }
            }

            @Override
            public String getDescription() {
                return LegacyComponentSerializer.legacySection().serialize(builder.getDescriptionComponent().orElse(Component.empty()));
            }

            @Override
            public void setDescription(String description) {
                if (supportsHex()) {
                    builder.description(PistonSerializersNormal.sectionRGB.deserialize(description));
                } else {
                    builder.description(PistonSerializersNormal.section.deserialize(description));
                }
            }

            @Override
            public int getMax() {
                return builder.getMaximumPlayers();
            }

            @Override
            public void setMax(int max) {
                builder.maximumPlayers(max);
            }

            @Override
            public int getOnline() {
                return builder.getOnlinePlayers();
            }

            @Override
            public void setOnline(int online) {
                builder.onlinePlayers(online);
            }

            @Override
            public String getVersionName() {
                return builder.getVersion().getName();
            }

            @Override
            public void setVersionName(String name) {
                builder.version(new ServerPing.Version(builder.getVersion().getProtocol(), name));
            }

            @Override
            public int getVersionProtocol() {
                return builder.getVersion().getProtocol();
            }

            @Override
            public void setVersionProtocol(int protocol) {
                builder.version(new ServerPing.Version(protocol, builder.getVersion().getName()));
            }

            @Override
            public void clearSamples() throws UnsupportedOperationException {
                builder.clearSamplePlayers();
            }

            @Override
            public void addSample(UUID uuid, String name) throws UnsupportedOperationException {
                builder.samplePlayers(new ServerPing.SamplePlayer(name, uuid));
            }

            @Override
            public boolean supportsHex() {
                return getClientProtocol() >= PistonConstants.MINECRAFT_1_16;
            }

            @Override
            public void setFavicon(StatusFavicon favicon) {
                builder.favicon((Favicon) favicon.getValue());
            }

            @Override
            public int getClientProtocol() throws UnsupportedOperationException {
                return event.getConnection().getProtocolVersion().getProtocol();
            }

            @Override
            public InetSocketAddress getClientVirtualHost() throws UnsupportedOperationException {
                return event.getConnection().getVirtualHost().orElse(null);
            }
        };
    }
}
