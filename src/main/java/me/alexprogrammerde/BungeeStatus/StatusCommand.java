package me.alexprogrammerde.BungeeStatus;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatusCommand extends Command implements TabExecutor {
    private static final String[] COMMANDS = { "reload" };
    private final BungeeStatus plugin;

    public StatusCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
        plugin = BungeeStatus.plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 0 && args[0].equals("reload") && sender.hasPermission("bungeestatus.reload")) {
            plugin.reloadConfiguration();
            sender.sendMessage(new ComponentBuilder("Reloaded the config!").create());
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        if (commandSender.hasPermission("bungeestatus.reload")) {
            final List<String> completions = new ArrayList<>();

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
