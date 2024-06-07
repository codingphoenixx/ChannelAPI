package de.codingphoenix.channelapi.providers.client;

import de.codingphoenix.channelapi.providers.event.EventHandler;
import de.codingphoenix.channelapi.providers.event.channel.ServerConnectEvent;
import de.codingphoenix.channelapi.providers.event.channel.ServerDisconnectClientConnectionEvent;
import de.codingphoenix.channelapi.providers.handler.DisconnectListener;
import de.codingphoenix.channelapi.providers.handler.SocketClientHandler;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.UUID;


@Getter
@Accessors(fluent = true)
public class ClientChannelProvider {
    private boolean connected = false;
    private final SocketChannel socketChannel;
    private final EventHandler eventHandler;
    private final DisconnectListener disconnectListener;
    private SocketClientHandler socketClientHandler;

    public ClientChannelProvider() throws IOException {
        socketChannel = SocketChannel.open();
        eventHandler = new EventHandler();
        disconnectListener = new DisconnectListener(socketChannel);
        Runtime.getRuntime().addShutdownHook(new Thread(this::disconnect));
    }

    public void connect(String hostname, int port) throws IOException {
        socketClientHandler = new SocketClientHandler(new UUID(0, 0), eventHandler, SocketClientHandler.SocketType.CLIENT, socketChannel);
        boolean isConnected = socketChannel.connect(new InetSocketAddress(hostname, port));

        if (!isConnected) {
            throw new IllegalStateException("The connection could not be established.");
        }

        disconnectListener.add((b) -> {
            eventHandler.triggerEvent(new ServerDisconnectClientConnectionEvent(socketChannel, socketClientHandler, b));
            disconnect();
        });

        eventHandler.triggerEvent(new ServerConnectEvent(socketChannel, socketClientHandler, hostname, port));


        connected = true;
        socketClientHandler.run();
    }

    public void disconnect() {
        try {
            connected = false;
            socketChannel.close();
            socketClientHandler.running(false);
            socketClientHandler = null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public SocketClientHandler socketClientHandler() {
        if (socketClientHandler == null) {
            throw new IllegalStateException("The connection was not established or already closed.");
        }
        return socketClientHandler;
    }


}
