package net.pistonmaster.pistonmotd.shared;

import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.kyori.PistonSerializersRelocated;
import net.pistonmaster.pistonmotd.shadow.kyori.adventure.text.Component;
import net.pistonmaster.pistonmotd.shared.extensions.PremiumVanishExtension;
import net.pistonmaster.pistonmotd.shared.extensions.SuperVanishExtension;
import net.pistonmaster.pistonmotd.shared.utils.EnumSafetyUtil;
import net.pistonmaster.pistonmotd.shared.utils.LuckPermsWrapper;
import net.pistonmaster.pistonmotd.shared.utils.MOTDUtil;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.stream.Collectors;

public interface StatusPingListener {
    String afterIcon = "                                                                            ";

    default void handle(PistonStatusPing ping) {
        PistonMOTDPlugin plugin = getPlugin();
        PistonMOTDServerConfig config = plugin.getPluginConfig();
        Set<UUID> vanished = new HashSet<>();

        if (config.isExtensionVanishSupervanish() && plugin.getSuperVanish().get()) {
            vanished.addAll(SuperVanishExtension.getVanishedPlayers());
        }

        if (config.isExtensionVanishPremiumvanish() && plugin.getPremiumVanish().get()) {
            vanished.addAll(PremiumVanishExtension.getVanishedPlayers());
        }

        if (config.isDescriptionActivated()) {
            ping.setDescription(MOTDUtil.getMOTDJson(config.getDescriptionText(), ping.supportsHex()));
        }

        if (config.isPlayersHide()) {
            try {
                ping.hidePlayers();
            } catch (UnsupportedOperationException e) {
                logUnsupportedConfig("players.hide");
            }
        } else {
            if (config.isPlayersMaxActivated()) {
                ping.setMax(config.getPlayersMaxValue());
            }

            if (config.isPlayersOnlineActivated()) {
                try {
                    ping.setOnline(config.getPlayersOnlineValue());
                } catch (UnsupportedOperationException e) {
                    logUnsupportedConfig("players.online");
                }
            }

            if (config.isExtensionVanishHideCount()) {
                try {
                    ping.setOnline(Math.max(ping.getOnline() - vanished.size(), 0));
                } catch (UnsupportedOperationException e) {
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

                        Component prefix = Component.empty();
                        Component suffix = Component.empty();
                        if (luckperms) {
                            LuckPermsWrapper.LuckPermsMeta meta = luckpermsWrapper.getWrappedMeta(player);

                            if (meta.getPrefix() != null)
                                prefix = PistonSerializersRelocated.ampersandRGB.deserialize(meta.getPrefix());

                            if (meta.getSuffix() != null)
                                suffix = PistonSerializersRelocated.ampersandRGB.deserialize(meta.getSuffix());
                        }

                        String displayName = PistonSerializersRelocated.section.serialize(prefix
                                .append(PistonSerializersRelocated.section.deserialize(player.getDisplayName())
                                        .append(suffix)))
                                // Reset character to prevent color bleeding
                                + "§r";

                        ping.addSample(player.getUniqueId(), displayName);
                    }
                } catch (UnsupportedOperationException e) {
                    logUnsupportedConfig("players.sample.vanilla");
                }
            } else if (config.isPlayersSampleActivated()) {
                try {
                    ping.clearSamples();

                    for (String str : config.getPlayersSampleText()) {
                        ping.addSample(UUID.randomUUID(), PlaceholderUtil.parseTextToLegacy(str));
                    }
                } catch (UnsupportedOperationException e) {
                    logUnsupportedConfig("players.sample");
                }
            }
        }

        if (config.isVersionNameActivated()) {
            try {
                ping.setVersionName(PlaceholderUtil.parseTextToLegacy(config.getVersionNameText().replace("%aftericon%", afterIcon)));
            } catch (UnsupportedOperationException e) {
                logUnsupportedConfig("version.name");
            }
        }

        if (config.isVersionProtocolActivated()) {
            try {
                ping.setVersionProtocol(config.getVersionProtocolValue());
            } catch (UnsupportedOperationException e) {
                logUnsupportedConfig("version.protocol");
            }
        }

        if (config.isAdvancedSupportedProtocolActivated()) {
            try {
                List<String> supportedProtocols = config.getAdvancedSupportedProtocolNumbers();

                if (!supportedProtocols.isEmpty()) {
                    List<Integer> protocols = supportedProtocols.stream().map(Integer::parseInt).collect(Collectors.toList());

                    if (protocols.contains(ping.getClientProtocol())) {
                        ping.setVersionProtocol(ping.getClientProtocol());
                    } else {
                        ping.setVersionProtocol(config.getAdvancedSupportedProtocolUnsupportedNumber());
                    }
                }
            } catch (UnsupportedOperationException e) {
                logUnsupportedConfig("advanced.supportedProtocol");
            }
        }

        if (config.isFaviconActivated()) {
            String modeString = config.getFaviconMode();
            FaviconMode mode = EnumSafetyUtil.getSafeEnum(FaviconMode.class, modeString);

            if (mode == FaviconMode.RANDOM) {
                if (plugin.getFavicons().isEmpty()) {
                    plugin.getPlatform().warn("No valid favicons found in your favicons folder, but the favicons setting is enabled...");
                } else {
                    ping.setFavicon(new ArrayList<>(plugin.getFavicons().values())
                            .get(plugin.getRandom().nextInt(0, plugin.getFavicons().size())));
                }
            } else if (mode == FaviconMode.SINGLE) {
                String faviconName = config.getFaviconSingle();
                StatusFavicon favicon = plugin.getFavicons().get(faviconName);

                if (favicon == null) {
                    plugin.getPlatform().warn("The favicon '" + faviconName + "' does not exist.");
                } else {
                    ping.setFavicon(favicon);
                }
            } else {
                plugin.getPlatform().warn("Invalid favicon mode: " + modeString);
            }
        }

        if (config.isAdvancedPerDomainStatusActivated()) {
            try {
                Optional<InetSocketAddress> virtualHost = ping.getClientVirtualHost();

                if (virtualHost.isPresent()) {
                    try {
                        for (PistonMOTDServerConfig.PerDomainStatusDomain domainData : config.getAdvancedPerDomainStatusDomains().values()) {
                            if (virtualHost.get().getHostString().endsWith(domainData.getDomain())) {
                                if (domainData.isDescriptionActivated()) {
                                    ping.setDescription(MOTDUtil.getMOTDJson(
                                            domainData.getDescriptionText(),
                                            ping.supportsHex()));
                                }

                                if (domainData.isFaviconActivated()) {
                                    String faviconName = domainData.getFaviconFile();
                                    StatusFavicon favicon = plugin.getFavicons().get(faviconName);

                                    if (favicon == null) {
                                        getPlugin().getPlatform().warn("The favicon '" + faviconName + "' does not exist.");
                                    } else {
                                        ping.setFavicon(favicon);
                                    }
                                }

                                break;
                            }
                        }
                    } catch (ClassCastException | NullPointerException e) {
                        getPlugin().getPlatform().warn("The 'advanced.perDomainStatus.domains' has invalid structure.", e);
                    }
                }
            } catch (UnsupportedOperationException e) {
                logUnsupportedConfig("advanced.supportedProtocol");
            }
        }
    }

    default void logUnsupportedConfig(String value) {
        getPlugin().getPlatform().warn("\"" + value + "\" was activated in the config, but your platform does not support this feature!");
    }

    PistonMOTDPlugin getPlugin();
}
