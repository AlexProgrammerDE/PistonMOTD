package net.pistonmaster.pistonmotd.shared;

import lombok.RequiredArgsConstructor;
import net.lenni0451.mcstructs.text.utils.TextWidthUtils;
import net.pistonmaster.pistonmotd.api.PlaceholderParser;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class CenterPlaceholder implements PlaceholderParser {
    private static final ThreadLocal<boolean[]> CENTERED_LINES = ThreadLocal.withInitial(() -> new boolean[2]);

    private static final int LINE_LENGTH = 45;
    private static final String PLACEHOLDER = "%center%";
    private static final String SPACE = " ";
    private static final char AMPERSAND = '&';
    private static final char BOLD = 'l';
    private static final char RESET = 'r';
    private static final char HEX_CODE = '#';
    private static final String BOLD_SYNTAX = AMPERSAND + String.valueOf(BOLD);
    private static final String RESET_SYNTAX = AMPERSAND + String.valueOf(RESET);
    private static final char[] LEGACY_CODES = {HEX_CODE, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'k', BOLD, 'm', 'n', 'o', RESET};
    private static final char[] COLOR_CODES = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    @Override
    public String parseString(String text) {
        boolean[] centeredLines = CENTERED_LINES.get();
        AtomicReference<String> colorCode = new AtomicReference<>();
        Set<Character> formatModifiers = new HashSet<>();
        String[] lines = text.split("\n", 2);
        for (int i = 0; i < lines.length; i++) {
            // We need to keep track of colors even if the line is not centered
            String parsed = centerText(lines[i], colorCode, formatModifiers);
            if (centeredLines[i]) {
                lines[i] = parsed;
            }
        }

        CENTERED_LINES.remove();
        return String.join("\n", lines);
    }

    private String centerText(String text, AtomicReference<String> colorCode, Set<Character> formatModifiers) {
        float textWidthCounter = 0;
        int textChars = text.length();

        for (int i = 0; i < textChars; i++) {
            char c = text.charAt(i);
            if (c == AMPERSAND && i + 1 < textChars) {
                char next = text.charAt(i + 1);
                if (isValidLegacyChar(next)) {
                    if (next == RESET) {
                        colorCode.set(null);
                        formatModifiers.clear();
                    } else if (next == HEX_CODE) {
                        colorCode.set(text.substring(i + 1, i + 8));
                        i += 6;
                    } else if (isValidColorChar(next)) {
                        colorCode.set(String.valueOf(next));
                    } else {
                        formatModifiers.add(next);
                    }

                    i++;
                    continue;
                }
            }

            textWidthCounter += TextWidthUtils.getCharWidth(c, 1, formatModifiers.contains(BOLD));
        }

        float spaceWidth = TextWidthUtils.getCharWidth(' ', 1, false);
        float spaceBoldWidth = TextWidthUtils.getCharWidth(' ', 1, true);
        float bigAWidth = TextWidthUtils.getCharWidth('A', 1, false);
        int[] leftPaddingSpaces = getLeftPadding(textWidthCounter, spaceWidth, spaceBoldWidth, LINE_LENGTH * bigAWidth);

        StringBuilder builder = new StringBuilder();
        builder.append(RESET_SYNTAX);
        builder.append(SPACE.repeat(Math.max(0, leftPaddingSpaces[0])));

        builder.append(BOLD_SYNTAX);
        builder.append(SPACE.repeat(Math.max(0, leftPaddingSpaces[1])));
        builder.append(RESET_SYNTAX);
        if (colorCode.get() != null) {
            builder.append(AMPERSAND).append(colorCode.get());
        }

        for (char formatModifier : formatModifiers) {
            builder.append(AMPERSAND).append(formatModifier);
        }

        return builder.append(text).toString();
    }

    private boolean isValidLegacyChar(char c) {
        for (char colorCode : LEGACY_CODES) {
            if (c == colorCode) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidColorChar(char c) {
        for (char colorCode : COLOR_CODES) {
            if (c == colorCode) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public static int[] getLeftPadding(float textWidth, float spaceWidth, float spaceBoldWidth, float lineLength) {
        // Calculate the total padding needed on both sides
        float totalPaddingWidth = lineLength - textWidth;

        // If the text is too wide to fit within the line length, return zero spaces
        if (totalPaddingWidth <= 0) {
            return new int[]{0, 0};
        }

        // Calculate the padding needed on one side to center the text
        float leftPaddingWidth = totalPaddingWidth / 2.0f;
        int maxPossibleSpaces = (int) (leftPaddingWidth / spaceWidth);

        return findClosestExponents(spaceWidth, spaceBoldWidth, leftPaddingWidth, maxPossibleSpaces);
    }

    private static int[] findClosestExponents(float x, float y, float z, int range) {
        // Initialize the exponents n and m, and the minimum difference
        int bestN = 0;
        int bestM = 0;
        float minDifference = Float.MAX_VALUE;

        // Iterate through possible values of n and m to find the best combination
        for (int n = 0; n <= range; n++) {
            for (int m = 0; m <= range; m++) {
                // Calculate x * n + y * m
                float value = x * n + y * m;

                // Calculate the difference from z
                float difference = Math.abs(value - z);

                // Update the best values if the current difference is smaller
                if (difference < minDifference) {
                    minDifference = difference;
                    bestN = n;
                    bestM = m;
                }
            }
        }

        return new int[]{bestN, bestM};
    }

    public static class PreProcessorCenterPlaceholder implements PlaceholderParser {
        @Override
        public String parseString(String text) {
            boolean[] centeredLines = CENTERED_LINES.get();

            String[] lines = text.split("\\n|<newline>", 2);
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].startsWith(PLACEHOLDER)) {
                    centeredLines[i] = true;
                    lines[i] = lines[i].substring(PLACEHOLDER.length());
                }
            }

            return String.join("\n", lines);
        }
    }
}
