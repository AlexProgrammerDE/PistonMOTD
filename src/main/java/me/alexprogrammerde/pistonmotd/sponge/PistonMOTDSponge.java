package me.alexprogrammerde.pistonmotd.sponge;

import com.google.inject.Inject;
import me.alexprogrammerde.pistonmotd.api.PlaceholderUtil;
import me.alexprogrammerde.pistonmotd.data.PluginData;
import me.alexprogrammerde.pistonmotd.utils.ConsoleColor;
import me.alexprogrammerde.pistonmotd.utils.UpdateChecker;
import me.alexprogrammerde.pistonmotd.utils.UpdateParser;
import me.alexprogrammerde.pistonmotd.utils.UpdateType;
import net.md_5.bungee.api.ChatColor;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.bstats.sponge.MetricsLite2;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.metric.MetricsConfigManager;

import java.io.File;
import java.nio.file.Path;

@Plugin(id = "pistonmotd", name = PluginData.NAME, version = PluginData.VERSION, description = PluginData.DESCRIPTION)
public class PistonMOTDSponge {
    protected ConfigurationNode rootNode;
    private final MetricsLite2.Factory metricsFactory;
    protected File icons;

    @Inject
    private Logger log;

    @Inject
    private Game game;

    @Inject
    @ConfigDir(sharedRoot = true)
    private Path privateConfigDir;

    @Inject
    private PluginContainer container;

    @Inject
    private MetricsConfigManager metricsConfigManager;

    @Inject
    public PistonMOTDSponge(MetricsLite2.Factory metricsFactory) {
        this.metricsFactory = metricsFactory;
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        log.info("  _____  _       _                 __  __   ____  _______  _____  ");
        log.info(" |  __ \\(_)     | |               |  \\/  | / __ \\|__   __||  __ \\ ");
        log.info(" | |__) |_  ___ | |_  ___   _ __  | \\  / || |  | |  | |   | |  | |");
        log.info(" |  ___/| |/ __|| __|/ _ \\ | '_ \\ | |\\/| || |  | |  | |   | |  | |");
        log.info(" | |    | |\\__ \\| |_| (_) || | | || |  | || |__| |  | |   | |__| |");
        log.info(" |_|    |_||___/ \\__|\\___/ |_| |_||_|  |_| \\____/   |_|   |_____/ ");
        log.info("                                                                  ");

        log.info(ConsoleColor.CYAN + "Loading config" + ConsoleColor.RESET);
        loadConfig();

        log.info(ConsoleColor.CYAN + "Registering command" + ConsoleColor.RESET);
        CommandSpec help = CommandSpec.builder()
                .description(Text.of("Get help about PistonMOTD!"))
                .permission("pistonmotd.help")
                .executor(new SpongeHelpCommand())
                .build();

        CommandSpec reload = CommandSpec.builder()
                .description(Text.of("Reload the configuration of PistonMOTD!"))
                .permission("pistonmotd.reload")
                .executor(new SpongeReloadCommand(this))
                .build();

        CommandSpec command = CommandSpec.builder()
                .description(Text.of("Main command of PistonMOTD"))
                .child(help, "help")
                .child(reload, "reload")
                .build();

        game.getCommandManager().register(this, command, "pistonmotd", "pistonmotdsponge");

        log.info(ConsoleColor.CYAN + "Registering placeholders" + ConsoleColor.RESET);
        PlaceholderUtil.registerParser(new CommonPlaceholder(game));

        log.info(ConsoleColor.CYAN + "Registering listeners" + ConsoleColor.RESET);
        game.getEventManager().registerListeners(this, new PingEvent(this));
        game.getEventManager().registerListeners(this, new JoinEvent(this));

        if (container.getVersion().isPresent() && rootNode.getNode("").getBoolean()) {
            log.info(ChatColor.AQUA + "Checking for a newer version");
            new UpdateChecker(log).getVersion(version -> new UpdateParser(container.getVersion().get(), version).parseUpdate(updateType -> {
                if (updateType == UpdateType.NONE || updateType == UpdateType.AHEAD) {
                    log.info(ChatColor.AQUA + "Your up to date!");
                } else {
                    if (updateType == UpdateType.MAJOR) {
                        log.info(ChatColor.RED + "There is a MAJOR update available!");
                    } else if (updateType == UpdateType.MINOR) {
                        log.info(ChatColor.RED + "There is a MINOR update available!");
                    } else if (updateType == UpdateType.PATCH) {
                        log.info(ChatColor.RED + "There is a PATCH update available!");
                    }

                    log.info(ChatColor.RED + "Current version: " + container.getVersion().get() + " New version: " + version);
                    log.info(ChatColor.RED + "Download it at: https://www.spigotmc.org/resources/80567");
                }
            }));
        }

        if (hasConsent()) {
            log.info(ConsoleColor.CYAN + "Loading metrics" + ConsoleColor.RESET);
            metricsFactory.make(9204);
        } else {
            log.info(ConsoleColor.CYAN + "Hey there! It seems like data collection is disabled. :( " + ConsoleColor.RESET);
            log.info(ConsoleColor.CYAN + "But don't worry... You can fix this! " + ConsoleColor.RESET);
            log.info(ConsoleColor.CYAN + "Just execute: \"/sponge metrics pistonmotd enable\"." + ConsoleColor.RESET);
            log.info(ConsoleColor.CYAN + "This info is just to give me small info about the server," + ConsoleColor.RESET);
            log.info(ConsoleColor.CYAN + "like its version and the plugin version." + ConsoleColor.RESET);
        }
    }

    protected void loadConfig() {
        try {
            if (container.getAsset("sponge.yml").isPresent()) {
                Asset asset = container.getAsset("sponge.yml").get();

                asset.copyToFile(new File(privateConfigDir.toFile(), "pistonmotd.yml").toPath(), false, true);
                rootNode = YAMLConfigurationLoader.builder().setPath(new File(privateConfigDir.toFile(), "pistonmotd.yml").toPath()).build().load();

                rootNode.mergeValuesFrom(YAMLConfigurationLoader.builder()
                        .setURL(asset.getUrl())
                        .build()
                        .load(ConfigurationOptions.defaults()));

                File iconFolder = new File(privateConfigDir.toFile(), "icons");

                if (!iconFolder.exists())
                    iconFolder.mkdir();
                icons = iconFolder;
            } else {
                throw new Exception("Default configuration file missing in jar!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean hasConsent() {
        return metricsConfigManager.getCollectionState(this.container).asBoolean();
    }
}