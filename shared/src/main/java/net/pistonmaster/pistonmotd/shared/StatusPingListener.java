package net.pistonmaster.pistonmotd.shared;

public interface StatusPingListener {
    default void handle(PistonStatusPing ping) {
        // NO-OP
    }
}
