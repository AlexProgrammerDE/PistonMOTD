package net.pistonmaster.pistonmotd.velocity;

import com.google.common.reflect.TypeToken;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.kyori.PistonSerializersNormal;
import net.pistonmaster.pistonmotd.shared.PistonStatusPing;
import net.pistonmaster.pistonmotd.shared.StatusPingListener;
import net.pistonmaster.pistonmotd.shared.utils.MOTDUtil;
import net.pistonmaster.pistonmotd.shared.utils.PistonConstants;
import ninja.leaping.configurate.ConfigurationNode;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("UnstableApiUsage")
public class PingEvent implements StatusPingListener {
    private final PistonMOTDVelocity plugin;
    private List<Favicon> favicons;
    private ThreadLocalRandom random;

    protected PingEvent(PistonMOTDVelocity plugin) {
        this.plugin = plugin;
        if (plugin.rootNode.getNode("icons").getBoolean()) {
            favicons = loadFavicons();
            random = ThreadLocalRandom.current();
        }
    }

    @Subscribe
    public void onPing(ProxyPingEvent event) {
        handle(wrap(event, event.getPing().asBuilder()));
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

            if (node.getNode("playercounter", "bukkitplayercounter").getBoolean()) {
                List<ServerPing.SamplePlayer> info = new ArrayList<>();

                for (Player player : plugin.server.getAllPlayers()) {
                    info.add(new ServerPing.SamplePlayer(PlaceholderUtil.parseText(player.getUsername() + "§r"), UUID.randomUUID()));
                }

                builder.samplePlayers(info.toArray(new ServerPing.SamplePlayer[0]));
            } else if (node.getNode("hooks", "luckpermsplayercounter").getBoolean() && plugin.luckpermsWrapper != null) {
                List<ServerPing.SamplePlayer> info = new ArrayList<>();

                for (Player player : plugin.server.getAllPlayers()) {
                    CachedMetaData metaData = plugin.luckpermsWrapper.luckperms.getPlayerAdapter(Player.class).getMetaData(player);

                    String prefix = metaData.getPrefix() == null ? "" : metaData.getPrefix();

                    String suffix = metaData.getSuffix() == null ? "" : metaData.getSuffix();

                    info.add(new ServerPing.SamplePlayer(PlaceholderUtil.parseText(prefix + player.getUsername() + suffix), UUID.randomUUID()));
                }

                builder.samplePlayers(info.toArray(new ServerPing.SamplePlayer[0]));
            } else if (node.getNode("playercounter", "activated").getBoolean()) {
                List<ServerPing.SamplePlayer> info = new ArrayList<>();

                for (String str : node.getNode("playercounter", "text").getList(TypeToken.of(String.class))) {
                    info.add(new ServerPing.SamplePlayer(PlaceholderUtil.parseText(str), UUID.randomUUID()));
                }

                builder.samplePlayers(info.toArray(new ServerPing.SamplePlayer[0]));
            }

            if (node.getNode("motd", "activated").getBoolean()) {
                boolean supportsHex = event.getConnection().getProtocolVersion().getProtocol() >= PistonConstants.MINECRAFT_1_16;
                String motd = MOTDUtil.getMOTD(node.getNode("motd", "text").getList(TypeToken.of(String.class)), supportsHex, PlaceholderUtil::parseText);

                if (supportsHex) {
                    builder.description(PistonSerializersNormal.sectionRGB.deserialize(motd));
                } else {
                    builder.description(PistonSerializersNormal.section.deserialize(motd));
                }
            }

            if (node.getNode("protocol", "activated").getBoolean() && node.getNode("overrideprotocolnumber", "activated").getBoolean()) {
                builder.version(new ServerPing.Version(node.getNode("overrideprotocolnumber", "value").getInt(), PlaceholderUtil.parseText(Objects.requireNonNull(node.getNode("protocol", "text").getString()).replace("%aftericon%", afterIcon))));
            } else if (node.getNode("protocol", "activated").getBoolean()) {
                builder.version(new ServerPing.Version(event.getPing().getVersion().getProtocol(), PlaceholderUtil.parseText(Objects.requireNonNull(node.getNode("protocol", "text").getString()).replace("%aftericon%", afterIcon))));
            } else if (node.getNode("overrideprotocolnumber", "activated").getBoolean()) {
                builder.version(new ServerPing.Version(node.getNode("overrideprotocolnumber", "value").getInt(), event.getPing().getVersion().getName()));
            }

            if (node.getNode("icons").getBoolean()) {
                builder.favicon(favicons.get(random.nextInt(0, favicons.size())));
            }

            event.setPing(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Favicon> loadFavicons() {
        File[] icons = plugin.icons.listFiles();

        List<File> validFiles = new ArrayList<>();

        if (icons != null && icons.length != 0) {
            for (File image : icons) {
                if (FilenameUtils.getExtension(image.getPath()).equals("png")) {
                    validFiles.add(image);
                }
            }
        }
        return Arrays.asList(validFiles.stream().map(this::createFavicon).filter(Objects::nonNull).toArray(Favicon[]::new));
    }

    private Favicon createFavicon(File file) {
        try {
            return Favicon.create(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
                boolean supportsHex = event.getConnection().getProtocolVersion().getProtocol() >= PistonConstants.MINECRAFT_1_16;

                if (supportsHex) {
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
        };
    }
}
