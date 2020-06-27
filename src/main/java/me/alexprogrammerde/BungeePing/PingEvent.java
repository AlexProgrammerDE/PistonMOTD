package me.alexprogrammerde.BungeePing;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.awt.image.BufferedImage;
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
    public void onPing(ProxyPingEvent event) {
        int online;
        int max;
        ServerPing.Players players;
        BaseComponent motd;
        ServerPing.Protocol protocol;
        String playername = event.getConnection().toString();
        plugin.getLogger().info(playername);
        String aftericon = "                                                                            ";

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

                info = addInfo(info, new ServerPing.PlayerInfo(str.replaceAll("%playername%", playername), String.valueOf(i)));
                i++;
            }

            players = new ServerPing.Players(max, online, info);
        } else {
            players = new ServerPing.Players(max, online, event.getResponse().getPlayers().getSample());
        }

        if (config.getBoolean("motd.activated")) {
            List list = config.getList("motd.text");
            motd = new TextComponent(list.get((int) Math.round(Math.random() * (list.size() - 1))).toString().replaceAll("%playername%", playername));
        } else {
            motd = event.getResponse().getDescriptionComponent();
        }

        if (config.getBoolean("protocol.activated")) {
            protocol = new ServerPing.Protocol(config.getString("protocol.text").replaceAll("%aftericon%", aftericon), config.getInt("protocol.versionnumber"));
        } else {
            protocol = event.getResponse().getVersion();
        }

        ServerPing ping = new ServerPing(protocol, players, motd, event.getResponse().getFaviconObject());
        event.setResponse(ping);

        event.getConnection().getUniqueId();
    }

    public static ServerPing.PlayerInfo[] addInfo(ServerPing.PlayerInfo[] arr, ServerPing.PlayerInfo info)
    {
        int i;

        // create a new array of size n+1
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
