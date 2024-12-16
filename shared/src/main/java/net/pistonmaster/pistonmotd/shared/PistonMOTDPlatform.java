package net.pistonmaster.pistonmotd.shared;

import net.pistonmaster.pistonmotd.shared.utils.ConsoleColor;

import java.io.InputStream;
import java.nio.file.*;
import java.util.List;

public interface PistonMOTDPlatform {
    boolean isPluginEnabled(String pluginName);

    StatusFavicon createFavicon(Path path) throws Exception;

    Path getPluginConfigFile();

    Path getFaviconFolder();

    InputStream getBundledResource(String name);

    List<PlayerWrapper> getPlayers();

    int getMaxPlayers();

    int getPlayerCount();

    default String getDownloadURL() {
        return "https://github.com/AlexProgrammerDE/PistonMOTD/releases";
    }

    default String getStrippedVersion() {
        return getVersion().replace("-SNAPSHOT", "");
    }

    String getVersion();

    void info(String message);

    default void warn(String message) {
        warn(message, null);
    }

    void warn(String message, Throwable t);

    void error(String message, Throwable t);

    default void startup(String message) {
        info(ConsoleColor.CYAN + message + ConsoleColor.RESET);
    }

    boolean isSuperVanishBukkitAvailable();

    boolean isPremiumVanishBukkitAvailable();

    boolean isPremiumVanishBungeeAvailable();

    boolean isPremiumVanishVelocityAvailable();

    boolean isLuckPermsAvailable();

    Class<?> getPlayerClass();

    class FaviconFilter implements DirectoryStream.Filter<Path> {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.png");

        @Override
        public boolean accept(Path entry) {
            return !Files.isDirectory(entry) && matcher.matches(entry);
        }
    }
}
