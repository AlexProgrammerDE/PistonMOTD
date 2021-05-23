package net.pistonmaster.pistonmotd.sponge;

import net.pistonmaster.pistonmotd.api.PlaceholderParser;
import org.spongepowered.api.Game;

public class CommonPlaceholder implements PlaceholderParser {
    private final Game game;

    protected CommonPlaceholder(Game game) {
        this.game = game;
    }

    @Override
    public String parseString(String text) {
        String returnedString = text;
        returnedString = returnedString.replace("%online%", String.valueOf(game.getServer().getOnlinePlayers().size()));
        returnedString = returnedString.replace("%max%", String.valueOf(game.getServer().getMaxPlayers()));
        returnedString = returnedString.replace("%newline%", "\n");

        return returnedString;
    }
}
