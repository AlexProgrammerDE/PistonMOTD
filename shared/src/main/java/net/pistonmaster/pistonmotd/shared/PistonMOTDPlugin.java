package net.pistonmaster.pistonmotd.shared;

import de.exlll.configlib.YamlConfigurations;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.shared.config.PistonMOTDPluginConfig;
import net.pistonmaster.pistonmotd.shared.utils.ConsoleColor;
import net.pistonmaster.pistonmotd.shared.utils.LuckPermsWrapper;
import net.pistonmaster.pistonutils.update.GitHubUpdateChecker;
import net.pistonmaster.pistonutils.update.SemanticVersion;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@RequiredArgsConstructor
@SuppressWarnings("WriteOnlyObject") // Lombok's getters are ignored by this check
public class PistonMOTDPlugin {
  private final AtomicReference<PistonMOTDPluginConfig> config = new AtomicReference<>(new PistonMOTDPluginConfig());
  private final AtomicReference<Map<String, StatusFavicon>> favicons = new AtomicReference<>(Map.of());
  private final AtomicBoolean vanishBukkit = new AtomicBoolean();
  private final AtomicBoolean vanishBungee = new AtomicBoolean();
  private final AtomicBoolean vanishVelocity = new AtomicBoolean();
  private final AtomicReference<LuckPermsWrapper> luckPerms = new AtomicReference<>();
  private final PistonMOTDPlatform platform;

  public PistonMOTDPluginConfig getPluginConfig() {
    return config.get();
  }

  public void logName() {
    platform.info("  _____  _       _                 __  __   ____  _______  _____  ");
    platform.info(" |  __ \\(_)     | |               |  \\/  | / __ \\|__   __||  __ \\ ");
    platform.info(" | |__) |_  ___ | |_  ___   _ __  | \\  / || |  | |  | |   | |  | |");
    platform.info(" |  ___/| |/ __|| __|/ _ \\ | '_ \\ | |\\/| || |  | |  | |   | |  | |");
    platform.info(" | |    | |\\__ \\| |_| (_) || | | || |  | || |__| |  | |   | |__| |");
    platform.info(" |_|    |_||___/ \\__|\\___/ |_| |_||_|  |_| \\____/   |_|   |_____/ ");
    platform.info("");
  }

  public void startupLoadConfig() {
    platform.startup("Loading config");
    loadConfig();
  }

  public void startupRegisterTasks() {
    platform.startup("Registering tasks");
    platform.runAsync(this::loadFavicons, 5, 5, TimeUnit.SECONDS);
  }

  public void loadConfig() {
    Path pluginConfigFile = platform.getPluginConfigFile();

    try {
      Path parent = pluginConfigFile.getParent();
      if (parent != null) {
        Files.createDirectories(parent);
      }

      // Use ConfigLib's update method which handles:
      // - Creating the file if it doesn't exist (with default values)
      // - Loading existing values from the file
      // - Adding new fields that were added to the config class
      // - Preserving user-modified values
      PistonMOTDPluginConfig loadedConfig = YamlConfigurations.update(
        pluginConfigFile,
        PistonMOTDPluginConfig.class
      );

      config.set(loadedConfig);
    } catch (Exception e) {
      platform.error("Could not load config", e);
    }

    try {
      Path iconFolder = platform.getFaviconFolder();
      if (!Files.exists(iconFolder)) {
        Files.createDirectories(iconFolder);
      }
    } catch (IOException e) {
      platform.error("Could not create the icon directory!", e);
    }

    loadFavicons();
  }

  public void registerCommonPlaceholder() {
    platform.startup("Registering placeholders");
    PlaceholderUtil.registerParser(new CommonPlaceholder(platform));
    PlaceholderUtil.registerParser(new CenterPlaceholder.PreProcessor());
    PlaceholderUtil.registerPostParser(new CenterPlaceholder.PostProcessor());
  }

  public void loadFavicons() {
    Map<String, StatusFavicon> newFavicons = new HashMap<>();

    try (DirectoryStream<Path> ds = Files.newDirectoryStream(platform.getFaviconFolder(), new PistonMOTDPlatform.FaviconFilter())) {
      for (Path p : ds) {
        try {
          Path fileName = p.getFileName();
          if (fileName != null) {
            newFavicons.put(fileName.toString(), platform.createFavicon(p));
          }
        } catch (Exception e) {
          Path fileName = p.getFileName();
          platform.error("Could not load favicon! (%s)".formatted(fileName != null ? fileName : p), e);
        }
      }
    } catch (IOException e) {
      platform.error("Could not load favicons!", e);
    }

    this.favicons.set(newFavicons);
  }

  public void loadHooks() {
    platform.startup("Looking for hooks");
    if (platform.isSuperVanishBukkitAvailable() || platform.isPremiumVanishBukkitAvailable()) {
      platform.startup("Hooking into SuperVanish/PremiumVanish (Bukkit)");
      vanishBukkit.set(true);
    }

    if (platform.isPremiumVanishBungeeAvailable()) {
      platform.startup("Hooking into PremiumVanish (Bungee)");
      vanishBungee.set(true);
    }

    if (platform.isPremiumVanishVelocityAvailable()) {
      platform.startup("Hooking into PremiumVanish (Velocity)");
      vanishVelocity.set(true);
    }

    if (platform.isLuckPermsAvailable()) {
      platform.startup("Hooking into LuckPerms");
      luckPerms.set(new LuckPermsWrapper(this));
    }
  }

  public void checkUpdate() {
    platform.startup("Checking for a newer version");
    try {
      String currentVersionString = platform.getVersion();
      SemanticVersion gitHubVersion = new GitHubUpdateChecker()
        .getVersion("https://api.github.com/repos/AlexProgrammerDE/PistonMOTD/releases/latest");
      SemanticVersion currentVersion = SemanticVersion.fromString(currentVersionString);

      if (gitHubVersion.isNewerThan(currentVersion)) {
        platform.info(ConsoleColor.RED + "There is an update available!" + ConsoleColor.RESET);
        platform.info(ConsoleColor.RED + "Current version: " + currentVersionString + " New version: " + gitHubVersion + ConsoleColor.RESET);
        platform.info(ConsoleColor.RED + "Download it at: " + platform.getDownloadURL() + ConsoleColor.RESET);
      } else {
        platform.startup("You're up to date!");
      }
    } catch (IOException e) {
      platform.error("Could not check for updates!", e);
    }
  }
}
