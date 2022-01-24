package net.pistonmaster.pistonmotd.shared;

import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.shared.utils.MOTDUtil;
import net.skinsrestorer.axiom.AxiomConfiguration;

import java.util.UUID;

public interface StatusPingListener {
    String afterIcon = "                                                                            ";

    default void handle(PistonStatusPing ping) {
        PistonMOTDPlugin plugin = getPlugin();
        AxiomConfiguration config = plugin.getPluginConfig();

        if (config.getBoolean("motd.activated")) {
            ping.setDescription(MOTDUtil.getMOTD(config.getStringList("motd.text"), ping.supportsHex(), PlaceholderUtil::parseText));
        }

        if (config.getBoolean("extended.hideplayers")) {
            ping.setHidePlayers(true);
        } else {
            if (config.getBoolean("extended.protocol.activated")) {
                ping.setVersionName(PlaceholderUtil.parseText(config.getString("extended.protocol.text").replace("%aftericon%", afterIcon)));
            }

            if (config.getBoolean("extended.overrideprotocolnumber.activated")) {
                ping.setVersionProtocol(config.getInt("extended.overrideprotocolnumber.value"));
            }
        }

        if (config.getBoolean("playercounter.bukkitplayercounter")) {
            ping.clearSamples();

            for (PlayerWrapper player : plugin.getPlayers()) {
                if (config.getStringList("hiddenplayers").contains(player.getName())) continue;

                ping.addSample(player.getUniqueId(), player.getDisplayName());
            }
        } else if (config.getBoolean("extended.playercounter.activated")) {
            ping.clearSamples();

            for (String str : config.getStringList("extended.playercounter.text")) {
                ping.addSample(UUID.randomUUID(), PlaceholderUtil.parseText(str));
            }
        }

        if (config.getBoolean("overridemax.activated")) {
            ping.setMax(config.getInt("overridemax.value"));
        }

        if (config.getBoolean("extended.overrideonline.activated")) {
            ping.setOnline(config.getInt("extended.overrideonline.value"));
        }

        if (config.getBoolean("icons")) {
            if (plugin.favicons.isEmpty()) {
                plugin.warn("No valid favicons found in your icons folder, but the icons setting is enabled...");
            } else {
                ping.setFavicon(plugin.favicons.get(plugin.random.nextInt(0, plugin.favicons.size())));
            }
        }
    }

    PistonMOTDPlugin getPlugin();
}
