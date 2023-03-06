package net.pistonmaster.pistonmotd.api;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.pistonmaster.pistonmotd.kyori.PistonSerializersRelocated;
import org.apiguardian.api.API;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class for managing parsers and parse text.
 */
@SuppressWarnings({"unused"})
public class PlaceholderUtil {
    private static final List<WeakReference<PlaceholderParser>> placeholders = new CopyOnWriteArrayList<>();

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

        for (WeakReference<PlaceholderParser> weakReference : placeholders) {
            PlaceholderParser parser = weakReference.get();

            if (parser == null) {
                placeholders.remove(weakReference);
                continue;
            }

            parsedText = parser.parseString(parsedText);
        }

        return PistonSerializersRelocated.unusualSectionRGB.serialize(PistonSerializersRelocated.ampersandRGB.deserialize(PistonSerializersRelocated.unusualSectionRGB.serialize(MiniMessage.miniMessage().deserialize(parsedText))));
    }

    /**
     * Register a parser to make him parse some placeholders
     *
     * @param parser A parser to register
     */
    @API(status = API.Status.STABLE)
    public static void registerParser(PlaceholderParser parser) {
        placeholders.add(new WeakReference<>(parser));
    }

    /**
     * Unregister a parser to stop him from parsing
     *
     * @param parser The parser to unregister
     */
    @API(status = API.Status.STABLE)
    public static void unregisterParser(PlaceholderParser parser) {
        placeholders.removeIf(w -> w.get() == parser);
    }
}
