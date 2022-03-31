package net.pistonmaster.pistonmotd.shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.shared.utils.ConsoleColor;
import net.pistonmaster.pistonmotd.shared.utils.LuckPermsWrapper;
import net.pistonmaster.pistonutils.logging.PistonLogger;
import net.pistonmaster.pistonutils.update.UpdateChecker;
import net.pistonmaster.pistonutils.update.UpdateParser;
import net.pistonmaster.pistonutils.update.UpdateType;
import net.skinsrestorer.axiom.AxiomConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@RequiredArgsConstructor
@SuppressWarnings("WriteOnlyObject") // Lombok's getters are ignored by this check
public class PistonMOTDPlugin {
    private final AxiomConfiguration config = new AxiomConfiguration();
    private final Map<String, StatusFavicon> favicons = new LinkedHashMap<>();
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private final AtomicBoolean premiumVanish = new AtomicBoolean();
    private final AtomicBoolean superVanish = new AtomicBoolean();
    private final AtomicReference<LuckPermsWrapper> luckPerms = new AtomicReference<>();
    private final PistonMOTDPlatform platform;

    public AxiomConfiguration getPluginConfig() {
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

            try (InputStream is = platform.getDefaultConfig()) {
                defaultConfig.load(is);
            }

            if (!Files.exists(pluginConfigFile)) {
                defaultConfig.save(pluginConfigFile);
            }

            config.load(pluginConfigFile);

            config.merge(defaultConfig);

            config.save(pluginConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
            platform.error("Could not load config");
        }

        try {
            Path iconFolder = platform.getFaviconFolder();
            if (!Files.exists(iconFolder)) {
                Files.createDirectories(iconFolder);
            }
        } catch (IOException e) {
            e.printStackTrace();
            platform.error("Could not create the icon directory!");
        }

        loadFavicons();
    }

    public void registerCommonPlaceholder() {
        platform.startup("Registering placeholders");
        PlaceholderUtil.registerParser(new CommonPlaceholder(platform));
    }

    public void loadFavicons() {
        favicons.clear();

        try (DirectoryStream<Path> ds = Files.newDirectoryStream(platform.getFaviconFolder(), new PistonMOTDPlatform.FaviconFilter())) {
            for (Path p : ds) {
                try {
                    favicons.put(p.getFileName().toString(), platform.createFavicon(p));
                } catch (Exception e) {
                    e.printStackTrace();
                    platform.error("Could not load favicon! (" + p.getFileName() + ")");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadHooks() {
        platform.startup("Looking for hooks");
        if (platform.isPluginEnabled(platform.getSuperVanishName())) {
            platform.startup("Hooking into SuperVanish");
            superVanish.set(true);
        }

        if (platform.isPluginEnabled(platform.getPremiumVanishName())) {
            platform.startup("Hooking into PremiumVanish");
            premiumVanish.set(true);
        }

        if (platform.isPluginEnabled(platform.getLuckPermsName())) {
            platform.startup("Hooking into LuckPerms");
            luckPerms.set(new LuckPermsWrapper());
        }
    }

    public void checkUpdate() {
        platform.startup("Checking for a newer version");
        new UpdateChecker(new PistonLogger(platform::info, platform::warn)).getVersion("https://www.pistonmaster.net/PistonMOTD/VERSION.txt", version -> new UpdateParser(platform.getStrippedVersion(), version).parseUpdate(updateType -> {
            if (updateType == UpdateType.NONE || updateType == UpdateType.AHEAD) {
                platform.startup("You're up to date!");
            } else {
                if (updateType == UpdateType.MAJOR) {
                    platform.info(ConsoleColor.RED + "There is a MAJOR update available!" + ConsoleColor.RESET);
                } else if (updateType == UpdateType.MINOR) {
                    platform.info(ConsoleColor.RED + "There is a MINOR update available!" + ConsoleColor.RESET);
                } else if (updateType == UpdateType.PATCH) {
                    platform.info(ConsoleColor.RED + "There is a PATCH update available!" + ConsoleColor.RESET);
                }

                platform.info(ConsoleColor.RED + "Current version: " + platform.getVersion() + " New version: " + version + ConsoleColor.RESET);
                platform.info(ConsoleColor.RED + "Download it at: " + platform.getDownloadURL() + ConsoleColor.RESET);
            }
        }));
    }
}
