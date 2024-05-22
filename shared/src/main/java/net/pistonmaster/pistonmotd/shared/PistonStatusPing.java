package net.pistonmaster.pistonmotd.shared;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;

public interface PistonStatusPing {
    void hidePlayers() throws UnsupportedOperationException;

    String getDescriptionJson();

    void setDescription(String descriptionJson);

    int getMax();

    void setMax(int max);

    int getOnline() throws UnsupportedOperationException;

    void setOnline(int online) throws UnsupportedOperationException;

    String getVersionName() throws UnsupportedOperationException;

    void setVersionName(String name) throws UnsupportedOperationException;

    int getVersionProtocol() throws UnsupportedOperationException;

    void setVersionProtocol(int protocol) throws UnsupportedOperationException;

    void clearSamples() throws UnsupportedOperationException;

    void addSample(UUID uuid, String name) throws UnsupportedOperationException;

    boolean supportsHex();

    void setFavicon(StatusFavicon favicon);

    int getClientProtocol() throws UnsupportedOperationException;

    Optional<InetSocketAddress> getClientVirtualHost() throws UnsupportedOperationException;
}
