package net.pistonmaster.pistonmotd.shared;

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
}
