package net.pistonmaster.pistonmotd.shared.utils;

import java.util.regex.Pattern;

public final class HostnameUtils {
  private HostnameUtils() {
  }

  /**
   * Checks if a hostname matches a given pattern.
   * Supports wildcards using '*' (e.g., '*.example.com').
   *
   * @param hostname The hostname to check.
   * @param pattern  The pattern to match against.
   * @return true if the hostname matches the pattern.
   */
  public static boolean matches(String hostname, String pattern) {
    if (hostname == null || pattern == null) {
      return false;
    }
    if (hostname.equals(pattern)) {
      return true;
    }
    String regex = pattern.replace(".", "\\.");
    regex = regex.replace("*", ".*");
    return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(hostname).matches();
  }
}
