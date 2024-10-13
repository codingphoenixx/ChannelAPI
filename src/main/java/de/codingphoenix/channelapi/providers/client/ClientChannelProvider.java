package de.codingphoenix.channelapi.providers.client;

import de.codingphoenix.channelapi.handler.SocketClientHandler;
import de.codingphoenix.channelapi.security.Security;
import de.codingphoenix.channelapi.event.EventHandler;
import de.codingphoenix.channelapi.event.channel.ServerConnectEvent;
import de.codingphoenix.channelapi.event.channel.ServerDisconnectClientConnectionEvent;
import de.codingphoenix.channelapi.handler.DisconnectListener;
import de.codingphoenix.channelapi.providers.server.ServerSocketClientHandler;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.UUID;


@Getter
@Accessors(fluent = true)
public class ClientChannelProvider {
    private final String identifier;
    private boolean connected = false;
    private final SocketChannel socketChannel;
    private final EventHandler eventHandler;
    private final DisconnectListener disconnectListener;
    private ClientSocketClientHandler clientSocketClientHandler;

    public ClientChannelProvider(String identifier) throws IOException {
        this.identifier = identifier;
        socketChannel = SocketChannel.open();
        eventHandler = new EventHandler();
        disconnectListener = new DisconnectListener(socketChannel);
        Runtime.getRuntime().addShutdownHook(new Thread(this::disconnect));
    }

    public void connect(String hostname, int port) throws IOException {
        clientSocketClientHandler = new ClientSocketClientHandler(new UUID(0, 0), eventHandler, ServerSocketClientHandler.SocketType.CLIENT, socketChannel);
        boolean isConnected = socketChannel.connect(new InetSocketAddress(hostname, port));

        if (!isConnected) {
            throw new IllegalStateException("The connection could not be established.");
        }

        disconnectListener.add((b) -> {
            eventHandler.triggerEvent(new ServerDisconnectClientConnectionEvent(socketChannel, clientSocketClientHandler, b));
            disconnect();
        });

        eventHandler.triggerEvent(new ServerConnectEvent(socketChannel, clientSocketClientHandler, hostname, port));


        connected = true;
        clientSocketClientHandler.write(
                new JSONObject()
                        .put("scope", "identification")
                        .put("data", new JSONObject()
                                .put("identifier", identifier)
                                .put("key", Security.KEY)
                        )
        );
        clientSocketClientHandler.run();

    }

    public void disconnect() {
        try {
            connected = false;
            socketChannel.close();
            clientSocketClientHandler.running(false);
            clientSocketClientHandler = null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public ClientSocketClientHandler clientSocketClientHandler() {
        if (clientSocketClientHandler == null) {
            throw new IllegalStateException("The connection was not established or already closed.");
        }
        return clientSocketClientHandler;
    }


}