package de.codingphoenix.channelapi.handler;

import de.codingphoenix.channelapi.event.EventHandler;
import de.codingphoenix.channelapi.providers.server.ServerSocketClientHandler;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.channels.SocketChannel;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
public abstract class SocketClientHandler {

    protected final UUID channelIdentifier;
    protected final SocketChannel socketChannel;
    protected final EventHandler eventHandler;
    protected final SocketType socketType;

    protected SocketClientHandler(UUID channelIdentifier, EventHandler eventHandler, ServerSocketClientHandler.SocketType socketType, SocketChannel socketChannel) {
        this.channelIdentifier = channelIdentifier;
        this.socketChannel = socketChannel;
        this.eventHandler = eventHandler;
        this.socketType = socketType;
    }

    public enum SocketType {
        SERVER, CLIENT;
    }
}