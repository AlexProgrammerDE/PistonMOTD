package net.pistonmaster.pistonmotd.bukkit;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.pistonmaster.pistonmotd.shared.PistonStatusPing;
import net.pistonmaster.pistonmotd.shared.StatusFavicon;
import net.pistonmaster.pistonmotd.shared.StatusPingHandler;
import net.pistonmaster.pistonmotd.shared.utils.PMHelpers;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.CachedServerIcon;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@SuppressWarnings({"removal", "deprecation"})
public class PingEventPaper implements Listener {
  public static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
    .character('§')
    .hexCharacter('#')
    .hexColors()
    .build();
  private final StatusPingHandler handler;

  @EventHandler
  public void onPing(PaperServerListPingEvent event) {
    handler.handle(wrap(event));
  }

  private PistonStatusPing wrap(PaperServerListPingEvent event) {
    return new PistonStatusPing() {
      @Override
      public void hidePlayers() {
        event.setHidePlayers(true);
      }

      @Override
      public String getDescriptionJson() {
        return GsonComponentSerializer.gson().serialize(LEGACY_COMPONENT_SERIALIZER.deserialize(event.getMotd()));
      }

      @Override
      public void setDescription(String descriptionJson) {
        event.setMotd(LEGACY_COMPONENT_SERIALIZER.serialize(GsonComponentSerializer.gson().deserialize(descriptionJson)));
      }

      @Override
      public int getMax() {
        return event.getMaxPlayers();
      }

      @Override
      public void setMax(int max) {
        event.setMaxPlayers(max);
      }

      @Override
      public int getOnline() {
        return event.getNumPlayers();
      }

      @Override
      public void setOnline(int online) {
        event.setNumPlayers(online);
      }

      @Override
      public String getVersionName() {
        return event.getVersion();
      }

      @Override
      public void setVersionName(String name) {
        event.setVersion(name);
      }

      @Override
      public int getVersionProtocol() {
        return event.getProtocolVersion();
      }

      @Override
      public void setVersionProtocol(int protocol) {
        event.setProtocolVersion(protocol);
      }

      @Override
      public void clearSamples() {
        try {
          event.getListedPlayers().clear();
        } catch (Throwable ignored) {
          event.getPlayerSample().clear();
        }
      }

      @Override
      public void addSample(UUID uuid, String name) {
        try {
          event.getListedPlayers().add(new PaperServerListPingEvent.ListedPlayerInfo(name, uuid));
        } catch (Throwable ignored) {
          event.getPlayerSample().add(Bukkit.createProfile(uuid, name));
        }
      }

      @Override
      public boolean supportsHex() {
        return getClientProtocol() == -1 || getClientProtocol() >= PMHelpers.MINECRAFT_1_16;
      }

      @Override
      public void setFavicon(StatusFavicon favicon) {
        event.setServerIcon((CachedServerIcon) favicon.getValue());
      }

      @Override
      public int getClientProtocol() {
        return event.getClient().getProtocolVersion();
      }

      @Override
      public Optional<InetSocketAddress> getClientVirtualHost() {
        return Optional.ofNullable(event.getClient().getVirtualHost());
      }
    };
  }
}
