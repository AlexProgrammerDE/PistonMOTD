package me.alexprogrammerde.pistonmotd.sponge;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;

public class SpongeHelpCommand implements CommandExecutor {
    @Override
    public @Nonnull CommandResult execute(CommandSource src, @Nonnull CommandContext args) {
        if (src.hasPermission("pistonmotd.reload")) {

            src.sendMessage(Text.of("Commands:"));
            src.sendMessage(Text.of("/pistonmotd help"));
            src.sendMessage(Text.of("/pistonmotd reload"));

            return CommandResult.success();
        } else {
            return CommandResult.empty();
        }
    }
}
