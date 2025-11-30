package net.pistonmaster.pistonmotd.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.pistonmaster.pistonmotd.kyori.PistonSerializersRelocated;
import org.apiguardian.api.API;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class for managing parsers and parse text.
 */
@SuppressWarnings({"unused"})
public class PlaceholderUtil {
  private static final char SPECIAL_KEY = (char) 127;
  private static final char TAG_START = '<';
  private static final char ESCAPE = '\\';
  private static final List<PlaceholderParser> preParsePlaceholders = new CopyOnWriteArrayList<>();
  private static final List<PlaceholderParser> postParsePlaceholders = new CopyOnWriteArrayList<>();

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
    String parsedText = MiniMessageConverter.convertMiniMessageString(text);
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

    return PistonSerializersRelocated.ampersandRGB.deserialize(ampersandRGB);
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
