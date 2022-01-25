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
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlugin;
import net.pistonmaster.pistonmotd.shared.PlayerWrapper;
import net.pistonmaster.pistonmotd.shared.StatusFavicon;
import net.pistonmaster.pistonmotd.shared.utils.LuckPermsWrapper;
import org.slf4j.Logger;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Plugin(id = "pistonmotd", name = PluginData.NAME, version = PluginData.VERSION, description = PluginData.DESCRIPTION, url = PluginData.URL, authors = {"AlexProgrammerDE"})
public class PistonMOTDVelocity implements PistonMOTDPlugin {
    protected final ProxyServer proxyServer;
    private final Logger log;
    protected LuckPermsWrapper luckpermsWrapper = null;

    @Inject
    @DataDirectory
    private Path pluginDir;

    @Inject
    private PluginContainer container;

    @Inject
    public PistonMOTDVelocity(ProxyServer proxyServer, Logger log) {
        this.proxyServer = proxyServer;
        this.log = log;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logName();

        loadConfig();

        registerCommonPlaceholder();

        startup("Looking for hooks");
        if (proxyServer.getPluginManager().getPlugin("luckperms").isPresent()) {
            try {
                startup("Hooking into LuckPerms");
                luckpermsWrapper = new LuckPermsWrapper();
            } catch (Exception ignored) {
            }
        }

        startup("Registering listeners");
        proxyServer.getEventManager().register(this, new PingEvent(this));

        startup("Registering command");
        proxyServer.getCommandManager().register("pistonmotd", new VelocityCommand(this), "pistonmotdv", "pistonmotdvelocity");

        if (container.getDescription().getVersion().isPresent()) {
            checkUpdate();
        }

        startup("Done! :D");
    }

    @Override
    public StatusFavicon createFavicon(Path path) throws Exception {
        return new StatusFavicon(Favicon.create(path));
    }

    @Override
    public Path getPluginConfigFile() {
        return pluginDir.resolve("config.conf");
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
}
