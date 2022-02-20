package net.pistonmaster.pistonmotd.shared.extensions;

import de.myzelyam.api.vanish.VanishAPI;

import java.util.List;
import java.util.UUID;

public class SuperVanishExtension {
    public static List<UUID> getVanishedPlayers() {
        return VanishAPI.getInvisiblePlayers();
    }
}
