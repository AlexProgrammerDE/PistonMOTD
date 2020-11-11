package me.alexprogrammerde.pistonmotd.velocity;

import com.google.common.reflect.TypeToken;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import me.alexprogrammerde.pistonmotd.api.PlaceholderUtil;
import me.alexprogrammerde.pistonmotd.utils.PistonConstants;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("UnstableApiUsage")
public class PingEvent {
    private final PistonMOTDVelocity plugin;

    protected PingEvent(PistonMOTDVelocity plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPing(ProxyPingEvent event) {
        try {
            final ServerPing.Builder builder = event.getPing().asBuilder();
            final String afterIcon = "                                                                            ";
            ConfigurationNode node = plugin.rootNode;

            if (node.getNode("overrideonline", "activated").getBoolean()) {
                builder.onlinePlayers(node.getNode("overrideonline", "value").getInt());
            }

            if (node.getNode("overridemax", "activated").getBoolean()) {
                builder.maximumPlayers(node.getNode("overridemax", "value").getInt());
            }

            if (node.getNode("playercounter", "activated").getBoolean()) {
                List<ServerPing.SamplePlayer> info = new ArrayList<>();

                for (String str : node.getNode("playercounter", "text").getList(TypeToken.of(String.class))) {
                    info.add(new ServerPing.SamplePlayer(PlaceholderUtil.parseText(str), UUID.randomUUID()));
                }

                builder.samplePlayers(info.toArray(new ServerPing.SamplePlayer[0]));
            }

            if (node.getNode("motd", "activated").getBoolean()) {
                List<String> motd = node.getNode("motd", "text").getList(TypeToken.of(String.class));

                if (event.getPing().getVersion().getProtocol() >= PistonConstants.MINECRAFT_1_16) {
                    builder.description(LegacyComponentSerializer.builder().character('§').hexCharacter('#').hexColors().build().deserialize(PlaceholderUtil.parseText(motd.get(ThreadLocalRandom.current().nextInt(0,  motd.size())))));
                } else {
                    builder.description(LegacyComponentSerializer.legacySection().deserialize(PlaceholderUtil.parseText(PlaceholderUtil.parseText(motd.get(ThreadLocalRandom.current().nextInt(0,  motd.size()))))));
                }
            }

            if (node.getNode("protocol", "activated").getBoolean()) {
                builder.version(new ServerPing.Version(event.getPing().getVersion().getProtocol(), PlaceholderUtil.parseText(Objects.requireNonNull(node.getNode("protocol", "text").getString()).replaceAll("%aftericon%", afterIcon))));
            }

            event.setPing(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
