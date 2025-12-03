package net.pistonmaster.pistonmotd.shared;

import lombok.RequiredArgsConstructor;
import net.lenni0451.mcstructs.text.utils.TextWidthUtils;
import net.pistonmaster.pistonmotd.api.PlaceholderParser;
import net.pistonmaster.pistonmotd.kyori.PistonSerializersRelocated;
import net.pistonmaster.pistonmotd.shadow.kyori.adventure.text.Component;
import net.pistonmaster.pistonmotd.shadow.kyori.adventure.text.TextComponent;
import net.pistonmaster.pistonmotd.shadow.kyori.adventure.text.format.ShadowColor;
import net.pistonmaster.pistonmotd.shadow.kyori.adventure.text.format.Style;
import net.pistonmaster.pistonmotd.shadow.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CenterPlaceholder {
  private static final ThreadLocal<boolean[]> CENTERED_LINES = ThreadLocal.withInitial(() -> new boolean[2]);
  private static final String PLACEHOLDER = "<center>";

  public static class PreProcessor implements PlaceholderParser {
    @Override
    public String parseString(String text) {
      boolean[] centeredLines = CENTERED_LINES.get();

      String[] lines = text.split("<newline>", 2);
      for (int i = 0; i < lines.length; i++) {
        if (lines[i].startsWith(PLACEHOLDER)) {
          centeredLines[i] = true;
          lines[i] = lines[i].substring(PLACEHOLDER.length());
        }
      }

      return String.join("<newline>", lines);
    }
  }

  public static class PostProcessor implements PlaceholderParser {
    private static final int LINE_LENGTH = 45;
    private static final String SPACE = " ";
    private static final float spaceWidth = TextWidthUtils.getCharWidth(' ', 1, false);
    private static final float spaceBoldWidth = TextWidthUtils.getCharWidth(' ', 1, true);
    private static final float bigAWidth = TextWidthUtils.getCharWidth('A', 1, false);
    private static final Style INVISIBLE_STYLE = Style.style()
      .decorations(Arrays.stream(TextDecoration.values())
        .collect(Collectors.toMap(Function.identity(), ignored -> TextDecoration.State.FALSE)))
      .shadowColor(ShadowColor.none())
      .build();
    private static final Style SPACE_NON_BOLD_STYLE = Style.style()
      .merge(INVISIBLE_STYLE)
      .decoration(TextDecoration.BOLD, TextDecoration.State.FALSE)
      .build();
    private static final Style SPACE_BOLD_STYLE = Style.style()
      .merge(INVISIBLE_STYLE)
      .decoration(TextDecoration.BOLD, TextDecoration.State.TRUE)
      .build();

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

    @Override
    public String parseString(String json) {
      boolean[] centeredLines = CENTERED_LINES.get();
      // Avoid breaking styling and wasting computation if no lines are centered
      if (!centeredLines[0] && !centeredLines[1]) {
        CENTERED_LINES.remove();
        return json;
      }

      Component component = centerText(PistonSerializersRelocated.gsonSerializer.deserialize(json));
      CENTERED_LINES.remove();
      return PistonSerializersRelocated.gsonSerializer.serialize(component);
    }

    private void propagateWidths(Component component, boolean isParentBold, AtomicBoolean isFirstLine, float[] widths) {
      TextDecoration.State boldState = component.style().decoration(TextDecoration.BOLD);
      boolean isCurrentBold = boldState == TextDecoration.State.NOT_SET ? isParentBold : boldState == TextDecoration.State.TRUE;

      String stringContent = component instanceof TextComponent textComponent ? textComponent.content() : "A";
      for (char c : stringContent.toCharArray()) {
        if (c == '\n' && isFirstLine.get()) {
          isFirstLine.set(false);
          continue;
        }

        widths[isFirstLine.get() ? 0 : 1] += TextWidthUtils.getCharWidth(c, 1, isCurrentBold);
      }

      for (Component child : component.children()) {
        propagateWidths(child, isCurrentBold, isFirstLine, widths);
      }
    }

    private Component centerText(Component component) {
      float[] widths = new float[2];
      propagateWidths(component, false, new AtomicBoolean(true), widths);

      for (int i = 0; i < widths.length; i++) {
        int[] leftPaddingSpaces = getLeftPadding(widths[i], spaceWidth, spaceBoldWidth, LINE_LENGTH * bigAWidth);
        Component normalPadding = Component.text(SPACE.repeat(leftPaddingSpaces[0]))
          .style(SPACE_NON_BOLD_STYLE);
        Component boldPadding = Component.text(SPACE.repeat(leftPaddingSpaces[1]))
          .style(SPACE_BOLD_STYLE);
        if (i == 0) {
          component = normalPadding
            .append(boldPadding)
            .append(component);
        } else {
          component = component.replaceText(builder ->
            builder
              .matchLiteral("\n")
              .replacement(Component.newline()
                .append(normalPadding)
                .append(boldPadding)
              )
          );
        }
      }

      return component;
    }
  }
}
