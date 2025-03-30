package net.pistonmaster.pistonmotd.shared.config;

import lombok.Getter;
import net.skinsrestorer.axiom.AxiomConfiguration;

@Getter
public class PistonMOTDDomainConfig extends PistonMOTDServerConfig {
    private String domain;

    protected void load(AxiomConfiguration config) {
        super.load(config);
        domain = config.getString("domain");
    }
}
