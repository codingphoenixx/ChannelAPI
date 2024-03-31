package de.codingphoenix.providers.event.channel;

import de.codingphoenix.providers.handler.SocketClientHandler;
import lombok.Getter;
import lombok.experimental.Accessors;


@Getter
@Accessors(fluent = true)
public class ServerChannelHandleEvent extends ChannelEvent{
    private final SocketClientHandler socketClientHandler;

    public ServerChannelHandleEvent(SocketClientHandler socketClientHandler) {
        super(socketClientHandler.socketChannel());
        this.socketClientHandler = socketClientHandler;
    }
}
