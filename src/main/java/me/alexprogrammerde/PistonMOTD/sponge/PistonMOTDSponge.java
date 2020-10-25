package me.alexprogrammerde.PistonMOTD.sponge;

import com.google.inject.Inject;
import me.alexprogrammerde.PistonMOTD.api.PlaceholderUtil;
import me.alexprogrammerde.PistonMOTD.utils.ConsoleColor;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.bstats.sponge.Metrics2;
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

@Plugin(id = "pistonmotd", name = "PistonMOTD", version = "3.0.0", description = "Best MOTD plugin multi platform support!")
public class PistonMOTDSponge {
    private ConfigurationNode rootNode;
    private final Metrics2.Factory metricsFactory;

    @Inject
    private Logger logger;

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
    public PistonMOTDSponge(Metrics2.Factory metricsFactory) {
        this.metricsFactory = metricsFactory;
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("  _____  _       _                 __  __   ____  _______  _____  ");
        logger.info(" |  __ \\(_)     | |               |  \\/  | / __ \\|__   __||  __ \\ ");
        logger.info(" | |__) |_  ___ | |_  ___   _ __  | \\  / || |  | |  | |   | |  | |");
        logger.info(" |  ___/| |/ __|| __|/ _ \\ | '_ \\ | |\\/| || |  | |  | |   | |  | |");
        logger.info(" | |    | |\\__ \\| |_| (_) || | | || |  | || |__| |  | |   | |__| |");
        logger.info(" |_|    |_||___/ \\__|\\___/ |_| |_||_|  |_| \\____/   |_|   |_____/ ");
        logger.info("                                                                  ");

        logger.info(ConsoleColor.CYAN + "Loading config" + ConsoleColor.RESET);
        loadConfig();

        logger.info(ConsoleColor.CYAN + "Registering command" + ConsoleColor.RESET);
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

        game.getCommandManager().register(this, command, "pistonmotdsponge");

        logger.info(ConsoleColor.CYAN + "Registering placeholders" + ConsoleColor.RESET);
        PlaceholderUtil.registerParser(new CommonPlaceholder(game));

        logger.info(ConsoleColor.CYAN + "Registering listeners" + ConsoleColor.RESET);
        game.getEventManager().registerListeners(this, new PingEvent(rootNode, this));
        game.getEventManager().registerListeners(this, new JoinEvent(this));

        if (hasConsent()) {
            logger.info(ConsoleColor.CYAN + "Loading metrics" + ConsoleColor.RESET);
            metricsFactory.make(9204);
        } else {
            logger.info(ConsoleColor.CYAN + "Hey there! It seems like data collection is disabled. :( " + ConsoleColor.RESET);
            logger.info(ConsoleColor.CYAN + "But don't worry... You can fix this! " + ConsoleColor.RESET);
            logger.info(ConsoleColor.CYAN + "Just execute: \"/sponge metrics pistonmotd enable\"." + ConsoleColor.RESET);
            logger.info(ConsoleColor.CYAN + "This info is just to give me small info about the server," + ConsoleColor.RESET);
            logger.info(ConsoleColor.CYAN + "like its version and the plugin version." + ConsoleColor.RESET);
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