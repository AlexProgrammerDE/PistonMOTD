package net.pistonmaster.pistonmotd.sponge;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.util.Tristate;

@RequiredArgsConstructor
public class JoinEvent {
    private final PistonMOTDSponge plugin;

    @Listener
    public void onJoin(ServerSideConnectionEvent temp) {
        if (temp instanceof ServerSideConnectionEvent.Join) {
            ServerSideConnectionEvent.Join event = (ServerSideConnectionEvent.Join) temp;
            if (event.player().hasPermission("sponge.command.metrics") && plugin.getEffectiveCollectionState() == Tristate.UNDEFINED) {
                event.player().sendMessage(Component.text("-----[ PistonMOTD ]-----").color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand("/sponge metrics pistonmotd enable")));
                event.player().sendMessage(Component.text("Hey there! It seems like data collection is disabled. :( ").color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand("/sponge metrics pistonmotd enable")));
                event.player().sendMessage(Component.text("But don't worry... You can fix this! ").color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand("/sponge metrics pistonmotd enable")));
                event.player().sendMessage(Component.text("Just click this message to enable it or just execute:").color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand("/sponge metrics pistonmotd enable")));
                event.player().sendMessage(Component.text("\"/sponge metrics pistonmotd enable\".").color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand("/sponge metrics pistonmotd enable")));
                event.player().sendMessage(Component.text("This info is just to give me small info about the server,").color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand("/sponge metrics pistonmotd enable")));
                event.player().sendMessage(Component.text("like its version and the plugin version.").color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand("/sponge metrics pistonmotd enable")));
                event.player().sendMessage(Component.text("-----[ PistonMOTD ]-----").color(NamedTextColor.GOLD).clickEvent(ClickEvent.runCommand("/sponge metrics pistonmotd enable")));
            }
        }
    }
}
