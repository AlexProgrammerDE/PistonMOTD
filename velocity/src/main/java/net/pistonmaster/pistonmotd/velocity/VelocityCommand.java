package net.pistonmaster.pistonmotd.velocity;

import com.velocitypowered.api.command.SimpleCommand;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class VelocityCommand implements SimpleCommand {
    private final PistonMOTDPlugin plugin;

    @Override
    public void execute(Invocation invocation) {
        if (((invocation.arguments().length > 0 && invocation.arguments()[0].equalsIgnoreCase("help")) || invocation.arguments().length == 0) && invocation.source().hasPermission("pistonmotd.help")) {
            invocation.source().sendMessage(Identity.nil(), Component.text("Commands:"));
            invocation.source().sendMessage(Identity.nil(), Component.text("/pistonmotd help"));
            invocation.source().sendMessage(Identity.nil(), Component.text("/pistonmotd reload"));
        } else if (invocation.arguments().length > 0 && invocation.arguments()[0].equalsIgnoreCase("reload") && invocation.source().hasPermission("pistonmotd.reload")) {
            plugin.loadConfig();
            invocation.source().sendMessage(Identity.nil(), Component.text("Reloaded the config!"));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] commands = {"reload", "help"};

        List<String> completions = new ArrayList<>();

        if (invocation.arguments().length == 1 && invocation.arguments()[0] != null) {
            for (String string : commands) {
                if (string.toLowerCase(Locale.ROOT).startsWith(invocation.arguments()[0].toLowerCase(Locale.ROOT))) {
                    completions.add(string);
                }
            }
        }

        Collections.sort(completions);

        return completions;
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("pistonmotd.reload") || invocation.source().hasPermission("pistonmotd.help");
    }
}
