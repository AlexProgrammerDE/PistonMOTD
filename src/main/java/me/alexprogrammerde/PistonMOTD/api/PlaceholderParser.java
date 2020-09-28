package me.alexprogrammerde.PistonMOTD.api;

public interface PlaceholderParser {
    /**
     * Parse the placeholders of a string
     * @param text Current string
     * @return Parsed string
     */
    String parseString(String text);
}
