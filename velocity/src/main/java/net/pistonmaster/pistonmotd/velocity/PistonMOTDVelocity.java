package net.pistonmaster.pistonmotd.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.data.PluginData;
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlugin;
import net.pistonmaster.pistonmotd.shared.utils.ConsoleColor;
import net.pistonmaster.pistonmotd.shared.utils.LuckPermsWrapper;
import net.pistonmaster.pistonutils.logging.PistonLogger;
import net.pistonmaster.pistonutils.update.UpdateChecker;
import net.pistonmaster.pistonutils.update.UpdateParser;
import net.pistonmaster.pistonutils.update.UpdateType;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Plugin(id = "pistonmotd", name = PluginData.NAME, version = PluginData.VERSION, description = PluginData.DESCRIPTION, url = PluginData.URL, authors = {"AlexProgrammerDE"})
public class PistonMOTDVelocity implements PistonMOTDPlugin {
    protected final ProxyServer server;
    private final Logger log;
    protected ConfigurationNode rootNode;
    protected File icons;
    protected LuckPermsWrapper luckpermsWrapper = null;

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
        logName();

        log.info(ConsoleColor.CYAN + "Loading config" + ConsoleColor.RESET);
        loadConfig();

        log.info(ConsoleColor.CYAN + "Registering placeholders" + ConsoleColor.RESET);
        PlaceholderUtil.registerParser(new CommonPlaceholder(server));

        log.info(ConsoleColor.CYAN + "Looking for hooks" + ConsoleColor.RESET);
        if (server.getPluginManager().getPlugin("luckperms").isPresent()) {
            try {
                log.info(ConsoleColor.CYAN + "Hooking into LuckPerms" + ConsoleColor.RESET);
                luckpermsWrapper = new LuckPermsWrapper();
            } catch (Exception ignored) {
            }
        }

        log.info(ConsoleColor.CYAN + "Registering listeners" + ConsoleColor.RESET);
        server.getEventManager().register(this, new PingEvent(this));

        log.info(ConsoleColor.CYAN + "Registering command" + ConsoleColor.RESET);
        server.getCommandManager().register("pistonmotdv", new VelocityCommand(this));

        if (container.getDescription().getVersion().isPresent()) {
            checkUpdate();
        }

        log.info(ConsoleColor.CYAN + "Done! :D" + ConsoleColor.RESET);
    }

    protected void loadConfig() {
        try {
            final File oldConfigFile = new File(pluginDir.toFile(), "config.yml");

            File file = new File(pluginDir.toFile(), "config.conf");

            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setFile(file).build();

            if (oldConfigFile.exists()) {
                loader.save(YAMLConfigurationLoader.builder().setFile(oldConfigFile).build().load());

                Files.delete(oldConfigFile.toPath());
            }

            if (!pluginDir.toFile().exists() && !pluginDir.toFile().mkdir()) {
                throw new IOException("Couldn't create folder!");
            }

            if (!file.exists()) {
                try {
                    Files.copy(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("velocity.conf")), file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            rootNode = loader.load();

            rootNode.mergeValuesFrom(HoconConfigurationLoader.builder().setURL(Objects.requireNonNull(getClass().getClassLoader().getResource("velocity.conf")).toURI().toURL()).build().load());

            loader.save(rootNode);

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

    @Override
    public String getVersion() {
        return container.getDescription().getVersion().orElse("Unknown");
    }

    @Override
    public void info(String message) {
        log.info(message);
    }

    @Override
    public void warn(String message) {
        log.warn(message);
    }

    @Override
    public void error(String message) {
        log.error(message);
    }
}
