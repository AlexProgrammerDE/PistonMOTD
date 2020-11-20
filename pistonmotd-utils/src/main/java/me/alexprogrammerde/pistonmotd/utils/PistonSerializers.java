package me.alexprogrammerde.pistonmotd.utils;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class PistonSerializers {
    public static final LegacyComponentSerializer sectionRGB = LegacyComponentSerializer.builder().character('§').hexCharacter('#').hexColors().build();
    public static final LegacyComponentSerializer unusualSectionRGB = LegacyComponentSerializer.builder().character('§').hexCharacter('#').hexColors().useUnusualXRepeatedCharacterHexFormat().build();
    public static final LegacyComponentSerializer ampersandRGB = LegacyComponentSerializer.builder().character('&').hexCharacter('#').hexColors().build();

    public static final LegacyComponentSerializer section = LegacyComponentSerializer.builder().character('§').build();
}
