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

        if (config.getBoolean("hideplayers")) {
            try {
                ping.setHidePlayers(true);
            } catch (UnsupportedOperationException e) {
                logUnsupportedConfig("protocol");
            }
        } else {
            if (config.getBoolean("protocol.activated")) {
                try {
                    ping.setVersionName(PlaceholderUtil.parseText(config.getString("extended.protocol.text").replace("%aftericon%", afterIcon)));
                } catch (UnsupportedOperationException e) {
                    logUnsupportedConfig("protocol");
                }
            }

            if (config.getBoolean("overrideprotocolnumber.activated")) {
                try {
                    ping.setVersionProtocol(config.getInt("extended.overrideprotocolnumber.value"));
                } catch (UnsupportedOperationException e) {
                    logUnsupportedConfig("overrideprotocolnumber");
                }
            }
        }

        if (config.getBoolean("playercounter.bukkitplayercounter")) {
            try {
                ping.clearSamples();

                for (PlayerWrapper player : plugin.getPlayers()) {
                    if (config.getStringList("hiddenplayers").contains(player.getName())) continue;

                    ping.addSample(player.getUniqueId(), player.getDisplayName());
                }
            } catch (UnsupportedOperationException e) {
                logUnsupportedConfig("playercounter.bukkitplayercounter");
            }
        } else if (config.getBoolean("playercounter.activated")) {
            try {
                ping.clearSamples();

                for (String str : config.getStringList("extended.playercounter.text")) {
                    ping.addSample(UUID.randomUUID(), PlaceholderUtil.parseText(str));
                }
            } catch (UnsupportedOperationException e) {
                logUnsupportedConfig("playercounter");
            }
        }

        if (config.getBoolean("overridemax.activated")) {
            ping.setMax(config.getInt("overridemax.value"));
        }

        if (config.getBoolean("overrideonline.activated")) {
            try {
                ping.setOnline(config.getInt("extended.overrideonline.value"));
            } catch (UnsupportedOperationException e) {
                logUnsupportedConfig("overrideonline");
            }
        }

        if (config.getBoolean("icons")) {
            if (plugin.favicons.isEmpty()) {
                plugin.warn("No valid favicons found in your icons folder, but the icons setting is enabled...");
            } else {
                ping.setFavicon(plugin.favicons.get(plugin.random.nextInt(0, plugin.favicons.size())));
            }
        }
    }

    default void logUnsupportedConfig(String value) {
        getPlugin().warn("\"" + value + "\" was activated in the config, but your platform does not support this feature!");
    }

    PistonMOTDPlugin getPlugin();
}
