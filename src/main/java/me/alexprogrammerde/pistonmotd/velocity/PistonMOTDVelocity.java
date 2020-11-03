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
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Plugin(id = "pistonmotd", name = PluginData.NAME, version = PluginData.VERSION, description = PluginData.DESCRIPTION, authors = {"AlexProgrammerDE"})
public class PistonMOTDVelocity {
    private final ProxyServer server;
    private final Logger logger;
    protected ConfigurationNode rootNode;
    protected File icons;

    @Inject
    @DataDirectory
    private Path pluginDir;

    @Inject
    private PluginContainer container;

    @Inject
    public PistonMOTDVelocity(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("  _____  _       _                 __  __   ____  _______  _____  ");
        logger.info(" |  __ \\(_)     | |               |  \\/  | / __ \\|__   __||  __ \\ ");
        logger.info(" | |__) |_  ___ | |_  ___   _ __  | \\  / || |  | |  | |   | |  | |");
        logger.info(" |  ___/| |/ __|| __|/ _ \\ | '_ \\ | |\\/| || |  | |  | |   | |  | |");
        logger.info(" | |    | |\\__ \\| |_| (_) || | | || |  | || |__| |  | |   | |__| |");
        logger.info(" |_|    |_||___/ \\__|\\___/ |_| |_||_|  |_| \\____/   |_|   |_____/ ");
        logger.info("                                                                  ");

        logger.info(ConsoleColor.CYAN + "Loading config" + ConsoleColor.RESET);
        loadConfig();

        logger.info(ConsoleColor.CYAN + "Registering placeholders" + ConsoleColor.RESET);
        PlaceholderUtil.registerParser(new CommonPlaceholder(server));

        server.getCommandManager().register("pistonmotdv", new VelocityCommand(this));

        logger.info(ConsoleColor.CYAN + "Registering listeners" + ConsoleColor.RESET);
        server.getEventManager().register(this, new PingEvent(this));
    }

    protected void loadConfig() {
        try {
            if (!pluginDir.toFile().exists()) {
                pluginDir.toFile().mkdir();
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

            if (!iconFolder.exists())
                iconFolder.mkdir();
            icons = iconFolder;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}