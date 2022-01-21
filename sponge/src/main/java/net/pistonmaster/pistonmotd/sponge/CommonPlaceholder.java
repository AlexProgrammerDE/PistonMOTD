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
        text = text.replace("%online%", String.valueOf(game.server().onlinePlayers().size()));
        text = text.replace("%max%", String.valueOf(game.server().maxPlayers()));
        text = text.replace("%newline%", "\n");

        return text;
    }
}
