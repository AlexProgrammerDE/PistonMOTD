package net.pistonmaster.pistonmotd.bukkit;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.kyori.PistonSerializersRelocated;
import net.pistonmaster.pistonmotd.shared.PistonStatusPing;
import net.pistonmaster.pistonmotd.shared.utils.MOTDUtil;
import net.pistonmaster.pistonmotd.shared.utils.PistonConstants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

@RequiredArgsConstructor
public class PingEventPaper implements Listener {
    private final PistonMOTDBukkit plugin;

    @EventHandler
    public void onPing(PaperServerListPingEvent event) {
        FileConfiguration config = plugin.getConfig();

        if (config.getBoolean("motd.activated")) {
            boolean supportsHex = event.getClient().getProtocolVersion() >= PistonConstants.MINECRAFT_1_16;
            Component motdComponent = PistonSerializersRelocated.unusualSectionRGB.deserialize(MOTDUtil.getMOTD(config.getStringList("motd.text"), supportsHex, PlaceholderUtil::parseText));

            if (supportsHex) {
                event.setMotd(PistonSerializersRelocated.unusualSectionRGB.serialize(motdComponent));
            } else {
                event.setMotd(PistonSerializersRelocated.section.serialize(motdComponent));
            }
        }

        if (config.getBoolean("extended.protocol.activated")) {
            event.setVersion(PlaceholderUtil.parseText(config.getString("extended.protocol.text")));
        }

        if (config.getBoolean("extended.overrideprotocolnumber.activated")) {
            event.setProtocolVersion(config.getInt("extended.overrideprotocolnumber.value"));
        }

        if (config.getBoolean("hooks.extended.luckpermsplayercounter") && plugin.luckpermsWrapper != null) {
            event.getPlayerSample().clear();

            int i = 0;

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                CachedMetaData metaData = plugin.luckpermsWrapper.luckperms.getPlayerAdapter(Player.class).getMetaData(player);

                String prefix = metaData.getPrefix() == null ? "" : metaData.getPrefix();

                String suffix = metaData.getSuffix() == null ? "" : metaData.getSuffix();

                event.getPlayerSample().add(i, Bukkit.createProfile(ChatColor.translateAlternateColorCodes('&', prefix + player.getDisplayName() + suffix)));
                i++;
            }
        } else if (config.getBoolean("extended.playercounter.activated")) {
            event.getPlayerSample().clear();

            for (String str : config.getStringList("extended.playercounter.text")) {
                event.getPlayerSample().add(Bukkit.createProfile(UUID.randomUUID(), PlaceholderUtil.parseText(str)));
            }
        }

        if (config.getBoolean("overridemax.activated")) {
            event.setMaxPlayers(config.getInt("overridemax.value"));
        }

        if (config.getBoolean("extended.overrideonline.activated")) {
            event.setNumPlayers(config.getInt("extended.overrideonline.value"));
        }

        if (config.getBoolean("extended.hideplayers")) {
            event.setHidePlayers(true);
        }

        if (plugin.getConfig().getBoolean("icons")) {
            event.setServerIcon(plugin.favicons.get(plugin.random.nextInt(0, plugin.favicons.size())));
        }
    }

    private PistonStatusPing wrap(PaperServerListPingEvent event) {
        return new PistonStatusPing() {
            @Override
            public void setDescription(String description) {
                event.setMotd(description);
            }

            @Override
            public void setMax(int max) {
                event.setMaxPlayers(max);
            }

            @Override
            public void setOnline(int online) {
                event.setNumPlayers(online);
            }

            @Override
            public void setVersionName(String name) {
                event.setVersion(name);
            }

            @Override
            public void setVersionProtocol(int protocol) {
                event.setProtocolVersion(protocol);
            }

            @Override
            public void setHidePlayers(boolean hidePlayers) throws UnsupportedOperationException {
                event.setHidePlayers(hidePlayers);
            }

            @Override
            public String getDescription() {
                return event.getMotd();
            }

            @Override
            public int getMax() {
                return event.getMaxPlayers();
            }

            @Override
            public int getOnline() {
                return event.getNumPlayers();
            }

            @Override
            public String getVersionName() {
                return event.getVersion();
            }

            @Override
            public int getVersionProtocol() {
                return event.getProtocolVersion();
            }

            @Override
            public boolean getHidePlayers() throws UnsupportedOperationException {
                return event.shouldHidePlayers();
            }
        };
    }
}
