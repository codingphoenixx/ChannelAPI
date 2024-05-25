package de.codingphoenix.channelapi.providers.event.channel;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.nio.channels.SocketChannel;

@Getter
@Setter
@Accessors(fluent = true)
public class ServerDisconnectClientConnectionEvent extends ChannelEvent {
    private final boolean saveDisconnected;

    public ServerDisconnectClientConnectionEvent(SocketChannel socketChannel, boolean saveDisconnected) {
        super(socketChannel);
        this.saveDisconnected = saveDisconnected;
    }
}
