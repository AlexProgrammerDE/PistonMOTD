package net.pistonmaster.pistonmotd.sponge;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.shared.PistonStatusPing;
import net.pistonmaster.pistonmotd.shared.StatusPingListener;
import net.pistonmaster.pistonmotd.shared.utils.MOTDUtil;
import org.apache.commons.io.FilenameUtils;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.server.ClientPingServerEvent;
import org.spongepowered.api.network.status.Favicon;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PingEvent implements StatusPingListener {
    private final PistonMOTDSponge plugin;
    private List<Favicon> favicons;
    private ThreadLocalRandom random;

    protected PingEvent(PistonMOTDSponge plugin) {
        this.plugin = plugin;
        if (plugin.rootNode.node("icons").getBoolean()) {
            favicons = loadFavicons();
            random = ThreadLocalRandom.current();
        }
    }

    @Listener
    public void onPing(ClientPingServerEvent event) {
        handle(wrap(event));
        ConfigurationNode node = plugin.rootNode;

        try {
            if (node.node("motd", "activated").getBoolean()) {
                List<String> motd = node.node("motd", "text").getList(String.class);
                event.response().setDescription(LegacyComponentSerializer.legacySection().deserialize(MOTDUtil.getMOTD(motd, false, PlaceholderUtil::parseText)));
            }

            event.response().setHidePlayers(node.node("hideplayers").getBoolean());

            event.response().players().ifPresent(players -> {
                if (node.node("overrideonline", "activated").getBoolean()) {
                    players.setOnline(node.node("overrideonline", "value").getInt());
                }

                if (node.node("overridemax", "activated").getBoolean()) {
                    players.setMax(node.node("overridemax", "value").getInt());
                }

                if (node.node("hooks", "luckpermsplayercounter").getBoolean() && plugin.luckpermsWrapper != null) {
                    players.profiles().clear();

                    for (Player player : plugin.game.server().onlinePlayers()) {
                        CachedMetaData metaData = plugin.luckpermsWrapper.luckperms.getPlayerAdapter(Player.class).getMetaData(player);

                        String prefix = metaData.getPrefix() == null ? "" : metaData.getPrefix();

                        String suffix = metaData.getSuffix() == null ? "" : metaData.getSuffix();

                        players.profiles().add(GameProfile.of(UUID.randomUUID(), PlaceholderUtil.parseText(prefix + LegacyComponentSerializer.legacySection().serialize(player.displayName().get()) + suffix)));
                    }
                } else if (node.node("playercounter", "activated").getBoolean()) {
                    players.profiles().clear();

                    try {
                        for (String str : node.node("playercounter", "text").getList(String.class)) {
                            players.profiles().add(GameProfile.of(UUID.randomUUID(), PlaceholderUtil.parseText(str)));
                        }
                    } catch (SerializationException e) {
                        e.printStackTrace();
                    }
                }
            });

            if (node.node("icons").getBoolean()) {
                event.response().setFavicon(favicons.get(random.nextInt(0, favicons.size())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Favicon> loadFavicons() {
        File[] icons = plugin.icons.listFiles();

        List<File> validFiles = new ArrayList<>();

        if (icons != null && icons.length != 0) {
            for (File image : icons) {
                if (FilenameUtils.getExtension(image.getPath()).equals("png")) {
                    validFiles.add(image);
                }
            }
        }
        return Arrays.asList(validFiles.stream().map(this::createFavicon).filter(Objects::nonNull).toArray(Favicon[]::new));
    }

    private Favicon createFavicon(File file) {
        try {
            return Favicon.load(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PistonStatusPing wrap(ClientPingServerEvent event) {
        return new PistonStatusPing() {
            @Override
            public void setDescription(String description) {
                event.response().setDescription(LegacyComponentSerializer.legacySection().deserialize(description));
            }

            @Override
            public void setMax(int max) {
                event.response().players().ifPresent(players -> players.setMax(max));
            }

            @Override
            public void setOnline(int online) throws UnsupportedOperationException {
                event.response().players().ifPresent(players -> players.setOnline(online));
            }

            @Override
            public void setVersionName(String name) throws UnsupportedOperationException {
            }

            @Override
            public void setVersionProtocol(int protocol) throws UnsupportedOperationException {
            }

            @Override
            public void setHidePlayers(boolean hidePlayers) throws UnsupportedOperationException {
                event.response().setHidePlayers(hidePlayers);
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public int getMax() {
                return 0;
            }

            @Override
            public int getOnline() throws UnsupportedOperationException {
                return 0;
            }

            @Override
            public String getVersionName() throws UnsupportedOperationException {
                return null;
            }

            @Override
            public int getVersionProtocol() throws UnsupportedOperationException {
                return 0;
            }

            @Override
            public boolean getHidePlayers() throws UnsupportedOperationException {
                return false;
            }
        };// TODO: Implement
    }
}
