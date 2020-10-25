package me.alexprogrammerde.PistonMOTD.sponge;

import com.google.common.reflect.TypeToken;
import me.alexprogrammerde.PistonMOTD.api.PlaceholderUtil;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.server.ClientPingServerEvent;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("UnstableApiUsage")
public class PingEvent {
    PistonMOTDSponge plugin;

    public PingEvent(PistonMOTDSponge plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void onPing(ClientPingServerEvent event) {
        ConfigurationNode node = plugin.rootNode;

        try {
            if (node.getNode("motd", "activated").getBoolean()) {
                List<String> motd = node.getNode("motd", "text").getList(new TypeToken<String>() {});
                event.getResponse().setDescription(Text.of(PlaceholderUtil.parseText(motd.get(ThreadLocalRandom.current().nextInt(0,  motd.size())))));
            }

            event.getResponse().setHidePlayers(node.getNode("hideplayers").getBoolean());

            if (event.getResponse().getPlayers().isPresent()) {
                if (node.getNode("overrideonline", "activated").getBoolean()) {
                    event.getResponse().getPlayers().get().setOnline(node.getNode("overrideonline", "value").getInt());
                }

                if (node.getNode("overridemax", "activated").getBoolean()) {
                    event.getResponse().getPlayers().get().setMax(node.getNode("overridemax", "value").getInt());
                }

                if (node.getNode("playercounter", "activated").getBoolean()) {
                    event.getResponse().getPlayers().get().getProfiles().clear();

                    for (String str : node.getNode("playercounter", "text").getList(new TypeToken<String>() {})) {
                        event.getResponse().getPlayers().get().getProfiles().add(GameProfile.of(UUID.randomUUID(), PlaceholderUtil.parseText(str)));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
