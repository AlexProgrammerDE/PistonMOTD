package net.pistonmaster.pistonmotd.bungee;

import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlugin;
import net.pistonmaster.pistonmotd.shared.utils.LuckPermsWrapper;
import org.bstats.bungeecord.Metrics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PistonMOTDBungee extends Plugin implements PistonMOTDPlugin {
    protected final List<String> headList = new ArrayList<>();
    protected Configuration config;
    protected File icons;
    protected ConfigManager manager;
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

        logName();

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
            } catch (Exception ignored) {
            }
        }

        log.info(ChatColor.AQUA + "Registering listeners");
        getProxy().getPluginManager().registerListener(this, new PingEvent(this));

        log.info(ChatColor.AQUA + "Registering command");
        getProxy().getPluginManager().registerCommand(this, new BungeeCommand(this));

        checkUpdate();

        log.info(ChatColor.AQUA + "Loading metrics");
        new Metrics(this, 8968);

        log.info(ChatColor.AQUA + "Done! :D");
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
        config = manager.getConfig();
        icons = manager.getIcons();
    }

    @Override
    public String getVersion() {
        return getDescription().getVersion();
    }

    @Override
    public void info(String message) {
        getLogger().info(message);
    }

    @Override
    public void warn(String message) {
        getLogger().warning(message);
    }

    @Override
    public void error(String message) {
        getLogger().severe(message);
    }
}
