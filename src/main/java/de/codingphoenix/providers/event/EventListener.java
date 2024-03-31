package de.codingphoenix.providers.event;

public interface EventListener<T extends Event> {
    void handleEvent(T event);
}
