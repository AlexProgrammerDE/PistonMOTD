package me.alexprogrammerde.PistonMOTD.utils;

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
        String[] currentArr = currentV.split("\\.");
        String[] spigotArr = spigotV.split("\\.");
        List<Integer> currentList = new ArrayList<>();
        List<Integer> spigotList = new ArrayList<>();

        for (String str : currentArr) {
            currentList.add(Integer.parseInt(str));
        }

        for (String str : spigotArr) {
            spigotList.add(Integer.parseInt(str));
        }

        // 0 = major, 1 = minor, 2 = patch
        if (spigotList.get(0) > currentList.get(0)) {
            consumer.accept(UpdateType.MAJOR);
        } else if (spigotList.get(1) > currentList.get(1)) {
            consumer.accept(UpdateType.MINOR);
        } else if (spigotList.get(2) > currentList.get(2)) {
            consumer.accept(UpdateType.PATCH);
        } else {
            consumer.accept(UpdateType.NONE);
        }
    }
}
