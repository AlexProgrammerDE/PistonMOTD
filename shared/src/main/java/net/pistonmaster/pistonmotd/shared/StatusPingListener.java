package net.pistonmaster.pistonmotd.shared;

import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.shared.extensions.PremiumVanishExtension;
import net.pistonmaster.pistonmotd.shared.extensions.SuperVanishExtension;
import net.pistonmaster.pistonmotd.shared.utils.ChatColor;
import net.pistonmaster.pistonmotd.shared.utils.EnumSafetyUtil;
import net.pistonmaster.pistonmotd.shared.utils.LuckPermsWrapper;
import net.pistonmaster.pistonmotd.shared.utils.MOTDUtil;

import java.util.*;
import java.util.stream.Collectors;

public interface StatusPingListener {
    String afterIcon = "                                                                            ";

    default void handle(PistonStatusPing ping) {
        PistonMOTDPlugin plugin = getPlugin();
        PistonMOTDConfig config = plugin.getPluginConfig();
        Set<UUID> vanished = new HashSet<>();

        if (config.isExtensionVanishSupervanish() && plugin.getSuperVanish().get()) {
            vanished.addAll(SuperVanishExtension.getVanishedPlayers());
        }

        if (config.isExtensionVanishPremiumvanish() && plugin.getPremiumVanish().get()) {
            vanished.addAll(PremiumVanishExtension.getVanishedPlayers());
        }

        if (config.isDescriptionActivated()) {
            ping.setDescription(MOTDUtil.getMOTD(config.getDescriptionText(), ping.supportsHex(), PlaceholderUtil::parseText));
        }

        if (config.isPlayersHide()) {
            try {
                ping.setHidePlayers(true);
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
                    ping.setOnline(ping.getOnline() - vanished.size());
                } catch (UnsupportedOperationException e) {
                    logUnsupportedConfig("extensions.vanish.hideCount");
                }
            }

            if (config.isPlayersSampleVanillaActivated()) {
                boolean luckperms = config.isExtensionPrefixLuckperms();
                LuckPermsWrapper luckpermsWrapper = plugin.getLuckPerms().get();

                if (luckperms && luckpermsWrapper == null) {
                    plugin.getPlatform().warn("Luckpemrs integration enabled, but LuckPerms is not installed!");
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

                        String displayName = ChatColor.translateAlternateColorCodes('&',
                                prefix + player.getDisplayName() + suffix + ChatColor.RESET);

                        ping.addSample(player.getUniqueId(), displayName);
                    }
                } catch (UnsupportedOperationException e) {
                    logUnsupportedConfig("players.sample.vanilla");
                }
            } else if (config.isPlayersSampleActivated()) {
                try {
                    ping.clearSamples();

                    for (String str : config.getPlayersSampleText()) {
                        ping.addSample(UUID.randomUUID(), PlaceholderUtil.parseText(str));
                    }
                } catch (UnsupportedOperationException e) {
                    logUnsupportedConfig("players.sample");
                }
            }
        }

        if (config.isVersionNameActivated()) {
            try {
                ping.setVersionName(PlaceholderUtil.parseText(config.getVersionNameText().replace("%aftericon%", afterIcon)));
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
    }

    default void logUnsupportedConfig(String value) {
        getPlugin().getPlatform().warn("\"" + value + "\" was activated in the config, but your platform does not support this feature!");
    }

    PistonMOTDPlugin getPlugin();
}
