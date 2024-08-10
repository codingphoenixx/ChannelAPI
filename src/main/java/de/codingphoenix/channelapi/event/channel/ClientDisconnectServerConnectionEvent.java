package de.codingphoenix.channelapi.event.channel;


import de.codingphoenix.channelapi.handler.SocketClientHandler;
import de.codingphoenix.channelapi.providers.server.ServerSocketClientHandler;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.channels.SocketChannel;

@Getter
@Accessors(fluent = true)
public class ClientDisconnectServerConnectionEvent extends ChannelEvent {
    private final boolean saveDisconnected;

    private final SocketClientHandler serverSocketClientHandler;

    public ClientDisconnectServerConnectionEvent(SocketChannel socketChannel, SocketClientHandler socketClientHandler, boolean saveDisconnected) {
        super(socketChannel);
        this.serverSocketClientHandler = socketClientHandler;
        this.saveDisconnected = saveDisconnected;
    }
}