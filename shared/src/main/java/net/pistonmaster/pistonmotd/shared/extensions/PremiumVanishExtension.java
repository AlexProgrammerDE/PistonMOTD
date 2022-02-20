package net.pistonmaster.pistonmotd.shared.extensions;

import java.util.List;
import java.util.UUID;
import de.myzelyam.api.vanish.*;

public class PremiumVanishExtension {
    public static List<UUID> getVanishedPlayers() {
        return BungeeVanishAPI.getInvisiblePlayers();
    }
}
