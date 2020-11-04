package me.alexprogrammerde.pistonmotd.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class UpdateChecker {
    private final Logger log;

    public UpdateChecker(Logger log) {
        this.log = log;
    }

    public void getVersion(final Consumer<String> consumer) {
        try (InputStream inputStream = new URL("https://www.pistonmaster.net/PistonMOTD/VERSION.txt").openStream(); Scanner scanner = new Scanner(inputStream)) {
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
            }
        } catch (IOException exception) {
            log.info(ConsoleColor.RED_BOLD + "Cannot look for updates: " + exception.getMessage() + ConsoleColor.RESET);
        }
    }
}