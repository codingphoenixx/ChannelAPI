package de.codingphoenix.channelapi.event.channel;


import de.codingphoenix.channelapi.handler.SocketClientHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.nio.channels.SocketChannel;

@Getter
@Setter
@Accessors(fluent = true)
public class ServerDisconnectClientConnectionEvent extends ChannelEvent {
    private final SocketClientHandler socketClientHandler;
    private final boolean saveDisconnected;

    public ServerDisconnectClientConnectionEvent(SocketChannel socketChannel, SocketClientHandler socketClientHandler, boolean saveDisconnected) {
        super(socketChannel);
        this.socketClientHandler = socketClientHandler;
        this.saveDisconnected = saveDisconnected;
    }
}