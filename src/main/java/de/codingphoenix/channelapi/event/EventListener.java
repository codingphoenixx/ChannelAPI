package de.codingphoenix.channelapi.event;

public interface EventListener<T extends Event> {
    void handleEvent(T event);
}