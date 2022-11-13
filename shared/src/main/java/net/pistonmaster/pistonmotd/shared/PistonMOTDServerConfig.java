package net.pistonmaster.pistonmotd.shared;

import lombok.Getter;
import net.skinsrestorer.axiom.AxiomConfiguration;
import net.skinsrestorer.axiom.AxiomConfigurationSection;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class PistonMOTDServerConfig {
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
    private boolean advancedPerDomainStatusActivated;
    private Map<String, PerDomainStatusDomain> advancedPerDomainStatusDomains;
    private boolean faviconActivated;
    private String faviconMode;
    private String faviconSingle;

    protected void load(AxiomConfiguration config) {
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
        advancedPerDomainStatusActivated = config.getBoolean("advanced.perDomainStatus.activated");

        AxiomConfigurationSection perDomainStatusSection = config.getSection("advanced.perDomainStatus.domains");
        List<String> domains = perDomainStatusSection.getKeys();
        Map<String, PerDomainStatusDomain> domainMap = new LinkedHashMap<>();
        for (String domainId : domains) {
            AxiomConfigurationSection domainSection = perDomainStatusSection.getSection(domainId);

            PerDomainStatusDomain domain = new PerDomainStatusDomain();
            domain.load(domainSection);

            domainMap.put(domainId, domain);
        }
        advancedPerDomainStatusDomains = domainMap;

        faviconActivated = config.getBoolean("favicon.activated");
        faviconMode = config.getString("favicon.mode").toUpperCase();
        faviconSingle = config.getString("favicon.single");
    }

    @Getter
    public static class PerDomainStatusDomain {
        private boolean descriptionActivated;
        private List<String> descriptionText;
        private boolean faviconActivated;
        private String faviconFile;
        private String domain;

        protected void load(AxiomConfigurationSection domainSection) {
            descriptionActivated = domainSection.getBoolean("description.activated");
            descriptionText = domainSection.getStringList("description.text");
            faviconActivated = domainSection.getBoolean("favicon.activated");
            faviconFile = domainSection.getString("favicon.file");
            domain = domainSection.getString("domain");
        }
    }
}
