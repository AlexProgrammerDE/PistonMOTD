package net.pistonmaster.pistonmotd.shared;

import lombok.Getter;
import net.skinsrestorer.axiom.AxiomConfiguration;

@Getter
public class PistonMOTDPluginConfig extends PistonMOTDServerConfig {
    private boolean updateChecking;

    protected void load(AxiomConfiguration config) {
        super.load(config);
        updateChecking = config.getBoolean("updateChecking");
    }
}
