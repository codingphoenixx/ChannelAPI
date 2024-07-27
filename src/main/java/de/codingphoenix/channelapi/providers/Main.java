package de.codingphoenix.channelapi.providers;

import de.codingphoenix.channelapi.providers.client.ClientChannelProvider;
import de.codingphoenix.channelapi.providers.event.channel.ChannelReceiveMessageEvent;
import de.codingphoenix.channelapi.providers.server.ServerChannelProvider;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private static ServerChannelProvider serverChannelProvider;
    private static ClientChannelProvider clientChannelProvider;


    public static void main(String[] args) throws IOException {
        serverChannelProvider = new ServerChannelProvider();
        clientChannelProvider = new ClientChannelProvider();


        serverChannelProvider.eventHandler().registerEventListener(ChannelReceiveMessageEvent.class, event -> {
            System.out.println("[SERVER] Server received channel message: " + event.message());
            try {
                event.socketClientHandler().write(new JSONObject().put("answer", "ok"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        clientChannelProvider.eventHandler().registerEventListener(ChannelReceiveMessageEvent.class, event -> {
            System.out.println("[CLIENT] Client received channel message: " + event.message());
        });

        new Thread(() -> {


            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        System.out.println("[CLIENT] Sending channel message...");
                        clientChannelProvider.socketClientHandler().write(new JSONObject().put("package", "temp"));
                        System.out.println("[CLIENT] Sent channel message.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 5000);

            try {
                System.out.println("[CLIENT] Connecting to server...");
                clientChannelProvider.connect("localhost", Short.MAX_VALUE);
                System.out.println("[CLIENT] Connected to server.");
            } catch (IOException e) {
                return;
            }
        }).start();

        System.out.println("[SERVER] Server starting...");
        serverChannelProvider.connect(Short.MAX_VALUE);
        System.out.println("[SERVER] Server startet.");
    }
}
