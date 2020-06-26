package me.alexprogrammerde.BungeePing;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatusCommand extends Command {
    private static final String[] COMMANDS = { "reload" };
    private Main plugin;

    public StatusCommand(String name, Main plugin) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length >= 1 && args[0] == "reload" && commandSender.hasPermission("bungeestatus.reload")) {
            plugin.reloadConfiguration();
        }
    }

    /*@Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        if (commandSender.hasPermission("bungeestatus.reload")) {
            final List<String> completions = new ArrayList<>();

            try {
                StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return completions;
        }

        return null;
    }*/
}
