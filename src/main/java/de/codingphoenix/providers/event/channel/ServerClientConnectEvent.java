package de.codingphoenix.providers.event.channel;

import de.codingphoenix.providers.event.Cancelable;
import de.codingphoenix.providers.handler.SocketClientHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.nio.channels.SocketChannel;

@Getter
@Setter
@Accessors(fluent = true)
public class ServerClientConnectEvent extends ChannelEvent implements Cancelable {

    public ServerClientConnectEvent(SocketChannel socketChannel) {
        super(socketChannel);
    }

    private boolean cancelled = false;
    @Override
    public boolean canceled() {
        return cancelled;
    }
}
