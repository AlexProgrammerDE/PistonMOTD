package net.pistonmaster.pistonmotd.bukkit;

import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class BukkitCommand implements CommandExecutor, TabExecutor {
    private final PistonMOTDBukkit plugin;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String s, String[] args) {
        if (((args.length > 0 && args[0].equalsIgnoreCase("help")) || args.length == 0) && sender.hasPermission("pistonmotd.help")) {
            sender.spigot().sendMessage(new ComponentBuilder("Commands:").create());
            sender.spigot().sendMessage(new ComponentBuilder("/pistonmotd help").create());
            sender.spigot().sendMessage(new ComponentBuilder("/pistonmotd reload").create());
            return true;
        } else if (args.length > 0 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("pistonmotd.reload")) {
            plugin.getPlugin().loadConfig();
            sender.spigot().sendMessage(new ComponentBuilder("Reloaded the config!").create());
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        List<String> commands = new ArrayList<>();

        if (sender.hasPermission("pistonmotd.help")) {
            commands.add("help");
        }

        if (sender.hasPermission("pistonmotd.reload")) {
            commands.add("reload");
        }

        List<String> completions = new ArrayList<>();

        if (!commands.isEmpty() && args.length == 1 && args[0] != null) {
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }

        Collections.sort(completions);

        return completions;
    }
}
