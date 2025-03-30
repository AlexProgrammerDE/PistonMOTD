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
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.data.PluginData;
import net.pistonmaster.pistonmotd.shared.*;
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
        PistonMOTDPlugin plugin = new PistonMOTDPlugin(this);
        plugin.logName();

        plugin.startupLoadConfig();

        plugin.registerCommonPlaceholder();
        PlaceholderUtil.registerParser(new ServerPlaceholder(proxyServer));

        plugin.loadHooks();

        startup("Registering listeners");
        proxyServer.getEventManager().register(this, new PingEvent(new StatusPingHandler(plugin)));

        startup("Registering command");
        proxyServer.getCommandManager().register("pistonmotd", new VelocityCommand(plugin), "pistonmotdv", "pistonmotdvelocity");

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
    public InputStream getBundledResource(String name) {
        return getClass().getClassLoader().getResourceAsStream(name);
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

            @Override
            public Object getHandle() {
                return player;
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
        return isPluginEnabled("premiumvanish");
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
