package de.codingphoenix.channelapi.providers.server;

import de.codingphoenix.channelapi.event.channel.*;
import de.codingphoenix.channelapi.handler.SocketClientHandler;
import de.codingphoenix.channelapi.security.Security;
import de.codingphoenix.channelapi.event.EventHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Accessors(fluent = true)
public class ServerSocketClientHandler extends SocketClientHandler implements Runnable {


    private ByteBuffer buffer;
    private boolean running;
    private boolean verifiedSocket;
    private String identifier;

    public ServerSocketClientHandler(UUID channelIdentifier, EventHandler eventHandler, SocketType socketType, SocketChannel socketChannel) {
        super(channelIdentifier, eventHandler, socketType, socketChannel);
        buffer = ByteBuffer.allocate(1024);
        verifiedSocket = false;
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

                if (verifiedSocket) {
                    eventHandler.triggerEvent(new ChannelReceiveMessageEvent(this, messageReceived));
                    continue;
                }

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(messageReceived);
                } catch (JSONException e) {
                    System.out.println("Received message from " + socketChannel.getRemoteAddress() + " in an unverified socket without identification. Data: " + messageReceived);
                    continue;
                }
                if (!jsonObject.has("scope") || !jsonObject.has("data")) {
                    System.out.println("Received message from " + socketChannel.getRemoteAddress() + " in an unverified socket without identification. Data: " + messageReceived);
                    continue;
                }
                JSONObject data = null;
                String identifier = null;
                String key = null;
                try {
                    data = jsonObject.getJSONObject("data");
                    identifier = data.getString("identifier");
                    key = data.getString("key");
                } catch (JSONException e) {
                    System.out.println("Received message from " + socketChannel.getRemoteAddress() + " in an unverified socket without identification. Data: " + messageReceived);
                    continue;
                }
                if (!data.has("identifier") || !data.has("key")) {
                    System.out.println("Received message from " + socketChannel.getRemoteAddress() + " in an unverified socket without identification. Data: " + messageReceived);
                    continue;
                }
                if (!Objects.equals(key, Security.KEY)) {
                    System.out.println("Received message from " + socketChannel.getRemoteAddress() + " in an unverified socket with WRONG identification. Data: " + messageReceived);
                    continue;
                }
                this.identifier = identifier;
                this.verifiedSocket = true;
                System.out.println("Successfully verified " + socketChannel.getRemoteAddress() + "!");
                eventHandler.triggerEvent(new ClientVerificationEvent(socketChannel));
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
}