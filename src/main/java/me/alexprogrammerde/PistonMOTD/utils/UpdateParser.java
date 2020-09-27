package me.alexprogrammerde.PistonMOTD.utils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UpdateParser {
    String currentv;
    String spigotv;

    public UpdateParser(@Nonnull String currentv, @Nonnull String spigotv) {
        this.currentv = currentv;
        this.spigotv = spigotv;
    }

    public void parseUpdate(Consumer<UpdateType> consumer) {
        String[] currentarr = currentv.split("\\.");
        String[] spigotarr = spigotv.split("\\.");
        List<Integer> currentlist = new ArrayList<>();
        List<Integer> spigotlist = new ArrayList<>();

        for (String str : currentarr) {
            currentlist.add(Integer.parseInt(str));
        }

        for (String str : spigotarr) {
            spigotlist.add(Integer.parseInt(str));
        }

        // 0 = major, 1 = minor, 2 = patch
        if (spigotlist.get(0) > currentlist.get(0)) {
            consumer.accept(UpdateType.MAJOR);
        } else if (spigotlist.get(1) > currentlist.get(1)) {
            consumer.accept(UpdateType.MINOR);
        } else if (spigotlist.get(2) > currentlist.get(2)) {
            consumer.accept(UpdateType.PATCH);
        } else {
            consumer.accept(UpdateType.NONE);
        }
    }
}
