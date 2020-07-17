package me.alexprogrammerde.BungeePing;

import com.google.common.io.Files;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PingEvent implements Listener {
    Configuration config;
    Main plugin;
    int i = 0;

    public PingEvent(Configuration config, Main plugin) {
        this.config = config;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) throws IOException {
        int online;
        int max;
        ServerPing.Players players;
        BaseComponent motd;
        ServerPing.Protocol protocol;
        String playername = event.getConnection().toString();
        plugin.getLogger().info(playername);
        String aftericon = "                                                                            ";
        Favicon icon = null;

        if (config.getBoolean("overrideonline")) {
            online = config.getInt("online");
        } else {
            online = event.getResponse().getPlayers().getOnline();
        }

        if (config.getBoolean("overridemax")) {
            max = config.getInt("max");
        } else {
            max = event.getResponse().getPlayers().getMax();
        }

        if (config.getBoolean("playercounter.activated")) {
            ServerPing.PlayerInfo[] info = {};
            int i = 0;

            for (String str : config.getStringList("playercounter.text")) {

                info = addInfo(info, new ServerPing.PlayerInfo(str, String.valueOf(i)));
                i++;
            }

            players = new ServerPing.Players(max, online, info);
        } else {
            players = new ServerPing.Players(max, online, event.getResponse().getPlayers().getSample());
        }

        if (config.getBoolean("motd.activated")) {
            List list = config.getList("motd.text");
            motd = new TextComponent(list.get((int) Math.round(Math.random() * (list.size() - 1))).toString());
        } else {
            motd = event.getResponse().getDescriptionComponent();
        }

        if (config.getBoolean("protocol.activated")) {
            ServerPing.Protocol provided = event.getResponse().getVersion();

            Main.plugin.getLogger().info(String.valueOf(provided.getProtocol()));

            provided.setName(config.getString("protocol.text").replaceAll("%aftericon%", aftericon));

            protocol = provided;
        } else {
            protocol = event.getResponse().getVersion();
        }

        if (config.getBoolean("icons.activated")) {
            File[] icons = Main.icons.listFiles();

            List<File> validfiles = new ArrayList<>();

            if (icons != null && icons.length != 0) {
                for (File image : icons) {
                    if (Files.getFileExtension(image.getPath()).equals("png")) {
                        validfiles.add(image);
                    }
                }

                icon = Favicon.create(ImageIO.read(validfiles.get((int) Math.round(Math.random() * (validfiles.size() - 1)))));
            } else {
                icon = event.getResponse().getFaviconObject();
            }
        } else {
            icon = event.getResponse().getFaviconObject();
        }

        ServerPing ping = new ServerPing(protocol, players, motd, icon);
        event.setResponse(ping);

        event.getConnection().getUniqueId();
    }

    public static ServerPing.PlayerInfo[] addInfo(ServerPing.PlayerInfo[] arr, ServerPing.PlayerInfo info) {
        int i;

        ServerPing.PlayerInfo[] newarr = new ServerPing.PlayerInfo[arr.length + 1];

        // insert the elements from
        // the old array into the new array
        // insert all elements till n
        // then insert x at n+1
        for (i = 0; i < arr.length; i++)
            newarr[i] = arr[i];

        newarr[arr.length] = info;

        return newarr;
    }
}
