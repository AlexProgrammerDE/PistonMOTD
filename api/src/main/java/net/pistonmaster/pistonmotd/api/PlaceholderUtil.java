package net.pistonmaster.pistonmotd.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.pistonmaster.pistonmotd.kyori.PistonSerializersRelocated;
import org.apiguardian.api.API;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

/**
 * Class for managing parsers and parse text.
 */
@SuppressWarnings({"unused"})
public class PlaceholderUtil {
    private static final Pattern LEGACY_HEX_PATTERN = Pattern.compile("[§&]#(?<hex>[0-9a-fA-F]{6})");
    private static final Pattern LEGACY_UGLY_HEX_PATTERN = Pattern.compile("([§&])x\\1(?<h1>[0-9a-fA-F])\\1(?<h2>[0-9a-fA-F])\\1(?<h3>[0-9a-fA-F])\\1(?<h4>[0-9a-fA-F])\\1(?<h5>[0-9a-fA-F])\\1(?<h6>[0-9a-fA-F])");
    private static final Map<Character, String> MINIMESSAGE_REPLACEMENTS;
    private static final List<PlaceholderParser> preParsePlaceholders = new CopyOnWriteArrayList<>();
    private static final List<PlaceholderParser> postParsePlaceholders = new CopyOnWriteArrayList<>();

    static {
        MINIMESSAGE_REPLACEMENTS = new HashMap<>();
        MINIMESSAGE_REPLACEMENTS.put('0', "black");
        MINIMESSAGE_REPLACEMENTS.put('1', "dark_blue");
        MINIMESSAGE_REPLACEMENTS.put('2', "dark_green");
        MINIMESSAGE_REPLACEMENTS.put('3', "dark_aqua");
        MINIMESSAGE_REPLACEMENTS.put('4', "dark_red");
        MINIMESSAGE_REPLACEMENTS.put('5', "dark_purple");
        MINIMESSAGE_REPLACEMENTS.put('6', "gold");
        MINIMESSAGE_REPLACEMENTS.put('7', "gray");
        MINIMESSAGE_REPLACEMENTS.put('8', "dark_gray");
        MINIMESSAGE_REPLACEMENTS.put('9', "blue");
        MINIMESSAGE_REPLACEMENTS.put('a', "green");
        MINIMESSAGE_REPLACEMENTS.put('b', "aqua");
        MINIMESSAGE_REPLACEMENTS.put('c', "red");
        MINIMESSAGE_REPLACEMENTS.put('d', "light_purple");
        MINIMESSAGE_REPLACEMENTS.put('e', "yellow");
        MINIMESSAGE_REPLACEMENTS.put('f', "white");

        MINIMESSAGE_REPLACEMENTS.put('k', "obfuscated");
        MINIMESSAGE_REPLACEMENTS.put('l', "bold");
        MINIMESSAGE_REPLACEMENTS.put('m', "strikethrough");
        MINIMESSAGE_REPLACEMENTS.put('n', "underline");
        MINIMESSAGE_REPLACEMENTS.put('o', "italic");
        MINIMESSAGE_REPLACEMENTS.put('r', "reset");
    }

    private PlaceholderUtil() {
    }

    /**
     * Parse a string and run all parsers on it and convert to json string
     *
     * @param text A string with placeholders to get parsed
     * @return A completely parsed gson string
     */
    @API(status = API.Status.INTERNAL)
    public static String parseTextToJson(final String text, boolean supportsHex) {
        Component ampersandRGB = parseTextToComponent(text);

        // Serialize it to JSON
        GsonComponentSerializer serializer = supportsHex ? PistonSerializersRelocated.gsonSerializer : PistonSerializersRelocated.gsonDownSamplingSerializer;
        return serializer.serialize(ampersandRGB);
    }

    /**
     * Parse a string and run all parsers on it and convert to legacy string
     *
     * @param text A string with placeholders to get parsed
     * @return A completely parsed legacy string
     */
    @API(status = API.Status.INTERNAL)
    public static String parseTextToLegacy(final String text) {
        Component ampersandRGB = parseTextToComponent(text);

        // Serialize it to non-rgb section
        return PistonSerializersRelocated.section.serialize(ampersandRGB);
    }

    private static Component parseTextToComponent(final String text) {
        String parsedText = convertMiniMessageString(text);

        for (PlaceholderParser parser : preParsePlaceholders) {
            parsedText = parser.parseString(parsedText);
        }

        // Initially parse the text via MiniMessage
        Component component = PistonSerializersRelocated.miniMessage.deserialize(parsedText);

        // Parse it to an ampersand RGB string
        String ampersandRGB = PistonSerializersRelocated.ampersandRGB.serialize(component);

        for (PlaceholderParser parser : postParsePlaceholders) {
            ampersandRGB = parser.parseString(ampersandRGB);
        }

        // Also parse ampersands that were not parsed by MiniMessage
        return PistonSerializersRelocated.ampersandRGB.deserialize(ampersandRGB);
    }

    @VisibleForTesting
    @API(status = API.Status.INTERNAL)
    public static String convertMiniMessageString(String str) {
        str = LEGACY_HEX_PATTERN.matcher(str).replaceAll("<#${hex}>");
        str = LEGACY_UGLY_HEX_PATTERN.matcher(str).replaceAll("<#${h1}${h2}${h3}${h4}${h5}${h6}>");

        str = replaceLegacyWithMiniMessage("&", str);
        str = replaceLegacyWithMiniMessage("§", str);

        return str;
    }

    private static String replaceLegacyWithMiniMessage(String prefix, String str) {
        for (Map.Entry<Character, String> entry : MINIMESSAGE_REPLACEMENTS.entrySet()) {
            str = str.replace(prefix + entry.getKey(), "<" + entry.getValue() + ">");
        }

        return str;
    }

    /**
     * Register a parser to make him parse some placeholders before converting MiniMessage and ampersand RGB and section RGB
     *
     * @param parser A parser to register
     */
    @API(status = API.Status.STABLE)
    public static void registerParser(PlaceholderParser parser) {
        preParsePlaceholders.add(parser);
    }

    /**
     * Register a parser to make him parse some placeholders after the main parsing
     *
     * @param parser A parser to register
     */
    @API(status = API.Status.STABLE)
    public static void registerPostParser(PlaceholderParser parser) {
        postParsePlaceholders.add(parser);
    }

    /**
     * Unregister a parser to stop him from parsing
     *
     * @param parser The parser to unregister
     */
    @API(status = API.Status.STABLE)
    public static void unregisterParser(PlaceholderParser parser) {
        preParsePlaceholders.removeIf(listParser -> listParser == parser);
        postParsePlaceholders.removeIf(listParser -> listParser == parser);
    }
}
