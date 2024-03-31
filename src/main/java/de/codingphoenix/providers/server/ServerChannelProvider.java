package de.codingphoenix.providers.server;

import de.codingphoenix.providers.event.channel.ServerClientConnectEvent;
import de.codingphoenix.providers.handler.SocketClientHandler;
import de.codingphoenix.providers.event.EventHandler;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
@Accessors(fluent = true)
public class ServerChannelProvider {
    private final ServerSocketChannel serverSocketChannel;
    private final EventHandler eventHandler;

    private final HashMap<UUID, SocketClientHandler> providers = new HashMap<>();

    public ServerChannelProvider() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        eventHandler = new EventHandler();

        Runtime.getRuntime().addShutdownHook(new Thread(this::disconnect));
    }

    public void connect(int port) throws IOException {
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        executor = Executors.newCachedThreadPool();

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            System.out.println("Client connected: " + socketChannel.getRemoteAddress());

            UUID channelIdentifier = UUID.randomUUID();

            ServerClientConnectEvent event = new ServerClientConnectEvent(socketChannel);
            eventHandler.triggerEvent(event);

            if (event.canceled()) {
                socketChannel.close();
                continue;
            }

            SocketClientHandler clientHandler = new SocketClientHandler(channelIdentifier, eventHandler, socketChannel);
            providers.put(channelIdentifier, clientHandler);
            executor.submit(clientHandler);
        }
    }

    public void disconnect() {
        try {
            for (SocketClientHandler provider : providers.values()) {
                provider.socketChannel().close();
            }

            serverSocketChannel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private ExecutorService executor;
}
