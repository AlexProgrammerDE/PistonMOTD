package me.alexprogrammerde.PistonMOTD.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class UpdateChecker {
    private final Logger log;
    private final int resourceId;

    public UpdateChecker(Logger log, int resourceId) {
        this.log = log;
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId + "/").openStream(); Scanner scanner = new Scanner(inputStream)) {
            if (scanner.hasNext()) {
                String returnedString = scanner.next();

                consumer.accept(returnedString);
            }
        } catch (IOException exception) {
            log.info("Cannot look for updates: " + exception.getMessage());
        }
    }
}