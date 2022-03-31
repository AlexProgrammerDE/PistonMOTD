package net.pistonmaster.pistonmotd.sponge;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;

@RequiredArgsConstructor
public class SpongeReloadCommand implements CommandExecutor {
    private final PistonMOTDSponge plugin;

    @Override
    public CommandResult execute(CommandContext context) {
        if (context.subject().hasPermission("pistonmotd.reload")) {
            plugin.getPlugin().loadConfig();

            context.sendMessage(Identity.nil(), Component.text("Reloaded the config!"));

            return CommandResult.success();
        } else {
            return CommandResult.error(Component.text("You don't have permission to do that!"));
        }
    }
}
