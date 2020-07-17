package me.alexprogrammerde.BungeePing;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatusCommand extends Command implements TabExecutor {
    private static final String[] COMMANDS = { "reload" };
    private final Main plugin;

    public StatusCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
        plugin = Main.plugin;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length > 0 && args[0].equals("reload") && commandSender.hasPermission("bungeestatus.reload")) {
            plugin.reloadConfiguration();
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
