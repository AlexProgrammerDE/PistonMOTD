package net.pistonmaster.pistonmotd.shared.extensions;

import de.myzelyam.api.vanish.BungeeVanishAPI;
import de.myzelyam.api.vanish.VanishAPI;
import de.myzelyam.api.vanish.VelocityVanishAPI;

import java.util.List;
import java.util.UUID;

public class VanishAPIExtension {
  public static List<UUID> getVanishedPlayersBukkit() {
    return VanishAPI.getInvisiblePlayers();
  }

  public static List<UUID> getVanishedPlayersBungee() {
    return BungeeVanishAPI.getInvisiblePlayers();
  }

  public static List<UUID> getVanishedPlayersVelocity() {
    return VelocityVanishAPI.getInvisiblePlayers();
  }

  private VanishAPIExtension() {
  }
}
