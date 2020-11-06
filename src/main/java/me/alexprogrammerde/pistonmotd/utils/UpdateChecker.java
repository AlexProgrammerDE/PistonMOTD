package me.alexprogrammerde.pistonmotd.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class UpdateChecker {
    private final Object log;

    public UpdateChecker(Object log) {
        this.log = log;
    }

    public void getVersion(final Consumer<String> consumer) {
        try (InputStream inputStream = new URL("https://www.pistonmaster.net/PistonMOTD/VERSION.txt").openStream(); Scanner scanner = new Scanner(inputStream)) {
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
            }
        } catch (IOException exception) {
            if (log instanceof Logger) {
                ((Logger) log).info(ConsoleColor.RED_BOLD + "Cannot look for updates: " + exception.getMessage() + ConsoleColor.RESET);
            } else if (log instanceof org.slf4j.Logger) {
                ((org.slf4j.Logger) log).info(ConsoleColor.RED_BOLD + "Cannot look for updates: " + exception.getMessage() + ConsoleColor.RESET);
            }
        }
    }
}