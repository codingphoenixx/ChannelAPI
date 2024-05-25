package de.codingphoenix.channelapi.providers.event.channel;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.nio.channels.SocketChannel;

@Getter
@Setter
@Accessors(fluent = true)
public class ServerDisconnectClientConnectionEvent extends ChannelEvent {
    public ServerDisconnectClientConnectionEvent(SocketChannel socketChannel) {
        super(socketChannel);
    }
}
