package net.pistonmaster.pistonmotd.shared;

import lombok.RequiredArgsConstructor;
import net.pistonmaster.pistonmotd.api.PlaceholderParser;

@RequiredArgsConstructor
public class CommonPlaceholder implements PlaceholderParser {
    private final PistonMOTDPlatform plugin;

    @Override
    public String parseString(String text) {
        text = text.replace("%online%", String.valueOf(plugin.getPlayerCount()));
        text = text.replace("%max%", String.valueOf(plugin.getMaxPlayers()));
        text = text.replace("%newline%", "\n");

        return text;
    }
}
