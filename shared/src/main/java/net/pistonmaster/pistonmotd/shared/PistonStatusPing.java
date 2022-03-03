package net.pistonmaster.pistonmotd.shared;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.UUID;

public interface PistonStatusPing {
    void setHidePlayers(boolean hidePlayers) throws UnsupportedOperationException;

    String getDescription();

    void setDescription(String description);

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

    InetSocketAddress getClientVirtualHost() throws UnsupportedOperationException;
}
