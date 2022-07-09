package net.pistonmaster.pistonmotd.sponge;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlugin;
import net.pistonmaster.pistonmotd.shared.PistonStatusPing;
import net.pistonmaster.pistonmotd.shared.StatusFavicon;
import net.pistonmaster.pistonmotd.shared.StatusPingListener;
import net.pistonmaster.pistonmotd.shared.utils.PistonConstants;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.server.ClientPingServerEvent;
import org.spongepowered.api.network.status.Favicon;
import org.spongepowered.api.network.status.StatusResponse;
import org.spongepowered.api.profile.GameProfile;

import java.net.InetSocketAddress;
import java.util.OptionalInt;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PingEvent implements StatusPingListener {
    private final PistonMOTDPlugin plugin;

    @Listener
    public void onPing(ClientPingServerEvent event) {
        handle(wrap(event));
    }

    public PistonStatusPing wrap(ClientPingServerEvent event) {
        return new PistonStatusPing() {
            @Override
            public void setHidePlayers(boolean hidePlayers) throws UnsupportedOperationException {
                event.response().setHidePlayers(hidePlayers);
            }

            @Override
            public String getDescription() {
                return LegacyComponentSerializer.legacySection().serialize(event.response().description());
            }

            @Override
            public void setDescription(String description) {
                event.response().setDescription(LegacyComponentSerializer.legacySection().deserialize(description));
            }

            @Override
            public int getMax() {
                return event.response().players().map(StatusResponse.Players::max).orElse(0);
            }

            @Override
            public void setMax(int max) {
                event.response().players().ifPresent(players -> players.setMax(max));
            }

            @Override
            public int getOnline() throws UnsupportedOperationException {
                return event.response().players().map(StatusResponse.Players::online).orElse(0);
            }

            @Override
            public void setOnline(int online) throws UnsupportedOperationException {
                event.response().players().ifPresent(players -> players.setOnline(online));
            }

            @Override
            public String getVersionName() throws UnsupportedOperationException {
                return event.response().version().name();
            }

            @Override
            public void setVersionName(String name) throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Not supported on sponge!");
            }

            @Override
            public int getVersionProtocol() throws UnsupportedOperationException {
                return event.response().version().dataVersion().orElse(0);
            }

            @Override
            public void setVersionProtocol(int protocol) throws UnsupportedOperationException {
                throw new UnsupportedOperationException("Not supported on sponge!");
            }

            @Override
            public void clearSamples() throws UnsupportedOperationException {
                event.response().players().ifPresent(e -> e.profiles().clear());
            }

            @Override
            public void addSample(UUID uuid, String name) throws UnsupportedOperationException {
                event.response().players().ifPresent(e -> e.profiles().add(GameProfile.of(UUID.randomUUID(), name)));
            }

            @Override
            public boolean supportsHex() {
                OptionalInt version = event.client().version().dataVersion();

                if (version.isPresent()) {
                    return version.getAsInt() >= PistonConstants.MINECRAFT_1_16;
                } else {
                    return false;
                }
            }

            @Override
            public void setFavicon(StatusFavicon favicon) {
                event.response().setFavicon((Favicon) favicon.getValue());
            }

            @Override
            public int getClientProtocol() throws UnsupportedOperationException {
                return event.client().version().dataVersion().orElse(0);
            }

            @Override
            public InetSocketAddress getClientVirtualHost() throws UnsupportedOperationException {
                return event.client().virtualHost().orElse(null);
            }
        };
    }
}
