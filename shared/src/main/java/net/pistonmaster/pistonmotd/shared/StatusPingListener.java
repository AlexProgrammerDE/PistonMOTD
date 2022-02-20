package net.pistonmaster.pistonmotd.shared;

import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.shared.utils.EnumSafetyUtil;
import net.pistonmaster.pistonmotd.shared.utils.MOTDUtil;
import net.skinsrestorer.axiom.AxiomConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        if (config.getBoolean("advanced.supportedProtocol.activated")) {
            try {
                List<String> supportedProtocols = config.getStringList("advanced.supportedProtocol.numbers");

                if (!supportedProtocols.isEmpty()) {
                    List<Integer> protocols = supportedProtocols.stream().map(Integer::parseInt).collect(Collectors.toList());

                    if (protocols.contains(ping.getClientProtocol())) {
                        ping.setVersionProtocol(ping.getClientProtocol());
                    } else {
                        ping.setVersionProtocol(config.getInt("advanced.supportedProtocol.unsupportedNumber"));
                    }
                }
            } catch (UnsupportedOperationException e) {
                logUnsupportedConfig("advanced.supportedProtocol");
            }
        }

        if (config.getBoolean("favicon.activated")) {
            String modeString = config.getString("favicon.mode").toUpperCase();
            FaviconMode mode = EnumSafetyUtil.getSafeEnum(FaviconMode.class, modeString);

            if (mode == FaviconMode.RANDOM) {
                if (plugin.favicons.isEmpty()) {
                    plugin.warn("No valid favicons found in your favicons folder, but the favicons setting is enabled...");
                } else {
                    ping.setFavicon(new ArrayList<>(plugin.favicons.values())
                            .get(plugin.random.nextInt(0, plugin.favicons.size())));
                }
            } else if (mode == FaviconMode.SINGLE) {
                String faviconName = config.getString("favicon.single");
                StatusFavicon favicon = plugin.favicons.get(faviconName);

                if (favicon == null) {
                    plugin.warn("The favicon '" + faviconName + "' does not exist.");
                } else {
                    ping.setFavicon(favicon);
                }
            } else {
                plugin.warn("Invalid favicon mode: " + modeString);
            }
        }
    }

    default void logUnsupportedConfig(String value) {
        getPlugin().warn("\"" + value + "\" was activated in the config, but your platform does not support this feature!");
    }

    PistonMOTDPlugin getPlugin();
}
