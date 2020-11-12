package me.alexprogrammerde.pistonmotd.bukkit;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import me.alexprogrammerde.pistonmotd.api.PlaceholderUtil;
import me.alexprogrammerde.pistonmotd.utils.PistonConstants;
import me.alexprogrammerde.pistonmotd.utils.PistonSerializers;
import net.kyori.adventure.text.Component;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PingEventPaper implements Listener {
    private final PistonMOTDBukkit plugin;

    protected PingEventPaper(PistonMOTDBukkit plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPing(PaperServerListPingEvent event) {
        FileConfiguration config = plugin.getConfig();

        if (config.getBoolean("motd.activated")) {
            List<String> motd = config.getStringList("motd.text");

            Component motdComponent = PistonSerializers.unusualSectionRGB.deserialize(PlaceholderUtil.parseText(motd.get(ThreadLocalRandom.current().nextInt(0,  motd.size()))));

            if (event.getClient().getProtocolVersion() >= PistonConstants.MINECRAFT_1_16) {
                event.setMotd(PistonSerializers.unusualSectionRGB.serialize(motdComponent));
            } else {
                event.setMotd(PistonSerializers.section.serialize(motdComponent));
            }
        }

        if (config.getBoolean("extended.protocol.activated")) {
            event.setVersion(PlaceholderUtil.parseText(config.getString("extended.protocol.activated")));
        }

        if (config.getBoolean("hooks.luckpermsplayercounter") && plugin.luckperms != null) {
            event.getPlayerSample().clear();

            int i = 0;

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                // Get a LuckPerms cached metadata for the player.
                CachedMetaData metaData = plugin.luckperms.getPlayerAdapter(Player.class).getMetaData(player);

                // Get their prefix.
                String prefix = metaData.getPrefix() == null ? "" : metaData.getPrefix();

                event.getPlayerSample().add(i, Bukkit.createProfile(ChatColor.translateAlternateColorCodes('&', prefix + ChatColor.RESET + player.getDisplayName())));
                i++;
            }
        } else if (config.getBoolean("extended.playercounter.activated")) {
            event.getPlayerSample().clear();

            int i = 0;

            for (String str : config.getStringList("extended.playercounter.text")) {
                event.getPlayerSample().add(i, Bukkit.createProfile(PlaceholderUtil.parseText(str)));
                i++;
            }
        }

        if (config.getBoolean("overridemax.activated")) {
            event.setMaxPlayers(config.getInt("overridemax.value"));
        }

        if (config.getBoolean("extended.overrideonline.activated")) {
            event.setNumPlayers(config.getInt("extended.overrideonline.value"));
        }

        if (config.getBoolean("extended.hideplayers")) {
            event.setHidePlayers(config.getBoolean("extended.hideplayers.value"));
        }

        if (plugin.getConfig().getBoolean("icons")) {
            File[] icons = plugin.icons.listFiles();

            List<File> validFiles = new ArrayList<>();

            if (icons != null && icons.length != 0) {
                for (File image : icons) {
                    if (FilenameUtils.getExtension(image.getPath()).equals("png")) {
                        validFiles.add(image);
                    }
                }

                try {
                    event.setServerIcon(plugin.getServer().loadServerIcon(validFiles.get((int) Math.round(Math.random() * (validFiles.size() - 1)))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
