package net.pistonmaster.pistonmotd.shared;

public interface PistonStatusPing {
    void setDescription(String description);

    void setMax(int max);

    void setOnline(int online) throws UnsupportedOperationException;

    void setVersionName(String name) throws UnsupportedOperationException;

    void setVersionProtocol(int protocol) throws UnsupportedOperationException;

    void setHidePlayers(boolean hidePlayers) throws UnsupportedOperationException;

    String getDescription();

    int getMax();

    int getOnline() throws UnsupportedOperationException;

    String getVersionName() throws UnsupportedOperationException;

    int getVersionProtocol() throws UnsupportedOperationException;

    boolean getHidePlayers() throws UnsupportedOperationException;
}
