package net.pistonmaster.pistonmotd.shared.config;

import lombok.Getter;
import net.pistonmaster.pistonmotd.shared.FaviconMode;
import net.pistonmaster.pistonmotd.shared.utils.PMHelpers;
import net.skinsrestorer.axiom.AxiomConfigurationSection;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
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
    descriptionActivated = Objects.requireNonNullElse(config.getBoolean("description.activated"), false);
    descriptionText = Objects.requireNonNullElse(config.getStringList("description.text"), List.of());

    playersHide = Objects.requireNonNullElse(config.getBoolean("players.hide"), false);

    playersMaxActivated = Objects.requireNonNullElse(config.getBoolean("players.max.activated"), false);
    playersMaxValue = Objects.requireNonNullElse(config.getInt("players.max.value"), 0);

    playersOnlineActivated = Objects.requireNonNullElse(config.getBoolean("players.online.activated"), false);
    playersOnlineValue = Objects.requireNonNullElse(config.getInt("players.online.value"), 0);

    playersSampleActivated = Objects.requireNonNullElse(config.getBoolean("players.sample.activated"), false);
    playersSampleText = Objects.requireNonNullElse(config.getStringList("players.sample.text"), List.of());
    playersSampleVanillaActivated = Objects.requireNonNullElse(config.getBoolean("players.sample.vanilla.activated"), false);
    playersSampleVanillaHidden = Objects.requireNonNullElse(config.getStringList("players.sample.vanilla.hidden"), List.of());

    versionNameActivated = Objects.requireNonNullElse(config.getBoolean("version.name.activated"), false);
    versionNameText = Objects.requireNonNullElse(config.getString("version.name.text"), "");

    versionProtocolActivated = Objects.requireNonNullElse(config.getBoolean("version.protocol.activated"), false);
    versionProtocolValue = Objects.requireNonNullElse(config.getInt("version.protocol.value"), 0);

    advancedSupportedProtocolActivated = Objects.requireNonNullElse(config.getBoolean("advanced.supportedProtocol.activated"), false);
    advancedSupportedProtocolNumbers = Objects.requireNonNullElse(config.getStringList("advanced.supportedProtocol.numbers"), List.<String>of())
        .stream()
        .flatMap(s -> {
          try {
            return Stream.of(Integer.parseInt(s));
          } catch (NumberFormatException e) {
            return Stream.of();
          }
        })
        .toList();
    advancedSupportedProtocolUnsupportedNumber = Objects.requireNonNullElse(config.getInt("advanced.supportedProtocol.unsupportedNumber"), -1);

    extensionVanishAPI = Objects.requireNonNullElse(config.getBoolean("extensions.vanish.vanishApi"), false);
    extensionVanishHideSample = Objects.requireNonNullElse(config.getBoolean("extensions.vanish.hideSample"), false);
    extensionVanishHideCount = Objects.requireNonNullElse(config.getBoolean("extensions.vanish.hideCount"), false);
    extensionPrefixLuckperms = Objects.requireNonNullElse(config.getBoolean("extensions.prefix.luckperms"), false);

    faviconActivated = Objects.requireNonNullElse(config.getBoolean("favicon.activated"), false);
    faviconMode = PMHelpers.getSafeEnum(FaviconMode.class, Objects.requireNonNullElse(config.getString("favicon.mode"), FaviconMode.SINGLE.name()).toUpperCase(Locale.ROOT));
    faviconSingle = Objects.requireNonNullElse(config.getString("favicon.single"), "");
  }
}
