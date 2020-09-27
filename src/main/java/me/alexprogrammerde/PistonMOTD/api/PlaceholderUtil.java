package me.alexprogrammerde.PistonMOTD.api;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PlaceholderUtil {
    private static final List<PlaceholderParser> placeholders = new ArrayList<>();

    public static String parseText(String text) {
        String returnedstring = text;

        for (PlaceholderParser parser : placeholders) {
            returnedstring = parser.parseString(returnedstring);
        }

        return returnedstring;
    }

    public static void registerParser(@Nonnull PlaceholderParser parser) {
        placeholders.add(parser);
    }

    public static void unregisterParser(@Nonnull PlaceholderParser parser) {
        placeholders.remove(parser);
    }
}
