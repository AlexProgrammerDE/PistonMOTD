package net.pistonmaster.pistonmotd.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.util.Favicon;
import lombok.Getter;
import net.pistonmaster.pistonmotd.data.PluginData;
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlatform;
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlugin;
import net.pistonmaster.pistonmotd.shared.PlayerWrapper;
import net.pistonmaster.pistonmotd.shared.StatusFavicon;
import org.bstats.velocity.Metrics;
import org.slf4j.Logger;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Plugin(id = "pistonmotd", name = PluginData.NAME, version = PluginData.VERSION, description = PluginData.DESCRIPTION, url = PluginData.URL, authors = {"AlexProgrammerDE"})
public class PistonMOTDVelocity implements PistonMOTDPlatform {
    private final ProxyServer proxyServer;
    private final Logger log;
    private final Path pluginDir;
    private final PluginContainer container;
    private final Metrics.Factory metricsFactory;
    @Getter
    private final PistonMOTDPlugin plugin = new PistonMOTDPlugin(this);

    @Inject
    public PistonMOTDVelocity(ProxyServer proxyServer, Logger log, @DataDirectory Path pluginDir, PluginContainer container, Metrics.Factory metricsFactory) {
        this.proxyServer = proxyServer;
        this.log = log;
        this.pluginDir = pluginDir;
        this.container = container;
        this.metricsFactory = metricsFactory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        plugin.logName();

        plugin.startupLoadConfig();

        plugin.registerCommonPlaceholder();

        plugin.loadHooks();

        startup("Registering listeners");
        proxyServer.getEventManager().register(this, new PingEvent(plugin));

        startup("Registering command");
        proxyServer.getCommandManager().register("pistonmotd", new VelocityCommand(this), "pistonmotdv", "pistonmotdvelocity");

        if (container.getDescription().getVersion().isPresent()) {
            plugin.checkUpdate();
        }

        startup("Loading metrics");
        metricsFactory.make(this, 14316);

        startup("Done! :D");
    }

    @Override
    public boolean isPluginEnabled(String pluginName) {
        return proxyServer.getPluginManager().getPlugin(pluginName).isPresent();
    }

    @Override
    public StatusFavicon createFavicon(Path path) throws Exception {
        return new StatusFavicon(Favicon.create(path));
    }

    @Override
    public Path getPluginConfigFile() {
        return pluginDir.resolve("config.yml");
    }

    @Override
    public Path getFaviconFolder() {
        return pluginDir.resolve("favicons");
    }

    @Override
    public InputStream getDefaultConfig() {
        return getClass().getClassLoader().getResourceAsStream("config.yml");
    }

    @Override
    public List<PlayerWrapper> getPlayers() {
        return proxyServer.getAllPlayers().stream().map(this::wrap).collect(Collectors.toList());
    }

    @Override
    public int getMaxPlayers() {
        return proxyServer.getConfiguration().getShowMaxPlayers();
    }

    @Override
    public int getPlayerCount() {
        return proxyServer.getPlayerCount();
    }

    private PlayerWrapper wrap(Player player) {
        return new PlayerWrapper() {
            @Override
            public String getDisplayName() {
                return player.getUsername();
            }

            @Override
            public String getName() {
                return player.getUsername();
            }

            @Override
            public UUID getUniqueId() {
                return player.getUniqueId();
            }
        };
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

    @Override
    public String getSuperVanishName() {
        return "supervanish"; // Does not support velocity
    }

    @Override
    public String getPremiumVanishName() {
        return "premiumvanish"; // Does not support velocity
    }

    @Override
    public String getLuckPermsName() {
        return "luckperms";
    }
}
