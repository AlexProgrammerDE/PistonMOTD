package net.pistonmaster.pistonmotd.api;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.pistonmaster.pistonmotd.shared.utils.PistonSerializers;
import org.apiguardian.api.API;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to use to manage parsers and parse text.
 */
@SuppressWarnings({"unused"})
public class PlaceholderUtil {
    private static final List<PlaceholderParser> placeholders = new ArrayList<>();

    private PlaceholderUtil() {
    }

    /**
     * Parse a string and run all parsers on him
     *
     * @param text A string with placeholders to get parsed
     * @return A completely parsed string
     */
    @API(status = API.Status.INTERNAL)
    public static String parseText(final String text) {
        String parsedText = text;

        for (PlaceholderParser parser : placeholders) {
            parsedText = parser.parseString(parsedText);
        }

        return PistonSerializers.unusualSectionRGB.serialize(PistonSerializers.ampersandRGB.deserialize(PistonSerializers.unusualSectionRGB.serialize(MiniMessage.markdown().parse(parsedText))));
    }

    /**
     * Register a parser to make him parse some placeholders
     *
     * @param parser A parser to register
     */
    @API(status = API.Status.STABLE)
    public static void registerParser(PlaceholderParser parser) {
        placeholders.add(parser);
    }

    /**
     * Unregister a parser to stop him from parsing
     *
     * @param parser The parser to unregister
     */
    @API(status = API.Status.STABLE)
    public static void unregisterParser(PlaceholderParser parser) {
        placeholders.remove(parser);
    }
}
