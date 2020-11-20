package me.alexprogrammerde.pistonmotd.utils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UpdateParser {
    private final String currentV;
    private final String webV;

    public UpdateParser(@Nonnull String currentV, @Nonnull String webV) {
        this.currentV = currentV;
        this.webV = webV;
    }

    public void parseUpdate(Consumer<UpdateType> consumer) {
        WebVersion web = new WebVersion();
        CurrentVersion current = new CurrentVersion();

        if (web.MAJOR > current.MAJOR) {
            consumer.accept(UpdateType.MAJOR);
        } else if (web.MAJOR < current.MAJOR) {
            consumer.accept(UpdateType.AHEAD);
        } else if (web.MINOR > current.MINOR) {
            consumer.accept(UpdateType.MINOR);
        } else if (web.MINOR < current.MINOR) {
            consumer.accept(UpdateType.AHEAD);
        } else if (web.PATCH > current.PATCH) {
            consumer.accept(UpdateType.PATCH);
        } else if (web.PATCH < current.PATCH) {
            consumer.accept(UpdateType.AHEAD);
        } else {
            consumer.accept(UpdateType.NONE);
        }
    }

    private class WebVersion {
        public WebVersion() {
            String[] webArr = webV.split("\\.");
            List<Integer> webList = new ArrayList<>();

            for (String str : webArr) {
                webList.add(Integer.parseInt(str));
            }

            MAJOR = webList.get(0);

            MINOR = webList.get(1);

            PATCH = webList.get(2);
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
