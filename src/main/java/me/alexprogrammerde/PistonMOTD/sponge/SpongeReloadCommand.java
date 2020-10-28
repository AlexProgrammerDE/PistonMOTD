package me.alexprogrammerde.PistonMOTD.sponge;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

public class SpongeReloadCommand implements CommandExecutor {
    private final PistonMOTDSponge plugin;

    protected SpongeReloadCommand(PistonMOTDSponge plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        plugin.loadConfig();

        src.sendMessage(Text.of("Reloaded the config!"));

        return CommandResult.success();
    }
}
