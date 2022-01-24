package net.pistonmaster.pistonmotd.shared;

import net.pistonmaster.pistonmotd.shared.utils.ConsoleColor;
import net.pistonmaster.pistonmotd.shared.utils.LuckPermsWrapper;
import net.pistonmaster.pistonutils.logging.PistonLogger;
import net.pistonmaster.pistonutils.update.UpdateChecker;
import net.pistonmaster.pistonutils.update.UpdateParser;
import net.pistonmaster.pistonutils.update.UpdateType;
import net.skinsrestorer.axiom.AxiomConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public interface PistonMOTDPlugin {
    AxiomConfiguration config = new AxiomConfiguration();
    List<StatusFavicon> favicons = new ArrayList<>();
    AtomicReference<LuckPermsWrapper> luckpermsWrapper = new AtomicReference<>();
    ThreadLocalRandom random = ThreadLocalRandom.current();

    default AxiomConfiguration getConfig() {
        return config;
    }

    default void logName() {
        info("  _____  _       _                 __  __   ____  _______  _____  ");
        info(" |  __ \\(_)     | |               |  \\/  | / __ \\|__   __||  __ \\ ");
        info(" | |__) |_  ___ | |_  ___   _ __  | \\  / || |  | |  | |   | |  | |");
        info(" |  ___/| |/ __|| __|/ _ \\ | '_ \\ | |\\/| || |  | |  | |   | |  | |");
        info(" | |    | |\\__ \\| |_| (_) || | | || |  | || |__| |  | |   | |__| |");
        info(" |_|    |_||___/ \\__|\\___/ |_| |_||_|  |_| \\____/   |_|   |_____/ ");
        info("                                                                  ");
    }

    default void loadConfig() {
        info(ConsoleColor.CYAN + "Loading config" + ConsoleColor.RESET);
        Path pluginConfigFile = getPluginConfigFile();
        try {
            config.load(pluginConfigFile);
        } catch (IOException e) {
            error("Could not load config");
        }

        try (InputStream defaultConfig = getDefaultConfig()) {
            AxiomConfiguration defaultConfigs = new AxiomConfiguration();
            defaultConfigs.load(defaultConfig);

            config.mergeDefault(defaultConfigs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.createDirectories(getIconFolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Path getPluginConfigFile();

    Path getIconFolder();

    InputStream getDefaultConfig();

    List<PlayerWrapper> getPlayers();

    default void checkUpdate() {
        info(ConsoleColor.CYAN + "Checking for a newer version" + ConsoleColor.RESET);
        new UpdateChecker(new PistonLogger(this::info, this::warn)).getVersion("https://www.pistonmaster.net/PistonMOTD/VERSION.txt", version -> new UpdateParser(getVersion(), version).parseUpdate(updateType -> {
            if (updateType == UpdateType.NONE || updateType == UpdateType.AHEAD) {
                info(ConsoleColor.CYAN + "You're up to date!" + ConsoleColor.RESET);
            } else {
                if (updateType == UpdateType.MAJOR) {
                    info(ConsoleColor.RED + "There is a MAJOR update available!" + ConsoleColor.RESET);
                } else if (updateType == UpdateType.MINOR) {
                    info(ConsoleColor.RED + "There is a MINOR update available!" + ConsoleColor.RESET);
                } else if (updateType == UpdateType.PATCH) {
                    info(ConsoleColor.RED + "There is a PATCH update available!" + ConsoleColor.RESET);
                }

                info(ConsoleColor.RED + "Current version: " + getVersion() + " New version: " + version + ConsoleColor.RESET);
                info(ConsoleColor.RED + "Download it at: https://ore.spongepowered.org/AlexProgrammerDE/PistonMOTD/versions" + ConsoleColor.RESET);
            }
        }));
    }

    String getVersion();

    void info(String message);

    void warn(String message);

    void error(String message);
}
