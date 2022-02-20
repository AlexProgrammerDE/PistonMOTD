package net.pistonmaster.pistonmotd.shared.extensions;

import de.myzelyam.api.vanish.BungeeVanishAPI;

import java.util.List;
import java.util.UUID;

public class PremiumVanishExtension {
    public static List<UUID> getVanishedPlayers() {
        return BungeeVanishAPI.getInvisiblePlayers();
    }
}
