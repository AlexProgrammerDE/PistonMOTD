package net.pistonmaster.pistonmotd.shared.utils;

public class EnumSafetyUtil {
    public static <T extends Enum<T>> T getSafeEnum(Class<T> enumClass, String name) {
        try {
            return Enum.valueOf(enumClass, name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
