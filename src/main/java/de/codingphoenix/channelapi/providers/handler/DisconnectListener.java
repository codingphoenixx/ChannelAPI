package de.codingphoenix.channelapi.providers.handler;

import de.codingphoenix.channelapi.providers.event.EventHandler;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@RequiredArgsConstructor
public class DisconnectListener {
    private final SocketChannel socketChannel;
    private final EventHandler eventHandler;



    public void add(Runnable executionOnDisconnect) throws IOException {
        var selector = Selector.open();

        boolean blocking = socketChannel.isBlocking();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        socketChannel.configureBlocking(blocking);

        new Thread(() -> {

            ByteBuffer buffer = ByteBuffer.allocate(256);

            while (true) {
                try {
                    selector.select();

                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectedKeys.iterator();

                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();

                        if (key.isReadable()) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            buffer.clear();
                            int bytesRead = channel.read(buffer);

                            if (bytesRead == -1) {
                                System.out.println("Connection was closed by the other side.");
                                executionOnDisconnect.run();
                                return;
                            }

                            buffer.flip();
                            while (buffer.hasRemaining()) {
                                System.out.print("DEBUG: " + (char) buffer.get());
                            }
                        }

                        iterator.remove();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();
    }
}
