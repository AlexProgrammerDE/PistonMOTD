package me.alexprogrammerde.BungeePing;

import me.alexprogrammerde.BungeePing.Listener.PingEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
    public void onEnable() {
        String prefix = ChatColor.AQUA + "[BungeePing] " + ChatColor.WHITE;
        this.getProxy().getPluginManager().registerListener(this, new PingEvent());
        getLogger().info(prefix + "Enabled the plugin. :)");
    }
}
