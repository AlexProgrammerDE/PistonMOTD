package net.pistonmaster.pistonmotd.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BungeeCommand extends Command implements TabExecutor {
    private final PistonMOTDBungee plugin;

    protected BungeeCommand(PistonMOTDBungee plugin) {
        super("pistonmotd", null, "pistonmotdb", "pistonmotdbungee");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (((args.length > 0 && args[0].equalsIgnoreCase("help")) || args.length == 0) && sender.hasPermission("pistonmotd.help")) {
            sender.sendMessage(new ComponentBuilder("Commands:").create());
            sender.sendMessage(new ComponentBuilder("/pistonmotd help").create());
            sender.sendMessage(new ComponentBuilder("/pistonmotd reload").create());
        } else if (args.length > 0 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("pistonmotd.reload")) {
            plugin.getPlugin().loadConfig();
            sender.sendMessage(new ComponentBuilder("Reloaded the config!").create());
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> commands = new ArrayList<>();

        if (sender.hasPermission("pistonmotd.help")) {
            commands.add("help");
        }

        if (sender.hasPermission("pistonmotd.reload")) {
            commands.add("reload");
        }

        List<String> completions = new ArrayList<>();

        if (!commands.isEmpty() && args.length == 1 && args[0] != null) {
            for (String string : commands) {
                if (string.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(string);
                }
            }
        }

        Collections.sort(completions);

        return completions;
    }
}
