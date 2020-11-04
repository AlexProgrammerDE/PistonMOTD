package me.alexprogrammerde.pistonmotd.utils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UpdateParser {
    private final String currentV;
    private final String spigotV;

    public UpdateParser(@Nonnull String currentV, @Nonnull String spigotV) {
        this.currentV = currentV;
        this.spigotV = spigotV;
    }

    public void parseUpdate(Consumer<UpdateType> consumer) {
        SpigotVersion spigot = new SpigotVersion();
        CurrentVersion current = new CurrentVersion();

        // 0 = major, 1 = minor, 2 = patch
        if (spigot.MAJOR > current.MAJOR) {
            consumer.accept(UpdateType.MAJOR);
        } else if (spigot.MINOR > current.MINOR) {
            consumer.accept(UpdateType.MINOR);
        } else if (spigot.PATCH > current.PATCH) {
            consumer.accept(UpdateType.PATCH);
        } else {
            consumer.accept(UpdateType.NONE);
        }
    }

    private class SpigotVersion {
        public SpigotVersion() {
            String[] spigotArr = spigotV.split("\\.");
            List<Integer> spigotList = new ArrayList<>();

            for (String str : spigotArr) {
                spigotList.add(Integer.parseInt(str));
            }

            MAJOR = spigotList.get(0);

            MINOR = spigotList.get(1);

            PATCH = spigotList.get(2);
        }

        public final int MAJOR;

        public final int MINOR;

        public final int PATCH;
    }

    private class CurrentVersion {
        public CurrentVersion() {
            String[] currentArr = currentV.split("\\.");
            List<Integer> currentList = new ArrayList<>();

            for (String str : currentArr) {
                currentList.add(Integer.parseInt(str));
            }

            MAJOR = currentList.get(0);

            MINOR = currentList.get(1);

            PATCH = currentList.get(2);
        }

        public final int MAJOR;

        public final int MINOR;

        public final int PATCH;
    }
}
