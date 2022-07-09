package net.pistonmaster.pistonmotd.shared;

import lombok.Getter;
import net.skinsrestorer.axiom.AxiomConfiguration;

import java.util.List;

@Getter
public class PistonMOTDConfig {
    private final AxiomConfiguration config = new AxiomConfiguration();
    private boolean extensionVanishSupervanish;
    private boolean extensionVanishPremiumvanish;
    private boolean extensionVanishHideSample;
    private boolean extensionVanishHideCount;
    private boolean extensionPrefixLuckperms;
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
    private List<String> advancedSupportedProtocolNumbers;
    private int advancedSupportedProtocolUnsupportedNumber;
    private boolean faviconActivated;
    private String faviconMode;
    private String faviconSingle;
    private boolean updateChecking;

    protected void load() {
        extensionVanishSupervanish = config.getBoolean("extensions.vanish.supervanish");
        extensionVanishPremiumvanish = config.getBoolean("extensions.vanish.premiumvanish");
        extensionVanishHideSample = config.getBoolean("extensions.vanish.hideSample");
        extensionVanishHideCount = config.getBoolean("extensions.vanish.hideCount");
        extensionPrefixLuckperms = config.getBoolean("extensions.prefix.luckperms");
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
        advancedSupportedProtocolNumbers = config.getStringList("advanced.supportedProtocol.numbers");
        advancedSupportedProtocolUnsupportedNumber = config.getInt("advanced.supportedProtocol.unsupportedNumber");
        faviconActivated = config.getBoolean("favicon.activated");
        faviconMode = config.getString("favicon.mode").toUpperCase();
        faviconSingle = config.getString("favicon.single");
        updateChecking = config.getBoolean("updateChecking");
    }
}
