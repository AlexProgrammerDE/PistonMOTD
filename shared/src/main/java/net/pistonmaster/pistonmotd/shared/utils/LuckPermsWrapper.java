package net.pistonmaster.pistonmotd.shared.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class LuckPermsWrapper {
    public final LuckPerms luckperms;

    public LuckPermsWrapper() {
        luckperms = LuckPermsProvider.get();
    }
}
