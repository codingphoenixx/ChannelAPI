package de.codingphoenix.channelapi.providers.handler;

import de.codingphoenix.channelapi.providers.event.EventHandler;
import de.codingphoenix.channelapi.providers.event.channel.ChannelReceiveMessageEvent;
import de.codingphoenix.channelapi.providers.event.channel.ClientDisconnectServerConnectionEvent;
import de.codingphoenix.channelapi.providers.event.channel.ServerDisconnectClientConnectionEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;

@Getter
@Setter
@Accessors(fluent = true)
public class SocketClientHandler implements Runnable {


    private ByteBuffer buffer;
    private final UUID channelIdentifier;
    private final SocketChannel socketChannel;
    private final EventHandler eventHandler;
    private final SocketType socketType;
    private boolean running;


    public SocketClientHandler(UUID channelIdentifier, EventHandler eventHandler, SocketType socketType, SocketChannel socketChannel) {
        this.channelIdentifier = channelIdentifier;
        this.eventHandler = eventHandler;
        this.socketChannel = socketChannel;
        this.socketType = socketType;
        buffer = ByteBuffer.allocate(1024);
    }

    private PrintWriter output;
    private BufferedReader input;

    @Override
    public void run() {
        running = true;


        try {
            while (running) {
                buffer.clear();
                int bytesRead = 0;
                try {
                    bytesRead = socketChannel.read(buffer);
                } catch (IOException e) {
                    if (socketType == SocketType.SERVER) {
                        eventHandler.triggerEvent(new ClientDisconnectServerConnectionEvent(socketChannel, this, false));
                    } else {
                        eventHandler.triggerEvent(new ServerDisconnectClientConnectionEvent(socketChannel, this, false));
                    }
                    socketChannel.close();
                    break;
                }
                if (bytesRead == -1) {
                    if (socketType == SocketType.SERVER) {
                        eventHandler.triggerEvent(new ClientDisconnectServerConnectionEvent(socketChannel, this, true));
                    } else {
                        eventHandler.triggerEvent(new ServerDisconnectClientConnectionEvent(socketChannel, this, true));
                    }
                    socketChannel.close();
                    break;
                }

                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                String messageReceived = new String(bytes).trim();
                if (messageReceived == null || messageReceived.isEmpty())
                    continue;
                eventHandler.triggerEvent(new ChannelReceiveMessageEvent(this, messageReceived));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean write(JSONObject jsonObject) throws IOException {
        return write(ByteBuffer.wrap(jsonObject.toString().getBytes()));
    }

    public boolean write(String msg) throws IOException {
        return write(ByteBuffer.wrap(msg.getBytes()));
    }

    public boolean write(ByteBuffer msg) throws IOException {
        int write = socketChannel.write(msg);
        return write != 0;
    }


    public enum SocketType {
        SERVER, CLIENT;
    }
}