package net.pistonmaster.pistonmotd.shared;

import net.pistonmaster.pistonmotd.shared.utils.PMUnsupportedConfigException;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;

public interface PistonStatusPing {
    void hidePlayers() throws PMUnsupportedConfigException;

    String getDescriptionJson();

    void setDescription(String descriptionJson);

    int getMax();

    void setMax(int max);

    int getOnline() throws PMUnsupportedConfigException;

    void setOnline(int online) throws PMUnsupportedConfigException;

    String getVersionName() throws PMUnsupportedConfigException;

    void setVersionName(String name) throws PMUnsupportedConfigException;

    int getVersionProtocol() throws PMUnsupportedConfigException;

    void setVersionProtocol(int protocol) throws PMUnsupportedConfigException;

    void clearSamples() throws PMUnsupportedConfigException;

    void addSample(UUID uuid, String name) throws PMUnsupportedConfigException;

    boolean supportsHex();

    void setFavicon(StatusFavicon favicon);

    int getClientProtocol() throws PMUnsupportedConfigException;

    Optional<InetSocketAddress> getClientVirtualHost() throws PMUnsupportedConfigException;
}
