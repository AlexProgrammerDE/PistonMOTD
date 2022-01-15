package net.pistonmaster.pistonmotd.velocity;

import com.google.common.reflect.TypeToken;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.shared.utils.MOTDUtil;
import net.pistonmaster.pistonmotd.shared.utils.PistonConstants;
import net.pistonmaster.pistonmotd.shared.utils.PistonSerializers;
import ninja.leaping.configurate.ConfigurationNode;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("UnstableApiUsage")
public class PingEvent {
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
                boolean supportsHex = event.getPing().getVersion().getProtocol() >= PistonConstants.MINECRAFT_1_16;
                String motd = MOTDUtil.getMOTD(node.getNode("motd", "text").getList(TypeToken.of(String.class)), supportsHex, PlaceholderUtil::parseText);

                if (supportsHex) {
                    builder.description(PistonSerializers.sectionRGB.deserialize(motd));
                } else {
                    builder.description(PistonSerializers.section.deserialize(motd));
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
}
