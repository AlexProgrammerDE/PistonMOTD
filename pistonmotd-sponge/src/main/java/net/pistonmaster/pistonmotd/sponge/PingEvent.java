package net.pistonmaster.pistonmotd.sponge;

import com.google.common.reflect.TypeToken;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.spongeapi.SpongeComponentSerializer;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.commons.io.FilenameUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.server.ClientPingServerEvent;
import org.spongepowered.api.profile.GameProfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("UnstableApiUsage")
public class PingEvent {
    private final PistonMOTDSponge plugin;

    protected PingEvent(PistonMOTDSponge plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void onPing(ClientPingServerEvent event) {
        ConfigurationNode node = plugin.rootNode;

        try {
            if (node.getNode("motd", "activated").getBoolean()) {
                List<String> motd = node.getNode("motd", "text").getList(new TypeToken<String>() {
                });
                event.getResponse().setDescription(SpongeComponentSerializer.get().serialize(LegacyComponentSerializer.legacySection().deserialize(PlaceholderUtil.parseText(motd.get(ThreadLocalRandom.current().nextInt(0, motd.size()))))));
            }

            event.getResponse().setHidePlayers(node.getNode("hideplayers").getBoolean());

            event.getResponse().getPlayers().ifPresent(players -> {
                if (node.getNode("overrideonline", "activated").getBoolean()) {
                    players.setOnline(node.getNode("overrideonline", "value").getInt());
                }

                if (node.getNode("overridemax", "activated").getBoolean()) {
                    players.setMax(node.getNode("overridemax", "value").getInt());
                }

                if (node.getNode("hooks", "luckpermsplayercounter").getBoolean() && plugin.luckpermsWrapper != null) {
                    players.getProfiles().clear();

                    for (Player player : plugin.game.getServer().getOnlinePlayers()) {
                        CachedMetaData metaData = plugin.luckpermsWrapper.luckperms.getPlayerAdapter(Player.class).getMetaData(player);

                        String prefix = metaData.getPrefix() == null ? "" : metaData.getPrefix();

                        String suffix = metaData.getSuffix() == null ? "" : metaData.getSuffix();

                        players.getProfiles().add(GameProfile.of(UUID.randomUUID(), PlaceholderUtil.parseText(prefix + player.getDisplayNameData().displayName().get().toPlain() + suffix)));
                    }
                } else if (node.getNode("playercounter", "activated").getBoolean()) {
                    players.getProfiles().clear();

                    try {
                        for (String str : node.getNode("playercounter", "text").getList(new TypeToken<String>() {
                        })) {
                            players.getProfiles().add(GameProfile.of(UUID.randomUUID(), PlaceholderUtil.parseText(str)));
                        }
                    } catch (ObjectMappingException e) {
                        e.printStackTrace();
                    }
                }
            });

            if (node.getNode("icons").getBoolean()) {
                File[] icons = plugin.icons.listFiles();

                List<File> validFiles = new ArrayList<>();

                if (icons != null && icons.length != 0) {
                    for (File image : icons) {
                        if (FilenameUtils.getExtension(image.getPath()).equals("png")) {
                            validFiles.add(image);
                        }
                    }

                    event.getResponse().setFavicon(Sponge.getGame().getRegistry().loadFavicon(validFiles.get((int) Math.round(Math.random() * (validFiles.size() - 1))).toPath()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
