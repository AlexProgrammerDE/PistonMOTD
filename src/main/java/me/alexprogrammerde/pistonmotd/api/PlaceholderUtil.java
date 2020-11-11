package me.alexprogrammerde.pistonmotd.api;

import me.alexprogrammerde.pistonmotd.utils.PistonSerializers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apiguardian.api.API;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to use to manage parsers and parse text.
 */
public class PlaceholderUtil {
    private static final List<PlaceholderParser> placeholders = new ArrayList<>();

    private PlaceholderUtil() {}

    /**
     * Parse a string and run all parsers on him
     * @param text A string with placeholders to get parsed
     * @return A completely parsed string
     */
    @API(status = API.Status.INTERNAL)
    public static String parseText(final String text) {
        String parsedText = text;

        for (PlaceholderParser parser : placeholders) {
            parsedText = parser.parseString(parsedText);
        }

        return parseColors(parsedText);
    }

    private static String parseColors(final String text) {
        return PistonSerializers.unusualSectionRGB.serialize(PistonSerializers.ampersandRGB.deserialize(PistonSerializers.unusualSectionRGB.serialize(MiniMessage.markdown().parse(text))));
    }

    /**
     * Register a parser to make him parse some placeholders
     * @param parser A parser to register
     */
    @API(status = API.Status.STABLE)
    public static void registerParser(@Nonnull PlaceholderParser parser) {
        placeholders.add(parser);
    }


    /**
     * Unregister a parser to stop him from parsing
     * @param parser The parser to unregister
     */
    @API(status = API.Status.STABLE)
    public static void unregisterParser(@Nonnull PlaceholderParser parser) {
        placeholders.remove(parser);
    }
}
