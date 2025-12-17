package net.pistonmaster.pistonmotd.shared.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import lombok.Getter;

import java.util.Map;

@Getter
@Configuration
public class PistonMOTDPluginConfig extends PistonMOTDServerConfig {
  @Comment({"", "Check for updates on startup. (Only works on Sponge)"})
  private boolean updateChecking = true;

  @Override
  public Advanced getAdvanced() {
    return advanced;
  }

  private PluginAdvanced advanced = new PluginAdvanced();

  @Getter
  @Configuration
  public static class PluginAdvanced extends Advanced {
    @Comment({
      "If activated the server will display a status per server address the request comes from.",
      "This is useful for servers that have multiple addresses and want to route traffic to another server.",
      "The default status is shown when the request doesn't come from the below addresses.",
      "All config options are available for the per domain status, but you have to add them below yourself.",
      "This is because this config would be too big if I added all available options for every example domain."
    })
    private PerDomainStatus perDomainStatus = new PerDomainStatus();

    @Getter
    @Configuration
    public static class PerDomainStatus {
      private boolean activated = false;
      private Map<String, PistonMOTDDomainConfig> domains = Map.of(
        "example", createExampleDomain1(),
        "example2", createExampleDomain2()
      );

      private static PistonMOTDDomainConfig createExampleDomain1() {
        return new PistonMOTDDomainConfig();
      }

      private static PistonMOTDDomainConfig createExampleDomain2() {
        return new PistonMOTDDomainConfig();
      }
    }
  }

  // Convenience accessor methods for plugin-specific settings
  public boolean isAdvancedPerDomainStatusActivated() {
    return advanced.getPerDomainStatus().isActivated();
  }

  public Map<String, PistonMOTDDomainConfig> getAdvancedPerDomainStatusDomains() {
    return advanced.getPerDomainStatus().getDomains();
  }
}
