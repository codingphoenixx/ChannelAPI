package de.codingphoenix.channelapi.event.channel;

import de.codingphoenix.channelapi.handler.SocketClientHandler;
import lombok.Getter;
import lombok.experimental.Accessors;


@Getter
@Accessors(fluent = true)
public class ChannelReceiveMessageEvent extends ServerChannelHandleEvent {

    public ChannelReceiveMessageEvent(SocketClientHandler socketClientHandler, String message) {
        super(socketClientHandler);
        this.message = message;
    }

    private final String message;
}