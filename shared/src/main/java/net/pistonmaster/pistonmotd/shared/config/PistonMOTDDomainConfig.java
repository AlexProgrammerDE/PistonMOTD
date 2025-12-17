package net.pistonmaster.pistonmotd.shared.config;

import de.exlll.configlib.Configuration;
import lombok.Getter;

@Getter
@Configuration
public class PistonMOTDDomainConfig extends PistonMOTDServerConfig {
  private String domain = "";
}
