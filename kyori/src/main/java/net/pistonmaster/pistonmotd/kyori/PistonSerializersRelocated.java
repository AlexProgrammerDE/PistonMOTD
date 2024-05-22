package net.pistonmaster.pistonmotd.kyori;

import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class PistonSerializersRelocated {
    public static final LegacyComponentSerializer section = LegacyComponentSerializer.builder().character('§').build();
    public static final LegacyComponentSerializer ampersandRGB = LegacyComponentSerializer.builder().character('&').hexCharacter('#').hexColors().build();
    public static final GsonComponentSerializer gsonSerializer = GsonComponentSerializer.gson();
    public static final GsonComponentSerializer gsonDownSamplingSerializer = GsonComponentSerializer.colorDownsamplingGson();

    private PistonSerializersRelocated() {
    }
}
