package net.pistonmaster.pistonmotd.shared;

import net.luckperms.api.cacheddata.CachedMetaData;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.shared.utils.MOTDUtil;
import net.skinsrestorer.axiom.AxiomConfiguration;

import java.util.UUID;

public interface StatusPingListener {
    default void handle(PistonStatusPing ping) {
        PistonMOTDPlugin plugin = getPlugin();
        AxiomConfiguration config = plugin.getConfig();

        if (config.getBoolean("motd.activated")) {
            ping.setDescription(MOTDUtil.getMOTD(config.getStringList("motd.text"), ping.supportsHex(), PlaceholderUtil::parseText));
        }

        if (config.getBoolean("extended.hideplayers")) {
            ping.setHidePlayers(true);
        } else {
            if (config.getBoolean("extended.protocol.activated")) {
                ping.setVersionName(PlaceholderUtil.parseText(config.getString("extended.protocol.text")));
            }

            if (config.getBoolean("extended.overrideprotocolnumber.activated")) {
                ping.setVersionProtocol(config.getInt("extended.overrideprotocolnumber.value"));
            }
        }

        if (plugin.luckpermsWrapper.get() != null && config.getBoolean("hooks.extended.luckpermsplayercounter")) {
            ping.clearSamples();

            for (PlayerWrapper player : plugin.getPlayers()) {
                CachedMetaData metaData = plugin.luckpermsWrapper.get().luckperms.getPlayerAdapter(Player.class).getMetaData(player);

                String prefix = metaData.getPrefix() == null ? "" : metaData.getPrefix();

                String suffix = metaData.getSuffix() == null ? "" : metaData.getSuffix();

                ping.addSample(UUID.randomUUID(), prefix + player.getDisplayName() + suffix);
            }
        } else if (config.getBoolean("extended.playercounter.activated")) {
            ping.clearSamples();

            for (String str : config.getStringList("extended.playercounter.text"))
        {
            ping.addSample(UUID.randomUUID(), PlaceholderUtil.parseText(str));
        }
    }
        if (config.getBoolean("overridemax.activated")) {
            ping.setMax(config.getInt("overridemax.value"));
        }

        if (config.getBoolean("extended.overrideonline.activated")) {
            ping.setOnline(config.getInt("extended.overrideonline.value"));
        }

        if (config.getBoolean("icons")) {
            ping.setFavicon(plugin.favicons.get(plugin.random.nextInt(0, plugin.favicons.size())));
        }
    }

    PistonMOTDPlugin getPlugin();
}
