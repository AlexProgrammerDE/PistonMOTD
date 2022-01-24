package net.pistonmaster.pistonmotd.sponge;

import com.google.inject.Inject;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.LinearComponents;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlugin;
import net.pistonmaster.pistonmotd.shared.utils.ConsoleColor;
import net.pistonmaster.pistonmotd.shared.utils.LuckPermsWrapper;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.bstats.sponge.Metrics;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.metric.MetricsConfigManager;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

@Plugin("pistonmotd")
public class PistonMOTDSponge implements PistonMOTDPlugin {
    private final Metrics.Factory metricsFactory;
    protected ConfigurationNode rootNode;
    protected File icons;
    protected LuckPermsWrapper luckpermsWrapper = null;
    @Inject
    protected Game game;
    @Inject
    private Logger log;
    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path defaultConfig;

    @Inject
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    @Inject
    @ConfigDir(sharedRoot = true)
    private Path publicConfigDir;

    @Inject
    private PluginContainer container;

    @Inject
    private MetricsConfigManager metricsConfigManager;

    @Inject
    @SuppressWarnings("SpongeInjection")
    public PistonMOTDSponge(Metrics.Factory metricsFactory) {
        this.metricsFactory = metricsFactory;
    }

    @Listener
    public void onServerStart(final ConstructPluginEvent event) {
        logName();

        loadConfig();

        info(ConsoleColor.CYAN + "Registering placeholders" + ConsoleColor.RESET);
        PlaceholderUtil.registerParser(new CommonPlaceholder(game));

        info(ConsoleColor.CYAN + "Looking for hooks" + ConsoleColor.RESET);
        if (game.pluginManager().plugin("luckperms").isPresent()) {
            try {
                log.info(ConsoleColor.CYAN + "Hooking into LuckPerms" + ConsoleColor.RESET);
                luckpermsWrapper = new LuckPermsWrapper();
            } catch (Exception ignored) {
            }
        }

        info(ConsoleColor.CYAN + "Registering listeners" + ConsoleColor.RESET);
        game.eventManager().registerListeners(container, new PingEvent(this));
        game.eventManager().registerListeners(container, new JoinEvent(this));

        if (rootNode.node("updatechecking").getBoolean()) {
            checkUpdate();
        }

        final Tristate collectionState = this.getEffectiveCollectionState();
        if (collectionState == Tristate.TRUE) {
            log.info(ConsoleColor.CYAN + "Loading metrics" + ConsoleColor.RESET);
            metricsFactory.make(9204);
        } else if (collectionState == Tristate.UNDEFINED) {
            log.info(ConsoleColor.CYAN + "Hey there! It seems like data collection is disabled. :( " + ConsoleColor.RESET);
            log.info(ConsoleColor.CYAN + "But don't worry... You can fix this! " + ConsoleColor.RESET);
            log.info(ConsoleColor.CYAN + "Just execute: \"/sponge metrics pistonmotd enable\"." + ConsoleColor.RESET);
            log.info(ConsoleColor.CYAN + "This info is just to give me small info about the server," + ConsoleColor.RESET);
            log.info(ConsoleColor.CYAN + "like its version and the plugin version." + ConsoleColor.RESET);
        }

        log.info(ConsoleColor.CYAN + "Done! :D" + ConsoleColor.RESET);
    }

    @Listener
    public void onRegisterCommands(final RegisterCommandEvent<Command.Parameterized> event) {
        info(ConsoleColor.CYAN + "Registering command" + ConsoleColor.RESET);
        Command.Parameterized help = Command.builder()
                .shortDescription(Component.text("Get help about PistonMOTD!"))
                .permission("pistonmotd.help")
                .executor(new SpongeHelpCommand())
                .build();

        Command.Parameterized reload = Command.builder()
                .shortDescription(Component.text("Reload the configuration of PistonMOTD!"))
                .permission("pistonmotd.reload")
                .executor(new SpongeReloadCommand(this))
                .build();

        event.register(this.container, Command.builder()
                .shortDescription(Component.text("Main command of PistonMOTD!"))
                .addParameter(Parameter.subcommand(help, "help"))
                .addParameter(Parameter.subcommand(reload, "reload"))
                .build(), "pistonmotd", "pistonmotdsponge");
    }

    @Override
    public Path getPluginConfigFile() {
        return defaultConfig;
    }

    @Override
    public InputStream getDefaultConfig() {
        return null; // TODO
    }

    /**
     * Gets the effective collection state for the plugin.
     * <p>
     * This will return the global collection state fallback, if the server administrator doesn't
     * have a specific collection state for the plugin.
     *
     * @return The collection state
     */
    protected Tristate getEffectiveCollectionState() {
        final Tristate pluginState = this.metricsConfigManager.collectionState(this.container);
        if (pluginState == Tristate.UNDEFINED) {
            return this.metricsConfigManager.globalCollectionState();
        }
        return pluginState;
    }

    @Override
    public String getVersion() {
        ArtifactVersion version = container.metadata().version();

        return version.getMajorVersion() + "." + version.getMinorVersion() + "." + version.getIncrementalVersion();
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
