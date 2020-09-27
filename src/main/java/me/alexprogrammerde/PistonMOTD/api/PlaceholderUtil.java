package me.alexprogrammerde.PistonMOTD.api;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PlaceholderUtil {
    private static final List<PlaceholderParser> placeholders = new ArrayList<>();

    /**
     * Parse a string and run all parsers on him
     * @param text A string with placeholders to get parsed
     * @return A completely parsed string
     */
    public static String parseText(String text) {
        String returnedstring = text;

        for (PlaceholderParser parser : placeholders) {
            returnedstring = parser.parseString(returnedstring);
        }

        return returnedstring;
    }

    /**
     * Register a parser to make him parse some placeholders
     * @param parser A parser to register
     */
    public static void registerParser(@Nonnull PlaceholderParser parser) {
        placeholders.add(parser);
    }

    /**
     * Unregister a parser to stop him from parsing
     * @param parser The parser to unregister
     */
    public static void unregisterParser(@Nonnull PlaceholderParser parser) {
        placeholders.remove(parser);
    }
}
