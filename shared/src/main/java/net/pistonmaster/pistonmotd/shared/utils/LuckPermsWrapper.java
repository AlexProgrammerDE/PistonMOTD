package net.pistonmaster.pistonmotd.shared.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlugin;
import net.pistonmaster.pistonmotd.shared.PlayerWrapper;

@RequiredArgsConstructor
public class LuckPermsWrapper {
    private final PistonMOTDPlugin plugin;
    private final LuckPerms luckperms = LuckPermsProvider.get();

    public LuckPermsMeta getWrappedMeta(PlayerWrapper player) {
        Class<?> playerClass = plugin.getPlatform().getPlayerClass();
        CachedMetaData meta = getMeta(player, playerClass);

        return new LuckPermsMeta() {
            @Override
            public String getPrefix() {
                return meta.getPrefix();
            }

            @Override
            public String getSuffix() {
                return meta.getSuffix();
            }
        };
    }

    @SuppressWarnings("unchecked")
    private <C> CachedMetaData getMeta(PlayerWrapper player, Class<C> playerClass) {
        return luckperms.getPlayerAdapter(playerClass).getMetaData((C) player.getHandle());
    }

    public interface LuckPermsMeta {
        String getPrefix();

        String getSuffix();
    }
}
