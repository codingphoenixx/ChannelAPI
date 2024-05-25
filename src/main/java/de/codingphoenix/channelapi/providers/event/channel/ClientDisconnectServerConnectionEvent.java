package de.codingphoenix.channelapi.providers.event.channel;


import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.channels.SocketChannel;

@Getter
@Accessors(fluent = true)
public class ClientDisconnectServerConnectionEvent extends ChannelEvent {
    private final boolean saveDisconnected;

    public ClientDisconnectServerConnectionEvent(SocketChannel socketChannel, boolean saveDisconnected) {
        super(socketChannel);
        this.saveDisconnected = saveDisconnected;
    }
}
