package net.pistonmaster.pistonmotd.sponge;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.util.Tristate;

@RequiredArgsConstructor
public class JoinEvent {
    private final PistonMOTDSponge plugin;

    @Listener
    public void onJoin(ServerSideConnectionEvent temp) {
      if (!(temp instanceof ServerSideConnectionEvent.Join event)) {
        return;
      }

      ServerPlayer player = event.player();
      if (player.hasPermission("sponge.command.metrics") && plugin.getEffectiveCollectionState() == Tristate.UNDEFINED) {
          send(player, "-----[ PistonMOTD ]-----");
          send(player, "Hey there! It seems like data collection is disabled. :(");
          send(player, "But you can enable it!");
          send(player, "Please enable it in this file: <server>/config/sponge/metrics.conf");
          send(player, "This only collects small metadata about the server,");
          send(player, "like its version and the plugin version.");
          send(player, "-----[ PistonMOTD ]-----");
      }
    }

    private void send(ServerPlayer player, String str) {
        player.sendMessage(Component.text(str).color(NamedTextColor.GOLD));
    }
}
