package me.alexprogrammerde.PistonMOTD.api;

/**
 * Interface to allow a class to get used as a parser.
 */
public interface PlaceholderParser {
    /**
     * Parse the placeholders of a string
     * @param text Current string
     * @return Parsed string
     */
    String parseString(String text);
}
