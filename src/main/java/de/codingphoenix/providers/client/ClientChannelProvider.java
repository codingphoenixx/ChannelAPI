package de.codingphoenix.providers.client;

import de.codingphoenix.providers.handler.SocketClientHandler;
import de.codingphoenix.providers.event.EventHandler;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.UUID;


@Getter
@Accessors(fluent = true)
public class ClientChannelProvider{
    private final SocketChannel socketChannel;
    private final EventHandler eventHandler;

    private SocketClientHandler socketClientHandler;

    public ClientChannelProvider() throws IOException {
        socketChannel = SocketChannel.open();
        eventHandler = new EventHandler();
        Runtime.getRuntime().addShutdownHook(new Thread(this::disconnect));
    }

    public void connect(String hostname, int port) throws IOException {
        socketChannel.connect(new InetSocketAddress(hostname, port));
        socketClientHandler = new SocketClientHandler(new UUID(0,0), eventHandler, socketChannel);
        socketClientHandler.run();
    }

    public void disconnect() {
        try {
            socketChannel.close();
            socketClientHandler.running(false);
            socketClientHandler = null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public SocketClientHandler socketClientHandler() {
        if (socketClientHandler == null) {
            throw new IllegalStateException("The connection was not established.");
        }
        return socketClientHandler;
    }
}
