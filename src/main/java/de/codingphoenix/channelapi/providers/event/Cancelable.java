package de.codingphoenix.channelapi.providers.event;

public interface Cancelable {

    void canceled(boolean canceled);
    boolean canceled();
}
