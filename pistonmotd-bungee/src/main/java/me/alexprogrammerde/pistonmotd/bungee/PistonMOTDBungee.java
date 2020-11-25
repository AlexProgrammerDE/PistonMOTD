package me.alexprogrammerde.pistonmotd.bungee;

import me.alexprogrammerde.pistonmotd.api.PlaceholderUtil;
import me.alexprogrammerde.pistonmotd.utils.LuckPermsWrapper;
import me.alexprogrammerde.pistonmotd.utils.UpdateChecker;
import me.alexprogrammerde.pistonmotd.utils.UpdateParser;
import me.alexprogrammerde.pistonmotd.utils.UpdateType;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import org.bstats.bungeecord.Metrics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PistonMOTDBungee extends Plugin {
    protected Configuration config;
    protected File icons;
    protected ConfigManager manager;
    protected final List<String> headList = new ArrayList<>();
    protected Logger log;
    protected LuckPermsWrapper luckpermsWrapper = null;

    @Override
    public void onEnable() {
        log = getLogger();
        BungeeAudiences.create(this);

        headList.add("# You can find color codes here: https://minecraft.tools/en/color-code.php");
        headList.add("# Formatting comes after the color! &d&l will work, but not &l&d.");
        headList.add("# MiniMessage formatting IS supported: https://docs.adventure.kyori.net/minimessage.html#template");
        headList.add("# HEX/RGB colors ARE supported. (Only the motd)");
        headList.add("# Hex format: &#FFFFFF");
        headList.add("# Note: The MiniMessage allows you to make rainbow colors and gradients");
        headList.add("# Placeholders: %online% (Players online)");
        headList.add("# %max% (Server max slots)");
        headList.add("# %aftericon% adds a bunch of spaces so the text is after the icon. (Only for protocol)");
        headList.add("# %newline% adds a newline to your motd.");
        headList.add("# %online_SERVERNAME% shows the current playercount of one of the servers.");

        log.info("  _____  _       _                 __  __   ____  _______  _____  ");
        log.info(" |  __ \\(_)     | |               |  \\/  | / __ \\|__   __||  __ \\ ");
        log.info(" | |__) |_  ___ | |_  ___   _ __  | \\  / || |  | |  | |   | |  | |");
        log.info(" |  ___/| |/ __|| __|/ _ \\ | '_ \\ | |\\/| || |  | |  | |   | |  | |");
        log.info(" | |    | |\\__ \\| |_| (_) || | | || |  | || |__| |  | |   | |__| |");
        log.info(" |_|    |_||___/ \\__|\\___/ |_| |_||_|  |_| \\____/   |_|   |_____/ ");
        log.info("                                                                  ");

        log.info(ChatColor.AQUA + "Loading config");
        loadConfig();

        log.info(ChatColor.AQUA + "Registering placeholders");
        for (String server : getProxy().getServers().keySet()) {
            PlaceholderUtil.registerParser(new ServerPlaceholder(server));
        }

        PlaceholderUtil.registerParser(new CommonPlaceholder());

        log.info(ChatColor.AQUA + "Looking for hooks");
        if (getProxy().getPluginManager().getPlugin("LuckPerms") != null) {
            try {
                log.info(ChatColor.AQUA + "Hooking into LuckPerms");
                luckpermsWrapper = new LuckPermsWrapper();
            } catch (Exception ignored) {}
        }

        log.info(ChatColor.AQUA + "Registering listeners");
        getProxy().getPluginManager().registerListener(this, new PingEvent(this, icons));

        log.info(ChatColor.AQUA + "Registering command");
        getProxy().getPluginManager().registerCommand(this, new BungeeCommand(this));

        log.info(ChatColor.AQUA + "Checking for a newer version");
        new UpdateChecker(getLogger()).getVersion(version -> new UpdateParser(getDescription().getVersion(), version).parseUpdate(updateType -> {
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

                log.info(ChatColor.RED + "Current version: " + this.getDescription().getVersion() + " New version: " + version);
                log.info(ChatColor.RED + "Download it at: https://www.spigotmc.org/resources/80567/updates");
            }
        }));

        log.info(ChatColor.AQUA + "Loading metrics");
        new Metrics(this, 8968);
    }

    @Override
    public void onDisable() {
        log.info(ChatColor.AQUA + "Unloading the listeners");
        getProxy().getPluginManager().unregisterListeners(this);

        log.info(ChatColor.AQUA + "Unloading the commands");
        getProxy().getPluginManager().unregisterCommands(this);

        log.info(ChatColor.AQUA + "Finished unloading!");
    }

    protected void loadConfig() {
        manager = new ConfigManager(this, headList);
        config = manager.getConfig("bungeeconfig.yml", "config.yml");
        icons = manager.getIcons();
    }
}
