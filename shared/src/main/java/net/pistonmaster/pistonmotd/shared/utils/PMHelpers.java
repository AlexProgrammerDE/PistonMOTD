package net.pistonmaster.pistonmotd.shared.utils;

import net.pistonmaster.pistonmotd.api.PlaceholderUtil;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PMHelpers {
    public static final int MINECRAFT_1_16 = 735;

    public static String getMOTDJson(List<String> motds, boolean supportsHex) {
        String unparsedMOTD = motds.get(ThreadLocalRandom.current().nextInt(motds.size()));
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
}
