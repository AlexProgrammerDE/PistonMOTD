package me.alexprogrammerde.PistonMOTD.velocity;

import com.google.common.reflect.TypeToken;
import com.velocitypowered.api.event.EventHandler;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import me.alexprogrammerde.PistonMOTD.api.PlaceholderUtil;
import net.kyori.adventure.text.Component;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class PingEvent implements EventHandler<ProxyPingEvent> {
    PistonMOTDVelocity plugin;

    public PingEvent(PistonMOTDVelocity plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(ProxyPingEvent event) {
        try {
            final ServerPing.Builder builder = event.getPing().asBuilder();
            final String afterIcon = "                                                                            ";
            ConfigurationNode node = plugin.rootNode;

            if (node.getNode("overrideonline", "activated").getBoolean()) {
                builder.onlinePlayers(node.getNode("overrideonline", "value").getInt());
            } else {
                builder.onlinePlayers(event.getPing().getPlayers().get().getOnline());
            }

            if (node.getNode("overridemax", "activated").getBoolean()) {
                builder.maximumPlayers(node.getNode("overridemax", "value").getInt());
            } else {
                builder.maximumPlayers(event.getPing().getPlayers().get().getMax());
            }

            if (node.getNode("playercounter", "activated").getBoolean()) {
                List<ServerPing.SamplePlayer> info = new ArrayList<>();

                for (String str : node.getNode("playercounter", "text").getList(TypeToken.of(String.class))) {
                    info.add(new ServerPing.SamplePlayer(PlaceholderUtil.parseText(str), UUID.randomUUID()));
                }

                builder.samplePlayers(info.toArray(new ServerPing.SamplePlayer[0]));
            } else {
                builder.samplePlayers(event.getPing().getPlayers().get().getSample().toArray(new ServerPing.SamplePlayer[0]));
            }

            if (node.getNode("motd", "activated").getBoolean()) {
                List<String> list = node.getNode("motd", "text").getList(TypeToken.of(String.class));
                builder.description(Component.text(PlaceholderUtil.parseText(list.get((int) Math.round(Math.random() * (list.size() - 1))))));
            } else {
                builder.description(event.getPing().getDescriptionComponent());
            }

            if (node.getNode("protocol", "activated").getBoolean()) {
                builder.version(new ServerPing.Version(event.getPing().getVersion().getProtocol(), PlaceholderUtil.parseText(Objects.requireNonNull(node.getNode("protocol", "text").getString()).replaceAll("%aftericon%", afterIcon))));

            } else {
                builder.version(event.getPing().getVersion());
            }

            event.setPing(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
