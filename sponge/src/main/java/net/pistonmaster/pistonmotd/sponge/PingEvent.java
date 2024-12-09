package net.pistonmaster.pistonmotd.sponge;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlugin;
import net.pistonmaster.pistonmotd.shared.PistonStatusPing;
import net.pistonmaster.pistonmotd.shared.StatusFavicon;
import net.pistonmaster.pistonmotd.shared.StatusPingListener;
import net.pistonmaster.pistonmotd.shared.utils.PMHelpers;
import net.pistonmaster.pistonmotd.shared.utils.PMUnsupportedConfigException;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.server.ClientPingServerEvent;
import org.spongepowered.api.network.status.Favicon;
import org.spongepowered.api.network.status.StatusResponse;
import org.spongepowered.api.profile.GameProfile;

import java.net.InetSocketAddress;
import java.util.Optional;
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
            public void hidePlayers() {
                event.response().setHidePlayers(true);
            }

            @Override
            public String getDescriptionJson() {
                return GsonComponentSerializer.gson().serialize(event.response().description());
            }

            @Override
            public void setDescription(String descriptionJson) {
                event.response().setDescription(GsonComponentSerializer.gson().deserialize(descriptionJson));
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
            public int getOnline() {
                return event.response().players().map(StatusResponse.Players::online).orElse(0);
            }

            @Override
            public void setOnline(int online) {
                event.response().players().ifPresent(players -> players.setOnline(online));
            }

            @Override
            public String getVersionName() {
                return event.response().version().name();
            }

            @Override
            public void setVersionName(String name) throws PMUnsupportedConfigException {
                throw new PMUnsupportedConfigException("Not supported on sponge!");
            }

            @Override
            public int getVersionProtocol() {
                return event.response().version().dataVersion().orElse(0);
            }

            @Override
            public void setVersionProtocol(int protocol) throws PMUnsupportedConfigException {
                throw new PMUnsupportedConfigException("Not supported on sponge!");
            }

            @Override
            public void clearSamples() {
                event.response().players().ifPresent(e -> e.profiles().clear());
            }

            @Override
            public void addSample(UUID uuid, String name) {
                event.response().players().ifPresent(e -> e.profiles().add(GameProfile.of(UUID.randomUUID(), name)));
            }

            @Override
            public boolean supportsHex() {
                return getClientProtocol() == -1 || getClientProtocol() >= PMHelpers.MINECRAFT_1_16;
            }

            @Override
            public void setFavicon(StatusFavicon favicon) {
                event.response().setFavicon((Favicon) favicon.getValue());
            }

            @Override
            public int getClientProtocol() {
                return event.client().version().dataVersion().orElse(-1);
            }

            @Override
            public Optional<InetSocketAddress> getClientVirtualHost() {
                return event.client().virtualHost();
            }
        };
    }
}
