package net.pistonmaster.pistonmotd.shared.config;

import lombok.Getter;
import net.pistonmaster.pistonmotd.shared.FaviconMode;
import net.pistonmaster.pistonmotd.shared.utils.PMHelpers;
import net.skinsrestorer.axiom.AxiomConfigurationSection;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Getter
public class PistonMOTDServerConfig {
    private boolean descriptionActivated;
    private List<String> descriptionText;
    private boolean playersHide;
    private boolean playersMaxActivated;
    private int playersMaxValue;
    private boolean playersOnlineActivated;
    private int playersOnlineValue;
    private boolean playersSampleActivated;
    private List<String> playersSampleText;
    private boolean playersSampleVanillaActivated;
    private List<String> playersSampleVanillaHidden;
    private boolean versionNameActivated;
    private String versionNameText;
    private boolean versionProtocolActivated;
    private int versionProtocolValue;
    private boolean advancedSupportedProtocolActivated;
    private List<Integer> advancedSupportedProtocolNumbers;
    private int advancedSupportedProtocolUnsupportedNumber;
    private boolean extensionVanishAPI;
    private boolean extensionVanishHideSample;
    private boolean extensionVanishHideCount;
    private boolean extensionPrefixLuckperms;
    private boolean faviconActivated;
    private FaviconMode faviconMode;
    private String faviconSingle;

    protected void load(AxiomConfigurationSection config) {
        descriptionActivated = config.getBoolean("description.activated");
        descriptionText = config.getStringList("description.text");

        playersHide = config.getBoolean("players.hide");

        playersMaxActivated = config.getBoolean("players.max.activated");
        playersMaxValue = config.getInt("players.max.value");

        playersOnlineActivated = config.getBoolean("players.online.activated");
        playersOnlineValue = config.getInt("players.online.value");

        playersSampleActivated = config.getBoolean("players.sample.activated");
        playersSampleText = config.getStringList("players.sample.text");
        playersSampleVanillaActivated = config.getBoolean("players.sample.vanilla.activated");
        playersSampleVanillaHidden = config.getStringList("players.sample.vanilla.hidden");

        versionNameActivated = config.getBoolean("version.name.activated");
        versionNameText = config.getString("version.name.text");

        versionProtocolActivated = config.getBoolean("version.protocol.activated");
        versionProtocolValue = config.getInt("version.protocol.value");

        advancedSupportedProtocolActivated = config.getBoolean("advanced.supportedProtocol.activated");
        advancedSupportedProtocolNumbers = config.getStringList("advanced.supportedProtocol.numbers")
            .stream()
            .flatMap(s -> {
              try {
                return Stream.of(Integer.parseInt(s));
              } catch (NumberFormatException e) {
                return Stream.of();
              }
            })
            .toList();
        advancedSupportedProtocolUnsupportedNumber = config.getInt("advanced.supportedProtocol.unsupportedNumber");

        extensionVanishAPI = config.getBoolean("extensions.vanish.vanishApi");
        extensionVanishHideSample = config.getBoolean("extensions.vanish.hideSample");
        extensionVanishHideCount = config.getBoolean("extensions.vanish.hideCount");
        extensionPrefixLuckperms = config.getBoolean("extensions.prefix.luckperms");

        faviconActivated = config.getBoolean("favicon.activated");
        faviconMode = PMHelpers.getSafeEnum(FaviconMode.class, config.getString("favicon.mode").toUpperCase(Locale.ROOT));
        faviconSingle = config.getString("favicon.single");
    }
}
