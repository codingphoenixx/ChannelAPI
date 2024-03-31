package de.codingphoenix.providers.event.channel;

import de.codingphoenix.providers.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.nio.channels.SocketChannel;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public class ChannelEvent extends Event {
    private final SocketChannel socketChannel;
}
