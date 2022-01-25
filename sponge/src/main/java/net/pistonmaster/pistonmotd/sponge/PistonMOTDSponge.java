package net.pistonmaster.pistonmotd.sponge;

import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlugin;
import net.pistonmaster.pistonmotd.shared.PlayerWrapper;
import net.pistonmaster.pistonmotd.shared.StatusFavicon;
import net.pistonmaster.pistonmotd.shared.utils.LuckPermsWrapper;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.bstats.sponge.Metrics;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.network.status.Favicon;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.metric.MetricsConfigManager;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Plugin("pistonmotd")
public class PistonMOTDSponge implements PistonMOTDPlugin {
    private final Metrics.Factory metricsFactory;
    protected LuckPermsWrapper luckpermsWrapper = null;
    @Inject
    protected Game game;
    @Inject
    private Logger log;

    @Inject
    @ConfigDir(sharedRoot = true)
    private Path publicConfigDir;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path privateConfigDir;

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

        registerCommonPlaceholder();

        startup("Looking for hooks");
        if (game.pluginManager().plugin("luckperms").isPresent()) {
            try {
                startup("Hooking into LuckPerms");
                luckpermsWrapper = new LuckPermsWrapper();
            } catch (Exception ignored) {
            }
        }

        startup("Registering listeners");
        game.eventManager().registerListeners(container, new PingEvent(this));
        game.eventManager().registerListeners(container, new JoinEvent(this));

        if (getPluginConfig().getBoolean("updatechecking")) {
            checkUpdate();
        }

        final Tristate collectionState = this.getEffectiveCollectionState();
        if (collectionState == Tristate.TRUE) {
            startup("Loading metrics");
            metricsFactory.make(9204);
        } else if (collectionState == Tristate.UNDEFINED) {
            startup("Hey there! It seems like data collection is disabled. :( ");
            startup("But you change fix this!");
            startup("Just execute: \"/sponge metrics pistonmotd enable\".");
            startup("This includes only small infos about the server,");
            startup("like its version and the plugin version.");
        }

        startup("Done! :D");
    }

    @Listener
    public void onRegisterCommands(final RegisterCommandEvent<Command.Parameterized> event) {
        startup("Registering command");
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
    public StatusFavicon createFavicon(Path path) throws Exception {
        return new StatusFavicon(Favicon.load(path));
    }

    @Override
    public Path getPluginConfigFile() {
        return publicConfigDir.resolve("pistonmotd.yml");
    }

    @Override
    public Path getFaviconFolder() {
        return privateConfigDir.resolve("favicons");
    }

    @Override
    public InputStream getDefaultConfig() {
        return container.openResource(URI.create("config.yml")).orElse(null);
    }

    @Override
    public List<PlayerWrapper> getPlayers() {
        return game.server().onlinePlayers().stream().map(this::wrap).collect(Collectors.toList());
    }

    @Override
    public int getMaxPlayers() {
        return game.server().maxPlayers();
    }

    @Override
    public int getPlayerCount() {
        return getPlayers().size();
    }

    private PlayerWrapper wrap(Player player) {
        return new PlayerWrapper() {
            @Override
            public String getDisplayName() {
                return LegacyComponentSerializer.legacySection().serialize(player.displayName().get());
            }

            @Override
            public String getName() {
                return player.name();
            }

            @Override
            public UUID getUniqueId() {
                return player.profile().uuid();
            }
        };
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
