package net.pistonmaster.pistonmotd.shared.config;

import lombok.Getter;
import net.skinsrestorer.axiom.AxiomConfigurationSection;

@Getter
public class PistonMOTDDomainConfig extends PistonMOTDServerConfig {
  private String domain;

  @Override
  protected void load(AxiomConfigurationSection config) {
    super.load(config);
    domain = config.getString("domain");
  }
}
