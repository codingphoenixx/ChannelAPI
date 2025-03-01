package de.codingphoenix.channelapi.event.channel;

import de.codingphoenix.channelapi.event.Cancelable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.nio.channels.SocketChannel;

@Getter
@Setter
@Accessors(fluent = true)
public class ClientVerificationEvent extends ChannelEvent {

    public ClientVerificationEvent(SocketChannel socketChannel) {
        super(socketChannel);
    }
}