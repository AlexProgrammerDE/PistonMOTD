package net.pistonmaster.pistonmotd.shared.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import lombok.Getter;

@Getter
@Configuration
public class PistonMOTDDomainConfig extends PistonMOTDBaseServerConfig {
  @Comment("Matches requests whose host ends with this domain.")
  private String domain = "";

  @Comment({"", "Advanced features and protocol-specific behavior for this domain."})
  private Advanced advanced = new Advanced();
}
