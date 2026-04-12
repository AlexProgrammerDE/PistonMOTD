package net.pistonmaster.pistonmotd.shared.config;

import de.exlll.configlib.Configuration;
import lombok.Getter;

@Getter
@Configuration
public class PistonMOTDDomainConfig extends PistonMOTDBaseServerConfig {
  private String domain = "";

  private Advanced advanced = new Advanced();
}
