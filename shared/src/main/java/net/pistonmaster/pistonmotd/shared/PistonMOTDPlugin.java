package net.pistonmaster.pistonmotd.shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.shared.config.PistonMOTDPluginConfig;
import net.pistonmaster.pistonmotd.shared.utils.ConsoleColor;
import net.pistonmaster.pistonmotd.shared.utils.LuckPermsWrapper;
import net.pistonmaster.pistonutils.update.GitHubUpdateChecker;
import net.pistonmaster.pistonutils.update.SemanticVersion;
import net.skinsrestorer.axiom.AxiomConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@RequiredArgsConstructor
@SuppressWarnings("WriteOnlyObject") // Lombok's getters are ignored by this check
public class PistonMOTDPlugin {
    private final PistonMOTDPluginConfig config = new PistonMOTDPluginConfig();
    private final AtomicReference<Map<String, StatusFavicon>> favicons = new AtomicReference<>(Map.of());
    private final AtomicBoolean vanishBukkit = new AtomicBoolean();
    private final AtomicBoolean vanishBungee = new AtomicBoolean();
    private final AtomicBoolean vanishVelocity = new AtomicBoolean();
    private final AtomicReference<LuckPermsWrapper> luckPerms = new AtomicReference<>();
    private final PistonMOTDPlatform platform;

    public PistonMOTDPluginConfig getPluginConfig() {
        return config;
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

    public void loadConfig() {
        Path pluginConfigFile = platform.getPluginConfigFile();
        AxiomConfiguration defaultConfig = new AxiomConfiguration();

        try {
            Files.createDirectories(pluginConfigFile.getParent());

            try (InputStream is = platform.getBundledResource("config.yml")) {
                defaultConfig.load(is);
            }

            if (!Files.exists(pluginConfigFile)) {
                defaultConfig.save(pluginConfigFile);
            }

            AxiomConfiguration axiomConfiguration = new AxiomConfiguration();

            axiomConfiguration.load(pluginConfigFile);

            axiomConfiguration.merge(defaultConfig);

            axiomConfiguration.save(pluginConfigFile);

            config.load(axiomConfiguration);
        } catch (IOException e) {
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
                    newFavicons.put(p.getFileName().toString(), platform.createFavicon(p));
                } catch (Exception e) {
                    platform.error("Could not load favicon! (" + p.getFileName() + ")", e);
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
                platform.startup("You're up to date!");
            } else {
                platform.info(ConsoleColor.RED + "There is an update available!" + ConsoleColor.RESET);
                platform.info(ConsoleColor.RED + "Current version: " + currentVersionString + " New version: " + gitHubVersion + ConsoleColor.RESET);
                platform.info(ConsoleColor.RED + "Download it at: https://github.com/AlexProgrammerDE/PistonMOTD/releases" + ConsoleColor.RESET);
            }
        } catch (IOException e) {
            platform.error("Could not check for updates!", e);
        }
    }
}
