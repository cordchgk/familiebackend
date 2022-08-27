package system.websockets;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Singleton;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import org.cord.Entities.MessageEntity;
import system.converter.ToJSON;


@ServerEndpoint("/messages/{userId}")
@ApplicationScoped
public class NotificationSocket {


    private static NotificationSocket instance;

    public static Map<String, List<Session>> sessions = new ConcurrentHashMap<>();


    public static NotificationSocket getInstance() {

        if(instance
                == null) {
            instance = new NotificationSocket();
            return instance;
        }
        return instance;
    }


    @OnOpen
    public void onOpen(
            @PathParam("userId") String userId,
            Session session) throws IOException {

        if(userId
                != null
                && !userId.equals("undefined")) {

            sessions.computeIfAbsent(
                    userId,
                    k -> new ArrayList<>());
            if(!sessions.get(userId)
                        .contains(session)) {

                sessions.get(userId)
                        .add(session);
            }

        }

    }


    @OnMessage
    public void onMessage(
            Session session,
            String message,
            @PathParam("userId") String userId) throws IOException {

    }


    public void broadcastMessage(MessageEntity messageEntity) {

        System.out.println(sessions);
        if(sessions.get(String.valueOf(messageEntity.getReceiverId()))
                != null) {

            for(int i = 0; i
                    < sessions.get(String.valueOf(messageEntity.getReceiverId()))
                              .size(); i++) {
                sessions.get(String.valueOf(messageEntity.getReceiverId()))
                        .get(i)
                        .getAsyncRemote()
                        .sendObject(ToJSON.toJSON(messageEntity));

            }
        }

    }


    @OnClose
    public void onClose(
            @PathParam("userId") String userId,
            Session session) throws IOException {

        if(sessions.get(userId)
                != null) {
            sessions.get(userId)
                    .remove(session);
        }

    }


    @OnError
    public void onError(
            Session session,
            Throwable throwable) {

        System.out.println(throwable);

    }


}