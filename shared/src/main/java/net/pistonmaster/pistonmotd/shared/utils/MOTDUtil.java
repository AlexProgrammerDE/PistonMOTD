package net.pistonmaster.pistonmotd.shared.utils;

import net.pistonmaster.pistonmotd.api.PlaceholderUtil;

import java.util.List;
import java.util.Random;

public class MOTDUtil {
    private static final Random random = new Random();

    public static String getMOTDJson(List<String> motds, boolean supportsHex) {
        String unparsedMOTD = motds.get(random.nextInt(motds.size()));

        String[] split = unparsedMOTD.contains("%nohexmotd%") ? unparsedMOTD.split("%nohexmotd%") : new String[]{unparsedMOTD};

        String returnedString;
        if (split.length > 1) {
            if (supportsHex) {
                returnedString = split[0];
            } else {
                returnedString = split[1];
            }
        } else {
            returnedString = split[0];
        }

        return PlaceholderUtil.parseTextToJson(returnedString, supportsHex);
    }
}
