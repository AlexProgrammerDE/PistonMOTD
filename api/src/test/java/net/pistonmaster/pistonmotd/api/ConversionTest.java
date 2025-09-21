package net.pistonmaster.pistonmotd.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConversionTest {
  @Test
  public void testBasicConversionSection() {
    Assertions.assertEquals("<red>Hello World", PlaceholderUtil.convertMiniMessageString("§cHello World"));
  }

  @Test
  public void testBasicConversionAmpersand() {
    Assertions.assertEquals("<red>Hello World", PlaceholderUtil.convertMiniMessageString("&cHello World"));
  }

  @Test
  public void testBasicConversionHexSection() {
    Assertions.assertEquals("<#FF0000>Hello World", PlaceholderUtil.convertMiniMessageString("§#ff0000Hello World"));
  }

  @Test
  public void testBasicConversionHexAmpersand() {
    Assertions.assertEquals("<#FF0000>Hello World", PlaceholderUtil.convertMiniMessageString("&#ff0000Hello World"));
  }

  @Test
  public void testBasicConversionHexSectionUgly() {
    Assertions.assertEquals("<#FF0000>Hello World", PlaceholderUtil.convertMiniMessageString("§x§f§f§0§0§0§0Hello World"));
  }

  @Test
  public void testBasicConversionHexAmpersandUgly() {
    Assertions.assertEquals("<#FF0000>Hello World", PlaceholderUtil.convertMiniMessageString("&x&f&f&0&0&0&0Hello World"));
  }

  @Test
  public void testNotEscapingTag() {
    Assertions.assertEquals("<#FF0000>Hello World<test>", PlaceholderUtil.convertMiniMessageString("&x&f&f&0&0&0&0Hello World<test>"));
  }

  @Test
  public void testNotEscapingEscape() {
    Assertions.assertEquals("<#FF0000>Hello World\\<test>", PlaceholderUtil.convertMiniMessageString("&x&f&f&0&0&0&0Hello World\\<test>"));
  }
}
