package de.codingphoenix.channelapi.event.channel;


import de.codingphoenix.channelapi.handler.SocketClientHandler;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.channels.SocketChannel;

@Getter
@Accessors(fluent = true)
public class ClientDisconnectServerConnectionEvent extends ChannelEvent {
    private final boolean saveDisconnected;

    private final SocketClientHandler socketClientHandler;

    public ClientDisconnectServerConnectionEvent(SocketChannel socketChannel, SocketClientHandler socketClientHandler, boolean saveDisconnected) {
        super(socketChannel);
        this.socketClientHandler = socketClientHandler;
        this.saveDisconnected = saveDisconnected;
    }
}