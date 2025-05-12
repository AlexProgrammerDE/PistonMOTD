package net.pistonmaster.pistonmotd.shared.config;

import lombok.Getter;
import net.skinsrestorer.axiom.AxiomConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class PistonMOTDPluginConfig extends PistonMOTDServerConfig {
  private boolean updateChecking;
  private boolean advancedPerDomainStatusActivated;
  private Map<String, PistonMOTDDomainConfig> advancedPerDomainStatusDomains;

  @Override
  public void load(AxiomConfigurationSection config) {
    super.load(config);
    updateChecking = config.getBoolean("updateChecking");

    advancedPerDomainStatusActivated = config.getBoolean("advanced.perDomainStatus.activated");
    AxiomConfigurationSection perDomainStatusSection = config.getSection("advanced.perDomainStatus.domains");
    List<String> domains = perDomainStatusSection.getKeys();
    Map<String, PistonMOTDDomainConfig> domainMap = new HashMap<>();
    for (String domainId : domains) {
      AxiomConfigurationSection domainSection = perDomainStatusSection.getSection(domainId);

      PistonMOTDDomainConfig domain = new PistonMOTDDomainConfig();
      domain.load(domainSection);

      domainMap.put(domainId, domain);
    }
    advancedPerDomainStatusDomains = domainMap;
  }
}
