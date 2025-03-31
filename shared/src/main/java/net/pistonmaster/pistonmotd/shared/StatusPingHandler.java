package net.pistonmaster.pistonmotd.shared;

import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.kyori.PistonSerializersRelocated;
import net.pistonmaster.pistonmotd.shared.config.PistonMOTDDomainConfig;
import net.pistonmaster.pistonmotd.shared.config.PistonMOTDPluginConfig;
import net.pistonmaster.pistonmotd.shared.config.PistonMOTDServerConfig;
import net.pistonmaster.pistonmotd.shared.extensions.VanishAPIExtension;
import net.pistonmaster.pistonmotd.shared.utils.LuckPermsWrapper;
import net.pistonmaster.pistonmotd.shared.utils.PMHelpers;
import net.pistonmaster.pistonmotd.shared.utils.PMUnsupportedConfigException;

import java.net.InetSocketAddress;
import java.util.*;

public record StatusPingHandler(PistonMOTDPlugin plugin) {
    public void handle(PistonStatusPing ping) {
        PistonMOTDPluginConfig config = plugin.getPluginConfig();

        applyServerConfig(ping, config);

        if (config.isAdvancedPerDomainStatusActivated()) {
            try {
                Optional<InetSocketAddress> virtualHost = ping.getClientVirtualHost();

                if (virtualHost.isPresent()) {
                    try {
                        for (PistonMOTDDomainConfig domainData : config.getAdvancedPerDomainStatusDomains().values()) {
                            if (virtualHost.get().getHostString().endsWith(domainData.getDomain())) {
                                applyServerConfig(ping, domainData);
                                break;
                            }
                        }
                    } catch (ClassCastException | NullPointerException e) {
                        plugin.getPlatform().warn("The 'advanced.perDomainStatus.domains' has invalid structure.", e);
                    }
                }
            } catch (PMUnsupportedConfigException e) {
                logUnsupportedConfig("advanced.supportedProtocol");
            }
        }
    }

    private void logUnsupportedConfig(String value) {
        plugin.getPlatform().warn("\"" + value + "\" was activated in the config, but your platform does not support this feature!");
    }

    private void applyServerConfig(PistonStatusPing ping, PistonMOTDServerConfig config) {
        Set<UUID> vanished = new HashSet<>();

        if (config.isExtensionVanishAPI()) {
            if (plugin.getVanishBukkit().get()) {
                vanished.addAll(VanishAPIExtension.getVanishedPlayersBukkit());
            } else if (plugin.getVanishBungee().get()) {
                vanished.addAll(VanishAPIExtension.getVanishedPlayersBungee());
            } else if (plugin.getVanishVelocity().get()) {
                vanished.addAll(VanishAPIExtension.getVanishedPlayersVelocity());
            }
        }

        // Any player count modifications first
        if (config.isPlayersHide()) {
            try {
                ping.hidePlayers();
            } catch (PMUnsupportedConfigException e) {
                logUnsupportedConfig("players.hide");
            }
        } else {
            if (config.isPlayersMaxActivated()) {
                ping.setMax(config.getPlayersMaxValue());
            }

            if (config.isPlayersOnlineActivated()) {
                try {
                    ping.setOnline(config.getPlayersOnlineValue());
                } catch (PMUnsupportedConfigException e) {
                    logUnsupportedConfig("players.online");
                }
            }

            if (config.isExtensionVanishHideCount()) {
                try {
                    ping.setOnline(Math.max(ping.getOnline() - vanished.size(), 0));
                } catch (PMUnsupportedConfigException e) {
                    logUnsupportedConfig("extensions.vanish.hideCount");
                }
            }

            if (config.isPlayersSampleVanillaActivated()) {
                boolean luckperms = config.isExtensionPrefixLuckperms();
                LuckPermsWrapper luckpermsWrapper = plugin.getLuckPerms().get();

                if (luckperms && luckpermsWrapper == null) {
                    plugin.getPlatform().warn("LuckPerms integration enabled, but LuckPerms is not installed!");
                    luckperms = false;
                }

                try {
                    ping.clearSamples();
                    List<String> hiddenNames = config.getPlayersSampleVanillaHidden();
                    boolean hideSample = config.isExtensionVanishHideSample();

                    for (PlayerWrapper player : plugin.getPlatform().getPlayers()) {
                        if (hiddenNames.contains(player.getName()))
                            continue;

                        if (hideSample && vanished.contains(player.getUniqueId()))
                            continue;

                        String prefix = "";
                        String suffix = "";
                        if (luckperms) {
                            LuckPermsWrapper.LuckPermsMeta meta = luckpermsWrapper.getWrappedMeta(player);

                            if (meta.getPrefix() != null)
                                prefix = meta.getPrefix();

                            if (meta.getSuffix() != null)
                                suffix = meta.getSuffix();
                        }

                        String displayName = PistonSerializersRelocated.section.serialize(
                            PistonSerializersRelocated.ampersandRGB.deserialize("%s%s%s"
                                .formatted(
                                    prefix,
                                    PistonSerializersRelocated.ampersandRGB.serialize(
                                        PistonSerializersRelocated.sectionRGB.deserialize(player.getDisplayName())),
                                    suffix
                                )
                            ))
                            // Reset character to prevent color bleeding
                            + "§r";

                        ping.addSample(player.getUniqueId(), displayName);
                    }
                } catch (PMUnsupportedConfigException e) {
                    logUnsupportedConfig("players.sample.vanilla");
                }
            } else if (config.isPlayersSampleActivated()) {
                try {
                    ping.clearSamples();

                    for (String str : config.getPlayersSampleText()) {
                        ping.addSample(
                            UUID.randomUUID(),
                            parseWithPing(ping, PlaceholderUtil.parseTextToLegacy(str))
                        );
                    }
                } catch (PMUnsupportedConfigException e) {
                    logUnsupportedConfig("players.sample");
                }
            }
        }

        if (config.isDescriptionActivated()) {
            ping.setDescription(PMHelpers.getMOTDJson(config.getDescriptionText()
                .stream()
                .map(str -> parseWithPing(ping, str))
                .toList(),
                ping.supportsHex()
            ));
        }

        if (config.isVersionNameActivated()) {
            try {
                ping.setVersionName(PlaceholderUtil.parseTextToLegacy(parseWithPing(ping, config.getVersionNameText()
                    .replace("%aftericon%", PMHelpers.AFTER_ICON)
                    .replace("<after_icon>", PMHelpers.AFTER_ICON)
                )));
            } catch (PMUnsupportedConfigException e) {
                logUnsupportedConfig("version.name");
            }
        }

        if (config.isVersionProtocolActivated()) {
            try {
                ping.setVersionProtocol(config.getVersionProtocolValue());
            } catch (PMUnsupportedConfigException e) {
                logUnsupportedConfig("version.protocol");
            }
        }

        if (config.isAdvancedSupportedProtocolActivated()) {
            try {
                List<Integer> protocols = config.getAdvancedSupportedProtocolNumbers();
                if (!protocols.isEmpty()) {
                    if (protocols.contains(ping.getClientProtocol())) {
                        ping.setVersionProtocol(ping.getClientProtocol());
                    } else {
                        ping.setVersionProtocol(config.getAdvancedSupportedProtocolUnsupportedNumber());
                    }
                }
            } catch (PMUnsupportedConfigException e) {
                logUnsupportedConfig("advanced.supportedProtocol");
            }
        }

        if (config.isFaviconActivated()) {
            FaviconMode mode = config.getFaviconMode();
            Map<String, StatusFavicon> favicons = plugin.getFavicons().get();

            if (mode == FaviconMode.RANDOM) {
                if (favicons.isEmpty()) {
                    plugin.getPlatform().warn("No valid favicons found in your favicons folder, but the favicons setting is enabled...");
                } else {
                    ping.setFavicon(PMHelpers.getRandomEntry(favicons.values()));
                }
            } else if (mode == FaviconMode.SINGLE) {
                String faviconName = config.getFaviconSingle();
                StatusFavicon favicon = favicons.get(faviconName);

                if (favicon == null) {
                    plugin.getPlatform().warn("The favicon '" + faviconName + "' does not exist.");
                } else {
                    ping.setFavicon(favicon);
                }
            } else {
                plugin.getPlatform().warn("Invalid favicon mode: " + mode);
            }
        }
    }

    private static String parseWithPing(PistonStatusPing ping, String str) {
        return str
            .replace("<passthrough_online>", String.valueOf(getPingOnline(ping)))
            .replace("<passthrough_max>", String.valueOf(ping.getMax()));
    }

    private static int getPingOnline(PistonStatusPing ping) {
        try {
            return ping.getOnline();
        } catch (PMUnsupportedConfigException e) {
            return 0;
        }
    }
}
