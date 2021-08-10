package net.pistonmaster.pistonmotd.utils;

import java.util.List;
import java.util.Random;
import java.util.function.UnaryOperator;

public class MOTDUtil {
    private static final Random random = new Random();

    public static String getMOTD(List<String> motds, boolean supportsHex, UnaryOperator<String> parser) {
        String unParsedMOTD = motds.get(random.nextInt(motds.size()));

        String[] split = unParsedMOTD.contains("%nohexmotd%") ? unParsedMOTD.split("%nohexmotd%") : new String[]{unParsedMOTD};

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

        return parser.apply(returnedString);
    }
}
