package net.pistonmaster.pistonmotd.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.RequiredArgsConstructor;
import net.pistonmaster.pistonmotd.api.PlaceholderParser;

@RequiredArgsConstructor
public class ServerPlaceholder implements PlaceholderParser {
  private final ProxyServer proxyServer;

  @Override
  public String parseString(String text) {
    for (RegisteredServer server : proxyServer.getAllServers()) {
      text = text.replace("%online_" + server.getServerInfo().getName() + "%", String.valueOf(server.getPlayersConnected().size()));
      text = text.replace("<online_" + server.getServerInfo().getName() + ">", String.valueOf(server.getPlayersConnected().size()));
    }
    return text;
  }
}
