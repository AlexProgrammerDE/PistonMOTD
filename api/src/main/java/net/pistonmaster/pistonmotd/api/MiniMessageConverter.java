package net.pistonmaster.pistonmotd.api;

import org.jetbrains.annotations.ApiStatus;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

@ApiStatus.Internal
public class MiniMessageConverter {
  private static final Map<String, String> COLOR_MAP = Map.ofEntries(
    Map.entry("0", "black"),
    Map.entry("1", "dark_blue"),
    Map.entry("2", "dark_green"),
    Map.entry("3", "dark_aqua"),
    Map.entry("4", "dark_red"),
    Map.entry("5", "dark_purple"),
    Map.entry("6", "gold"),
    Map.entry("7", "gray"),
    Map.entry("8", "dark_gray"),
    Map.entry("9", "blue"),
    Map.entry("a", "green"),
    Map.entry("b", "aqua"),
    Map.entry("c", "red"),
    Map.entry("d", "light_purple"),
    Map.entry("e", "yellow"),
    Map.entry("f", "white")
  );
  private static final Map<String, String> FORMATTING_MAP = Map.ofEntries(
    Map.entry("k", "obfuscated"),
    Map.entry("l", "bold"),
    Map.entry("m", "strikethrough"),
    Map.entry("n", "underlined"),
    Map.entry("o", "italic"),
    Map.entry("r", "reset")
  );
  private static final Pattern SECTION_HEX_PATTERN = Pattern.compile("§#([0-9a-fA-F]{6})");
  private static final Pattern AMPERSAND_HEX_PATTERN = Pattern.compile("&#([0-9a-fA-F]{6})");
  private static final Pattern SECTION_HEX_UGLY_PATTERN = Pattern.compile("§x(§[0-9a-fA-F]){6}");
  private static final Pattern AMPERSAND_HEX_UGLY_PATTERN = Pattern.compile("&x(&[0-9a-fA-F]){6}");

  public static String convertMiniMessageString(String str) {
    str = convertMiniMessageString(str, '&', AMPERSAND_HEX_PATTERN, AMPERSAND_HEX_UGLY_PATTERN);
    str = convertMiniMessageString(str, '§', SECTION_HEX_PATTERN, SECTION_HEX_UGLY_PATTERN);

    return str;
  }

  private static String convertMiniMessageString(String str, char legacyChar, Pattern hexPattern, Pattern hexUglyPattern) {
    {
      // Convert hex colors
      var hexMatcher = hexPattern.matcher(str);
      StringBuilder sb = new StringBuilder();
      while (hexMatcher.find()) {
        String hex = hexMatcher.group(1).toUpperCase(Locale.ROOT);
        hexMatcher.appendReplacement(sb, "<reset><#" + hex + ">");
      }
      hexMatcher.appendTail(sb);
      str = sb.toString();
    }

    {
      // Convert ugly hex colors (handles patterns like &x&F&F&0&0&0&0 or §x§F§F§0§0§0§0)
      var uglyHexMatcher = hexUglyPattern.matcher(str);
      StringBuilder sb = new StringBuilder();
      while (uglyHexMatcher.find()) {
        StringBuilder hex = new StringBuilder();
        String match = uglyHexMatcher.group();
        // Collect only hexadecimal characters from the match (0-9, a-f, A-F)
        for (int i = 0; i < match.length(); i++) {
          char c = match.charAt(i);
          if (Character.digit(c, 16) != -1) {
            hex.append(c);
          }
        }
        uglyHexMatcher.appendReplacement(sb, "<reset><#" + hex.toString().toUpperCase(Locale.ROOT) + ">");
      }
      uglyHexMatcher.appendTail(sb);
      str = sb.toString();
    }

    // Convert standard colors
    StringBuilder result = new StringBuilder();
    char[] chars = str.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      if (chars[i] == legacyChar && i + 1 < chars.length) {
        String code = String.valueOf(chars[i + 1]).toLowerCase(Locale.ROOT);
        String colorReplacement = COLOR_MAP.get(code);
        if (colorReplacement != null) {
          result.append("<reset>").append("<").append(colorReplacement).append(">");
          i++; // Skip the next character as it's part of the color code
          continue;
        } else {
          String formattingReplacement = FORMATTING_MAP.get(code);
          if (formattingReplacement != null) {
            result.append("<").append(formattingReplacement).append(">");
            i++; // Skip the next character as it's part of the formatting code
            continue;
          }
        }
      }
      result.append(chars[i]);
    }

    return result.toString();
  }
}
