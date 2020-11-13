package me.alexprogrammerde.pistonmotd.bungee;

import me.alexprogrammerde.pistonmotd.api.PlaceholderUtil;
import me.alexprogrammerde.pistonmotd.utils.PistonSerializers;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.ProtocolConstants;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PingEvent implements Listener {
    private final PistonMOTDBungee plugin;
    private final File iconFolder;

    protected PingEvent(PistonMOTDBungee plugin, File icons) {
        this.plugin = plugin;
        this.iconFolder = icons;
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) throws IOException {
        int online;
        int max;
        ServerPing.Players players;
        BaseComponent motd;
        ServerPing.Protocol protocol;
        final String afterIcon = "                                                                            ";
        Favicon icon;
        final Configuration config = plugin.config;

        if (config.getBoolean("overrideonline.activated")) {
            online = config.getInt("overrideonline.value");
        } else {
            online = event.getResponse().getPlayers().getOnline();
        }

        if (config.getBoolean("overridemax.activated")) {
            max = config.getInt("overridemax.value");
        } else {
            max = event.getResponse().getPlayers().getMax();
        }

        if (config.getBoolean("hooks.luckpermsplayercounter") && plugin.luckpermsWrapper != null) {
            List<ServerPing.PlayerInfo> info = new ArrayList<>();

            int i = 0;

            for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
                CachedMetaData metaData = plugin.luckpermsWrapper.luckperms.getPlayerAdapter(ProxiedPlayer.class).getMetaData(player);

                String prefix = metaData.getPrefix() == null ? "" : metaData.getPrefix();

                String suffix = metaData.getSuffix() == null ? "" : metaData.getSuffix();

                info.add(new ServerPing.PlayerInfo(ChatColor.translateAlternateColorCodes('&',  prefix + player.getDisplayName() + suffix), String.valueOf(i)));
                i++;
            }

            players = new ServerPing.Players(max, online, info.toArray(new ServerPing.PlayerInfo[0]));
        } else {
            if (config.getBoolean("playercounter.activated")) {
                List<ServerPing.PlayerInfo> info = new ArrayList<>();

                int i = 0;

                for (String str : config.getStringList("playercounter.text")) {
                    info.add(new ServerPing.PlayerInfo(PlaceholderUtil.parseText(str), String.valueOf(i)));
                    i++;
                }

                players = new ServerPing.Players(max, online, info.toArray(new ServerPing.PlayerInfo[0]));
            } else {
                players = new ServerPing.Players(max, online, event.getResponse().getPlayers().getSample());
            }
        }

        if (config.getBoolean("motd.activated")) {
            List<String> list = config.getStringList("motd.text");

            String randomMotd = PlaceholderUtil.parseText(list.get(ThreadLocalRandom.current().nextInt(0,  list.size())));

            if (event.getConnection().getVersion() >= ProtocolConstants.MINECRAFT_1_16) {
                motd = new TextComponent(BungeeComponentSerializer.get().serialize(PistonSerializers.sectionRGB.deserialize(randomMotd)));
            } else {
                motd = new TextComponent(BungeeComponentSerializer.legacy().serialize(PistonSerializers.sectionRGB.deserialize(randomMotd)));
            }
        } else {
            motd = event.getResponse().getDescriptionComponent();
        }

        if (config.getBoolean("protocol.activated")) {
            ServerPing.Protocol provided = event.getResponse().getVersion();

            provided.setName(PlaceholderUtil.parseText(config.getString("protocol.text").replaceAll("%aftericon%", afterIcon)));

            protocol = provided;
        } else {
            protocol = event.getResponse().getVersion();
        }

        if (config.getBoolean("icons")) {
            File[] icons = iconFolder.listFiles();

            List<File> validFiles = new ArrayList<>();

            if (icons != null && icons.length != 0) {
                for (File image : icons) {
                    if (FilenameUtils.getExtension(image.getPath()).equals("png")) {
                        validFiles.add(image);
                    }
                }

                icon = Favicon.create(ImageIO.read(validFiles.get((int) Math.round(Math.random() * (validFiles.size() - 1)))));
            } else {
                icon = event.getResponse().getFaviconObject();
            }
        } else {
            icon = event.getResponse().getFaviconObject();
        }

        ServerPing ping = new ServerPing(protocol, players, motd, icon);
        event.setResponse(ping);
    }
}
