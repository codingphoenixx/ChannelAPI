package de.codingphoenix.channelapi.providers.event.channel;

import de.codingphoenix.channelapi.providers.event.Cancelable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.nio.channels.SocketChannel;

@Getter
@Setter
@Accessors(fluent = true)
public class ClientConnectToServerEvent extends ChannelEvent implements Cancelable {

    public ClientConnectToServerEvent(SocketChannel socketChannel) {
        super(socketChannel);
    }

    private boolean cancelled = false;
    @Override
    public boolean canceled() {
        return cancelled;
    }
}
