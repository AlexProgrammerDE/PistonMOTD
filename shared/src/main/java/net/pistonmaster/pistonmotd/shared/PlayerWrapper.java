package net.pistonmaster.pistonmotd.shared;

import java.util.UUID;

public interface PlayerWrapper {
  String getDisplayName();

  String getName();

  UUID getUniqueId();

  Object getHandle();
}
