package de.codingphoenix.channelapi.event.channel;


import de.codingphoenix.channelapi.handler.SocketClientHandler;

import java.nio.channels.SocketChannel;

public class ServerConnectEvent extends ChannelEvent {

    private final SocketClientHandler socketClientHandler;
    private final String hostAddress;
    private final int port;

    public ServerConnectEvent(SocketChannel socketChannel, SocketClientHandler socketClientHandler, String hostAddress, int port) {
        super(socketChannel);
        this.socketClientHandler = socketClientHandler;
        this.hostAddress = hostAddress;
        this.port = port;
    }
}