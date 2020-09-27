package me.alexprogrammerde.PistonMOTD.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BungeeCommand extends Command implements TabExecutor {
    private final String[] COMMANDS = { "reload", "help" };
    private final PistonMOTDBungee plugin;

    public BungeeCommand(PistonMOTDBungee plugin, String name) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (((args.length > 0 && args[0].equalsIgnoreCase("help")) || args.length == 0) && sender.hasPermission("pistonmotd.help")) {
            sender.sendMessage(new ComponentBuilder("Commands:").create());
            sender.sendMessage(new ComponentBuilder("/pistonmotd help").create());
            sender.sendMessage(new ComponentBuilder("/pistonmotd reload").create());
        } else if (args.length > 0 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("pistonmotd.reload")) {
            plugin.reloadConfiguration();
            sender.sendMessage(new ComponentBuilder("Reloaded the config!").create());
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (sender.hasPermission("pistonmotd.reload") || sender.hasPermission("pistonmotd.help")) {
            List<String> completions = new ArrayList<>();

            if (args.length == 1 && args[0] != null) {
                for (String string : COMMANDS) {
                    if (string.toLowerCase().startsWith(args[0].toLowerCase())) {
                        completions.add(string);

                        Collections.sort(completions);
                    }
                }
            }

            return completions;
        }

        return null;
    }
}
