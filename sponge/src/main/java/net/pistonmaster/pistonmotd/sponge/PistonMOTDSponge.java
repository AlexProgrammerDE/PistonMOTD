package net.pistonmaster.pistonmotd.sponge;

import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.pistonmaster.pistonmotd.shared.*;
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
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("SpongeInjection")
@Plugin("pistonmotd")
public class PistonMOTDSponge implements PistonMOTDPlatform {
    private final Metrics.Factory metricsFactory;
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
    private PistonMOTDPlugin plugin;

    @Inject
    public PistonMOTDSponge(Metrics.Factory metricsFactory) {
        this.metricsFactory = metricsFactory;
    }

    @Listener
    public void onServerStart(final ConstructPluginEvent event) {
        plugin = new PistonMOTDPlugin(this);

        plugin.logName();

        plugin.startupLoadConfig();

        plugin.registerCommonPlaceholder();

        plugin.loadHooks();

        startup("Registering listeners");
        game.eventManager().registerListeners(container, new PingEvent(new StatusPingHandler(plugin)), MethodHandles.publicLookup());
        game.eventManager().registerListeners(container, new JoinEvent(this), MethodHandles.publicLookup());

        if (plugin.getPluginConfig().isUpdateChecking()) {
            plugin.checkUpdate();
        }

        final Tristate collectionState = this.getEffectiveCollectionState();
        if (collectionState == Tristate.TRUE) {
            startup("Loading metrics");
            metricsFactory.make(9204);
        } else if (collectionState == Tristate.UNDEFINED) {
            startup("Hey there! It seems like data collection is disabled. :(");
            startup("But you can enable it!");
            startup("Just execute: \"/sponge metrics pistonmotd enable\"");
            startup("This only collects small infos about the server,");
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
                .executor(new SpongeReloadCommand(plugin))
                .build();

        event.register(this.container, Command.builder()
                .shortDescription(Component.text("Main command of PistonMOTD!"))
                .addParameter(Parameter.subcommand(help, "help"))
                .addParameter(Parameter.subcommand(reload, "reload"))
                .build(), "pistonmotd", "pistonmotdsponge");
    }

    @Override
    public boolean isPluginEnabled(String pluginName) {
        return game.pluginManager().plugin(pluginName).isPresent();
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
    public InputStream getBundledResource(String name) {
        return container.openResource(name).orElse(null);
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

    @Override
    public String getDownloadURL() {
        return "https://ore.spongepowered.org/AlexProgrammerDE/PistonMOTD/versions";
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

            @Override
            public Object getHandle() {
                return player;
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
    public void warn(String message, Throwable t) {
        log.warn(message, t);
    }

    @Override
    public void error(String message, Throwable t) {
        log.error(message, t);
    }

    @Override
    public boolean isSuperVanishBukkitAvailable() {
        return false;
    }

    @Override
    public boolean isPremiumVanishBukkitAvailable() {
        return false;
    }

    @Override
    public boolean isPremiumVanishBungeeAvailable() {
        return false;
    }

    @Override
    public boolean isPremiumVanishVelocityAvailable() {
        return false;
    }

    @Override
    public boolean isLuckPermsAvailable() {
        return isPluginEnabled("luckperms");
    }

    @Override
    public Class<?> getPlayerClass() {
        return Player.class;
    }
}
