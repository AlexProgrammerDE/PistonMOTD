package net.pistonmaster.pistonmotd.shared.utils;

import net.pistonmaster.pistonmotd.api.PlaceholderUtil;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PMHelpers {
    public static final String AFTER_ICON = "                                                                            ";
    public static final int MINECRAFT_1_16 = 735;

    public static String getMOTDJson(List<String> motds, boolean supportsHex) {
        String unparsedMOTD = getRandomEntry(motds);
        String[] split = unparsedMOTD.split("%nohexmotd%", 2);
        return PlaceholderUtil.parseTextToJson(
                split.length == 2 ? supportsHex ? split[0] : split[1] : split[0],
                supportsHex);
    }

    public static <T extends Enum<T>> T getSafeEnum(Class<T> enumClass, String name) {
        try {
            return Enum.valueOf(enumClass, name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static <T> T getRandomEntry(List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    public static <E> E getRandomEntry(Collection<E> list) {
        int index = ThreadLocalRandom.current().nextInt(list.size());
        int i = 0;
        for (E entry : list) {
            if (i == index) {
                return entry;
            }
            i++;
        }

        throw new IllegalStateException("Failed to get random entry");
    }
}
