package net.pistonmaster.pistonmotd.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConversionTest {
  @Test
  public void testBasicConversionSection() {
    Assertions.assertEquals("<reset><red>Hello World", MiniMessageConverter.convertMiniMessageString("§cHello World"));
  }

  @Test
  public void testBasicConversionAmpersand() {
    Assertions.assertEquals("<reset><red>Hello World", MiniMessageConverter.convertMiniMessageString("&cHello World"));
  }

  @Test
  public void testBasicConversionHexSection() {
    Assertions.assertEquals("<reset><#FF0000>Hello World", MiniMessageConverter.convertMiniMessageString("§#ff0000Hello World"));
  }

  @Test
  public void testBasicConversionHexAmpersand() {
    Assertions.assertEquals("<reset><#FF0000>Hello World", MiniMessageConverter.convertMiniMessageString("&#ff0000Hello World"));
  }

  @Test
  public void testBasicConversionHexSectionUgly() {
    Assertions.assertEquals("<reset><#FF0000>Hello World", MiniMessageConverter.convertMiniMessageString("§x§f§f§0§0§0§0Hello World"));
  }

  @Test
  public void testBasicConversionHexAmpersandUgly() {
    Assertions.assertEquals("<reset><#FF0000>Hello World", MiniMessageConverter.convertMiniMessageString("&x&f&f&0&0&0&0Hello World"));
  }

  @Test
  public void testNotEscapingTag() {
    Assertions.assertEquals("<reset><#FF0000>Hello World<test>", MiniMessageConverter.convertMiniMessageString("&x&f&f&0&0&0&0Hello World<test>"));
  }

  @Test
  public void testNotEscapingEscape() {
    Assertions.assertEquals("<reset><#FF0000>Hello World\\<test>", MiniMessageConverter.convertMiniMessageString("&x&f&f&0&0&0&0Hello World\\<test>"));
  }

  @Test
  public void testColorThenFormattingKeepsFormattingAfterColor() {
    Assertions.assertEquals("<reset><red>X<underlined>Y", MiniMessageConverter.convertMiniMessageString("§cX§nY"));
  }

  @Test
  public void testFormattingThenColorDisablesFormattingAfterColor() {
    Assertions.assertEquals("<underlined>X<reset><red>Y", MiniMessageConverter.convertMiniMessageString("§nX§cY"));
  }

  @Test
  public void testReuseFormattingWhenChangingColors() {
    Assertions.assertEquals("<reset><red><underlined>X<reset><green><underlined>Y", MiniMessageConverter.convertMiniMessageString("§c§nX§a§nY"));
  }

  @Test
  public void testAnvilHexNonItalicExample() {
    Assertions.assertEquals("<reset><#04280D> NVBIS", MiniMessageConverter.convertMiniMessageString("§#04280D NVBIS"));
  }
}
