package net.pistonmaster.pistonmotd.shared;

import lombok.RequiredArgsConstructor;
import net.lenni0451.mcstructs.text.utils.TextWidthUtils;
import net.pistonmaster.pistonmotd.api.PlaceholderParser;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class CenterPlaceholder implements PlaceholderParser {
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
        AtomicReference<String> colorCode = new AtomicReference<>();
        Set<Character> formatModifiers = new HashSet<>();
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = centerText(lines[i], colorCode, formatModifiers);
        }

        return String.join("\n", lines);
    }

    private String centerText(String text, AtomicReference<String> colorCode, Set<Character> formatModifiers) {
        if (!text.startsWith(PLACEHOLDER)) {
            return text;
        }

        text = text.substring(PLACEHOLDER.length());

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
                        colorCode.set(HEX_CODE + text.substring(i + 1, i + 8));
                        i += 7;
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
        for (int i = 0; i < leftPaddingSpaces[0]; i++) {
            builder.append(SPACE);
        }

        builder.append(BOLD_SYNTAX);
        for (int i = 0; i < leftPaddingSpaces[1]; i++) {
            builder.append(SPACE);
        }
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

    public static int[] getLeftPadding(float textWidth, float spaceWidth, float spaceBoldWidth, float lineLength) {
        // Calculate the total padding needed on both sides
        float totalPaddingWidth = lineLength - textWidth;

        // If the text is too wide to fit within the line length, return zero spaces
        if (totalPaddingWidth <= 0) {
            return new int[]{0, 0};
        }

        // Calculate the padding needed on one side to center the text
        float leftPaddingWidth = totalPaddingWidth / 2.0f;

        // Initialize counters for normal and bold spaces
        int normalSpaces = 0;
        int boldSpaces = 0;

        // Calculate the number of normal and bold spaces
        while (leftPaddingWidth > 0) {
            if (leftPaddingWidth >= spaceBoldWidth) {
                boldSpaces++;
                leftPaddingWidth -= spaceBoldWidth;
            } else if (leftPaddingWidth >= spaceWidth) {
                normalSpaces++;
                leftPaddingWidth -= spaceWidth;
            } else {
                break; // When the remaining space is too small for even a normal space
            }
        }

        // Return the result as an array [normalSpaces, boldSpaces]
        return new int[]{normalSpaces, boldSpaces};
    }

    @RequiredArgsConstructor
    private class ParseResult {
        private final String text;
        private final String colorCode;
        private final Set<Character> formatModifiers;
    }
}
