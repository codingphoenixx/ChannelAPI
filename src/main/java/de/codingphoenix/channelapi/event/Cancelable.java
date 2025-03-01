package de.codingphoenix.channelapi.event;

public interface Cancelable {

    void canceled(boolean canceled);
    boolean canceled();
}