package system;


import io.quarkus.runtime.Startup;

import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Singleton
@Startup
public class OnUpdateCacheClearer {


    public OnUpdateCacheClearer() throws SQLException {

        String url = "jdbc:postgresql://localhost:5432/postgres";
        Connection userListenerConnection = null;
        try {
            userListenerConnection = DriverManager.getConnection(
                    url,
                    "postgres",
                    "Blasbara123?!");
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }

        Connection groupListenerConnection = null;
        try {
            groupListenerConnection = DriverManager.getConnection(
                    url,
                    "postgres",
                    "Blasbara123?!");
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }

        Connection messagesListenerConnection = null;
        try {
            messagesListenerConnection = DriverManager.getConnection(
                    url,
                    "postgres",
                    "Blasbara123?!");
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }

        UserUpdateListener listener = new UserUpdateListener(userListenerConnection);
        listener.start();
        GroupUpdateListener groupUpdateListener = new GroupUpdateListener(groupListenerConnection);
        groupUpdateListener.start();
        MessagesUpdateListener messagesUpdateListener = new MessagesUpdateListener(messagesListenerConnection);
        messagesUpdateListener.start();
    }


}
