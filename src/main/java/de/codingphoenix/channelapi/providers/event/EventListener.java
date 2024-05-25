package de.codingphoenix.channelapi.providers.event;

public interface EventListener<T extends Event> {
    void handleEvent(T event);
}
