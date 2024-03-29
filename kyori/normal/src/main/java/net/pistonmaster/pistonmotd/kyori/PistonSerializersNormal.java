package net.pistonmaster.pistonmotd.kyori;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class PistonSerializersNormal {
    public static final LegacyComponentSerializer sectionRGB = LegacyComponentSerializer.builder().character('§').hexCharacter('#').hexColors().build();
    public static final LegacyComponentSerializer unusualSectionRGB = LegacyComponentSerializer.builder().character('§').hexCharacter('#').hexColors().useUnusualXRepeatedCharacterHexFormat().build();
    public static final LegacyComponentSerializer ampersandRGB = LegacyComponentSerializer.builder().character('&').hexCharacter('#').hexColors().build();
    public static final LegacyComponentSerializer section = LegacyComponentSerializer.builder().character('§').build();

    private PistonSerializersNormal() {
    }
}
