package me.alexprogrammerde.PistonMOTD.sponge;

import me.alexprogrammerde.PistonMOTD.api.PlaceholderParser;
import org.spongepowered.api.Game;

public class CommonPlaceholder implements PlaceholderParser {
    Game game;

    public CommonPlaceholder(Game game) {
        this.game = game;
    }

    @Override
    public String parseString(String text) {
        String returnedString = text;
        returnedString = returnedString.replaceAll("%online%", String.valueOf(game.getServer().getOnlinePlayers().size()));
        returnedString = returnedString.replaceAll("%max%", String.valueOf(game.getServer().getMaxPlayers()));
        returnedString = returnedString.replaceAll("%newline%", "\n");

        returnedString = returnedString.replaceAll("&", "§");

        return returnedString;
    }
}
