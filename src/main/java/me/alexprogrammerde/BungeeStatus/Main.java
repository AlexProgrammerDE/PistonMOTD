package me.alexprogrammerde.BungeeStatus;

import me.alexprogrammerde.BungeeStatus.Listener.PingEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
    public void onEnable() {
        String prefix = ChatColor.AQUA + "[BungeeStatus] " + ChatColor.WHITE;
        this.getProxy().getPluginManager().registerListener(this, new PingEvent());
    }
}
