package net.pistonmaster.pistonmotd.shared;

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
import java.nio.file.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public interface PistonMOTDPlugin {
    AxiomConfiguration config = new AxiomConfiguration();
    Map<String, StatusFavicon> favicons = new LinkedHashMap<>();
    ThreadLocalRandom random = ThreadLocalRandom.current();
    AtomicBoolean premiumVanish = new AtomicBoolean();
    AtomicBoolean superVanish = new AtomicBoolean();
    AtomicReference<LuckPermsWrapper> luckPerms = new AtomicReference<>();

    default AxiomConfiguration getPluginConfig() {
        return config;
    }

    default void logName() {
        info("  _____  _       _                 __  __   ____  _______  _____  ");
        info(" |  __ \\(_)     | |               |  \\/  | / __ \\|__   __||  __ \\ ");
        info(" | |__) |_  ___ | |_  ___   _ __  | \\  / || |  | |  | |   | |  | |");
        info(" |  ___/| |/ __|| __|/ _ \\ | '_ \\ | |\\/| || |  | |  | |   | |  | |");
        info(" | |    | |\\__ \\| |_| (_) || | | || |  | || |__| |  | |   | |__| |");
        info(" |_|    |_||___/ \\__|\\___/ |_| |_||_|  |_| \\____/   |_|   |_____/ ");
        info("");
    }

    default void startupLoadConfig() {
        startup("Loading config");
        loadConfig();
    }

    default void loadConfig() {
        Path pluginConfigFile = getPluginConfigFile();
        AxiomConfiguration defaultConfig = new AxiomConfiguration();

        try {
            Files.createDirectories(pluginConfigFile.getParent());

            try (InputStream is = getDefaultConfig()) {
                defaultConfig.load(is);
            }

            if (!Files.exists(pluginConfigFile)) {
                defaultConfig.save(pluginConfigFile);
            }

            config.load(pluginConfigFile);

            config.mergeDefault(defaultConfig);

            config.save(pluginConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
            error("Could not load config");
        }

        try {
            Path iconFolder = getFaviconFolder();
            if (!Files.exists(iconFolder)) {
                Files.createDirectories(iconFolder);
            }
        } catch (IOException e) {
            e.printStackTrace();
            error("Could not create the icon directory!");
        }

        loadFavicons();
    }

    default void registerCommonPlaceholder() {
        startup("Registering placeholders");
        PlaceholderUtil.registerParser(new CommonPlaceholder(this));
    }

    default void loadFavicons() {
        favicons.clear();

        try (DirectoryStream<Path> ds = Files.newDirectoryStream(getFaviconFolder(), new FaviconFilter())) {
            for (Path p : ds) {
                try {
                    favicons.put(p.getFileName().toString(), createFavicon(p));
                } catch (Exception e) {
                    e.printStackTrace();
                    error("Could not load favicon! (" + p.getFileName() + ")");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void loadHooks() {
        startup("Looking for hooks");
        if (isPluginEnabled(getSuperVanishName())) {
            startup("Hooking into SuperVanish");
            superVanish.set(true);
        }

        if (isPluginEnabled(getPremiumVanishName())) {
            startup("Hooking into PremiumVanish");
            premiumVanish.set(true);
        }

        if (isPluginEnabled(getLuckPermsName())) {
            startup("Hooking into LuckPerms");
            luckPerms.set(new LuckPermsWrapper());
        }
    }

    boolean isPluginEnabled(String pluginName);

    StatusFavicon createFavicon(Path path) throws Exception;

    Path getPluginConfigFile();

    Path getFaviconFolder();

    InputStream getDefaultConfig();

    List<PlayerWrapper> getPlayers();

    int getMaxPlayers();

    int getPlayerCount();

    default void checkUpdate() {
        startup("Checking for a newer version");
        new UpdateChecker(new PistonLogger(this::info, this::warn)).getVersion("https://www.pistonmaster.net/PistonMOTD/VERSION.txt", version -> new UpdateParser(getStrippedVersion(), version).parseUpdate(updateType -> {
            if (updateType == UpdateType.NONE || updateType == UpdateType.AHEAD) {
                startup("You're up to date!");
            } else {
                if (updateType == UpdateType.MAJOR) {
                    info(ConsoleColor.RED + "There is a MAJOR update available!" + ConsoleColor.RESET);
                } else if (updateType == UpdateType.MINOR) {
                    info(ConsoleColor.RED + "There is a MINOR update available!" + ConsoleColor.RESET);
                } else if (updateType == UpdateType.PATCH) {
                    info(ConsoleColor.RED + "There is a PATCH update available!" + ConsoleColor.RESET);
                }

                info(ConsoleColor.RED + "Current version: " + getVersion() + " New version: " + version + ConsoleColor.RESET);
                info(ConsoleColor.RED + "Download it at: " + getDownloadURL() + ConsoleColor.RESET);
            }
        }));
    }

    default String getDownloadURL() {
        return "https://github.com/AlexProgrammerDE/PistonMOTD/releases";
    }

    default String getStrippedVersion() {
        return getVersion().replace("-SNAPSHOT", "");
    }

    String getVersion();

    void info(String message);

    void warn(String message);

    void error(String message);

    default void startup(String message) {
        info(ConsoleColor.CYAN + message + ConsoleColor.RESET);
    }

    String getSuperVanishName();

    String getPremiumVanishName();

    String getLuckPermsName();

    class FaviconFilter implements DirectoryStream.Filter<Path> {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.png");

        @Override
        public boolean accept(Path entry) {
            return !Files.isDirectory(entry) && matcher.matches(entry);
        }
    }
}
