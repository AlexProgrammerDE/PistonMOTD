package me.alexprogrammerde.pistonmotd.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.alexprogrammerde.pistonmotd.api.PlaceholderUtil;
import me.alexprogrammerde.pistonmotd.data.PluginData;
import me.alexprogrammerde.pistonmotd.utils.ConsoleColor;
import me.alexprogrammerde.pistonmotd.utils.UpdateChecker;
import me.alexprogrammerde.pistonmotd.utils.UpdateParser;
import me.alexprogrammerde.pistonmotd.utils.UpdateType;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Plugin(id = "pistonmotd", name = PluginData.NAME, version = PluginData.VERSION, description = PluginData.DESCRIPTION, url = PluginData.URL, authors = {"AlexProgrammerDE"})
public class PistonMOTDVelocity {
    protected final ProxyServer server;
    private final Logger log;
    protected ConfigurationNode rootNode;
    protected File icons;

    @Inject
    @DataDirectory
    private Path pluginDir;

    @Inject
    private PluginContainer container;

    @Inject
    public PistonMOTDVelocity(ProxyServer server, Logger log) {
        this.server = server;
        this.log = log;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        log.info("  _____  _       _                 __  __   ____  _______  _____  ");
        log.info(" |  __ \\(_)     | |               |  \\/  | / __ \\|__   __||  __ \\ ");
        log.info(" | |__) |_  ___ | |_  ___   _ __  | \\  / || |  | |  | |   | |  | |");
        log.info(" |  ___/| |/ __|| __|/ _ \\ | '_ \\ | |\\/| || |  | |  | |   | |  | |");
        log.info(" | |    | |\\__ \\| |_| (_) || | | || |  | || |__| |  | |   | |__| |");
        log.info(" |_|    |_||___/ \\__|\\___/ |_| |_||_|  |_| \\____/   |_|   |_____/ ");
        log.info("                                                                  ");

        log.info(ConsoleColor.CYAN + "Loading config" + ConsoleColor.RESET);
        loadConfig();

        log.info(ConsoleColor.CYAN + "Registering placeholders" + ConsoleColor.RESET);
        PlaceholderUtil.registerParser(new CommonPlaceholder(server));

        server.getCommandManager().register("pistonmotdv", new VelocityCommand(this));

        log.info(ConsoleColor.CYAN + "Registering listeners" + ConsoleColor.RESET);
        server.getEventManager().register(this, new PingEvent(this));

        if (container.getDescription().getVersion().isPresent()) {
            log.info(ConsoleColor.CYAN + "Checking for a newer version" + ConsoleColor.RESET);
            new UpdateChecker(log).getVersion(version -> new UpdateParser(container.getDescription().getVersion().get(), version).parseUpdate(updateType -> {
                if (updateType == UpdateType.NONE || updateType == UpdateType.AHEAD) {
                    log.info(ConsoleColor.CYAN + "Your up to date!" + ConsoleColor.RESET);
                } else {
                    if (updateType == UpdateType.MAJOR) {
                        log.info(ConsoleColor.RED + "There is a MAJOR update available!" + ConsoleColor.RESET);
                    } else if (updateType == UpdateType.MINOR) {
                        log.info(ConsoleColor.RED + "There is a MINOR update available!" + ConsoleColor.RESET);
                    } else if (updateType == UpdateType.PATCH) {
                        log.info(ConsoleColor.RED + "There is a PATCH update available!" + ConsoleColor.RESET);
                    }

                    log.info(ConsoleColor.RED + "Current version: " + container.getDescription().getVersion().get() + " New version: " + version + ConsoleColor.RESET);
                    log.info(ConsoleColor.RED + "Download it at: https://www.spigotmc.org/resources/80567/updates" + ConsoleColor.RESET);
                }
            }));
        }
    }

    protected void loadConfig() {
        try {
            if (!pluginDir.toFile().exists()) {
                if (!pluginDir.toFile().mkdir()) {
                    throw new IOException("Couldn't create folder!");
                }
            }

            File file = new File(pluginDir.toFile(), "config.yml");

            if (!file.exists()) {
                try {
                    Files.copy(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("velocity.yml")), file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            rootNode = YAMLConfigurationLoader.builder().setPath(file.toPath()).build().load();

            rootNode.mergeValuesFrom(YAMLConfigurationLoader.builder()
                    .setURL(Objects.requireNonNull(getClass().getClassLoader().getResource("velocity.yml")))
                    .build()
                    .load(ConfigurationOptions.defaults()));

            File iconFolder = new File(pluginDir.toFile(), "icons");

            if (!iconFolder.exists()) {
                if (!iconFolder.mkdir()) {
                    throw new IOException("Couldn't create folder!");
                }
            }

            icons = iconFolder;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}