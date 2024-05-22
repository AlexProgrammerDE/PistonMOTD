package net.pistonmaster.pistonmotd.shared.utils;

import net.pistonmaster.pistonmotd.api.PlaceholderUtil;

import java.util.List;
import java.util.Random;

public class MOTDUtil {
    private static final Random random = new Random();

    public static String getMOTDJson(List<String> motds, boolean supportsHex) {
        String unparsedMOTD = motds.get(random.nextInt(motds.size()));
        String[] split = unparsedMOTD.split("%nohexmotd%", 2);
        return PlaceholderUtil.parseTextToJson(
                split.length == 2 ? supportsHex ? split[0] : split[1] : split[0],
                supportsHex);
    }
}
