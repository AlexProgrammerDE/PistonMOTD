package me.alexprogrammerde.pistonmotd.api;

import org.apiguardian.api.API;

/**
 * Interface to allow a class to get used as a parser.
 */
public interface PlaceholderParser {
    /**
     * Parse the placeholders of a string
     * @param text Current string
     * @return Parsed string
     */
    @API(status = API.Status.STABLE)
    String parseString(String text);
}
