package me.alexprogrammerde.pistonmotd.sponge;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

public class JoinEvent {
    private final PistonMOTDSponge plugin;

    protected JoinEvent(PistonMOTDSponge plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void onJoin(ClientConnectionEvent temp) {
        if (temp instanceof ClientConnectionEvent.Join) {
            ClientConnectionEvent.Join event = (ClientConnectionEvent.Join) temp;
            if (event.getTargetEntity().hasPermission("sponge.command.metrics") && !plugin.hasConsent()) {
                event.getTargetEntity().sendMessage(Text.builder("-----[ PistonMOTD ]-----").color(TextColors.GOLD).onClick(TextActions.runCommand("/sponge metrics pistonmotd enable")).build());
                event.getTargetEntity().sendMessage(Text.builder("Hey there! It seems like data collection is disabled. :( ").color(TextColors.GOLD).onClick(TextActions.runCommand("/sponge metrics pistonmotd enable")).build());
                event.getTargetEntity().sendMessage(Text.builder("But don't worry... You can fix this! ").color(TextColors.GOLD).onClick(TextActions.runCommand("/sponge metrics pistonmotd enable")).build());
                event.getTargetEntity().sendMessage(Text.builder("Just click this message to enable it or just execute:").color(TextColors.GOLD).onClick(TextActions.runCommand("/sponge metrics pistonmotd enable")).build());
                event.getTargetEntity().sendMessage(Text.builder("\"/sponge metrics pistonmotd enable\".").color(TextColors.GOLD).onClick(TextActions.runCommand("/sponge metrics pistonmotd enable")).build());
                event.getTargetEntity().sendMessage(Text.builder("This info is just to give me small info about the server,").color(TextColors.GOLD).onClick(TextActions.runCommand("/sponge metrics pistonmotd enable")).build());
                event.getTargetEntity().sendMessage(Text.builder("like its version and the plugin version.").color(TextColors.GOLD).onClick(TextActions.runCommand("/sponge metrics pistonmotd enable")).build());
                event.getTargetEntity().sendMessage(Text.builder("-----[ PistonMOTD ]-----").color(TextColors.GOLD).onClick(TextActions.runCommand("/sponge metrics pistonmotd enable")).build());
            }
        }
    }
}
