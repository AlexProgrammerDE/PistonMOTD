package net.pistonmaster.pistonmotd.sponge;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;

public class SpongeReloadCommand implements CommandExecutor {
    private final PistonMOTDSponge plugin;

    protected SpongeReloadCommand(PistonMOTDSponge plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nonnull CommandResult execute(CommandSource src, @Nonnull CommandContext args) {
        if (src.hasPermission("pistonmotd.reload")) {
            plugin.loadConfig();

            src.sendMessage(Text.of("Reloaded the config!"));

            return CommandResult.success();
        } else {
            return CommandResult.empty();
        }
    }
}
