package net.pistonmaster.pistonmotd.bungee;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.pistonmaster.pistonmotd.kyori.PistonSerializersRelocated;
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlugin;
import net.pistonmaster.pistonmotd.shared.PistonStatusPing;
import net.pistonmaster.pistonmotd.shared.StatusFavicon;
import net.pistonmaster.pistonmotd.shared.StatusPingListener;
import net.pistonmaster.pistonmotd.shared.utils.PistonConstants;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public class PingEvent implements Listener, StatusPingListener {
    private final PistonMOTDPlugin plugin;

    @EventHandler
    public void onPing(ProxyPingEvent event) {
        handle(wrap(event));
    }

    private PistonStatusPing wrap(ProxyPingEvent event) {
        return new PistonStatusPing() {
            @Override
            public void setHidePlayers(boolean hidePlayers) throws UnsupportedOperationException {
                if (hidePlayers) {
                    event.getResponse().setPlayers(null);
                }
            }

            @Override
            public String getDescription() {
                return event.getResponse().getDescriptionComponent().toLegacyText();
            }

            @Override
            public void setDescription(String description) {
                if (supportsHex()) {
                    event.getResponse().setDescriptionComponent(new TextComponent(BungeeComponentSerializer.get().serialize(PistonSerializersRelocated.sectionRGB.deserialize(description))));
                } else {
                    event.getResponse().setDescriptionComponent(new TextComponent(BungeeComponentSerializer.legacy().serialize(PistonSerializersRelocated.sectionRGB.deserialize(description))));
                }
            }

            @Override
            public int getMax() {
                return event.getResponse().getPlayers().getMax();
            }

            @Override
            public void setMax(int max) {
                event.getResponse().getPlayers().setMax(max);
            }

            @Override
            public int getOnline() {
                return event.getResponse().getPlayers().getOnline();
            }

            @Override
            public void setOnline(int online) {
                event.getResponse().getPlayers().setOnline(online);
            }

            @Override
            public String getVersionName() {
                return event.getResponse().getVersion().getName();
            }

            @Override
            public void setVersionName(String name) {
                event.getResponse().getVersion().setName(name);
            }

            @Override
            public int getVersionProtocol() {
                return event.getResponse().getVersion().getProtocol();
            }

            @Override
            public void setVersionProtocol(int protocol) {
                event.getResponse().getVersion().setProtocol(protocol);
            }

            @Override
            public void clearSamples() throws UnsupportedOperationException {
                event.getResponse().getPlayers().setSample(new ServerPing.PlayerInfo[0]);
            }

            @Override
            public void addSample(UUID uuid, String name) throws UnsupportedOperationException {
                event.getResponse().getPlayers().setSample(
                        Stream.concat(Arrays.stream(event.getResponse().getPlayers().getSample()),
                                        Stream.of(new ServerPing.PlayerInfo(name, uuid)))
                                .toArray(ServerPing.PlayerInfo[]::new));
            }

            @Override
            public boolean supportsHex() {
                return event.getConnection().getVersion() >= PistonConstants.MINECRAFT_1_16;
            }

            @Override
            public void setFavicon(StatusFavicon favicon) {
                event.getResponse().setFavicon((Favicon) favicon.getValue());
            }

            @Override
            public int getClientProtocol() throws UnsupportedOperationException {
                return event.getConnection().getVersion();
            }

            @Override
            public InetSocketAddress getClientVirtualHost() throws UnsupportedOperationException {
                return event.getConnection().getVirtualHost();
            }
        };
    }
}
