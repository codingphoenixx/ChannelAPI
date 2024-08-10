package de.codingphoenix.channelapi.event.channel;

import de.codingphoenix.channelapi.handler.SocketClientHandler;
import de.codingphoenix.channelapi.providers.server.ServerSocketClientHandler;
import lombok.Getter;
import lombok.experimental.Accessors;


@Getter
@Accessors(fluent = true)
public class ChannelReceiveMessageEvent extends ServerChannelHandleEvent {

    public ChannelReceiveMessageEvent(SocketClientHandler serverSocketClientHandler, String message) {
        super(serverSocketClientHandler);
        this.message = message;
    }

    private final String message;
}