package de.codingphoenix.channelapi.providers.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

@RequiredArgsConstructor
public class DisconnectListener {
    private final SocketChannel socketChannel;



    public void add(DisconnectAction executionOnDisconnect) throws IOException {
        var selector = Selector.open();

        boolean blocking = socketChannel.isBlocking();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

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


                            int bytesRead = 0;
                            try {
                                bytesRead = socketChannel.read(buffer);
                            } catch (IOException e) {
                                executionOnDisconnect.run(false);
                                socketChannel.close();
                                break;
                            }

                            if (bytesRead == -1) {
                                System.out.println("Connection was closed by the other side.");
                                executionOnDisconnect.run(true);
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


    public interface DisconnectAction {
        void run(boolean state);
    }
}
