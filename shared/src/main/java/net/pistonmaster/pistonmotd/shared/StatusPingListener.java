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

        if (config.getBoolean("description.activated")) {
            ping.setDescription(MOTDUtil.getMOTD(config.getStringList("description.text"), ping.supportsHex(), PlaceholderUtil::parseText));
        }

        if (config.getBoolean("players.hide")) {
            try {
                ping.setHidePlayers(true);
            } catch (UnsupportedOperationException e) {
                logUnsupportedConfig("players.hide");
            }
        } else {
            if (config.getBoolean("players.max.activated")) {
                ping.setMax(config.getInt("players.max.value"));
            }

            if (config.getBoolean("players.online.activated")) {
                try {
                    ping.setOnline(config.getInt("players.online.value"));
                } catch (UnsupportedOperationException e) {
                    logUnsupportedConfig("players.online");
                }
            }

            if (config.getBoolean("players.sample.bukkit.activated")) {
                try {
                    ping.clearSamples();

                    for (PlayerWrapper player : plugin.getPlayers()) {
                        if (config.getStringList("players.sample.bukkit.hidden").contains(player.getName())) continue;

                        ping.addSample(player.getUniqueId(), player.getDisplayName());
                    }
                } catch (UnsupportedOperationException e) {
                    logUnsupportedConfig("players.sample.bukkit");
                }
            } else if (config.getBoolean("players.sample.activated")) {
                try {
                    ping.clearSamples();

                    for (String str : config.getStringList("players.sample.text")) {
                        ping.addSample(UUID.randomUUID(), PlaceholderUtil.parseText(str));
                    }
                } catch (UnsupportedOperationException e) {
                    logUnsupportedConfig("players.sample");
                }
            }
        }

        if (config.getBoolean("version.name.activated")) {
            try {
                ping.setVersionName(PlaceholderUtil.parseText(config.getString("version.name.text").replace("%aftericon%", afterIcon)));
            } catch (UnsupportedOperationException e) {
                logUnsupportedConfig("version.name");
            }
        }

        if (config.getBoolean("version.protocol.activated")) {
            try {
                ping.setVersionProtocol(config.getInt("version.protocol.value"));
            } catch (UnsupportedOperationException e) {
                logUnsupportedConfig("version.protocol");
            }
        }

        if (config.getBoolean("favicons")) {
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
