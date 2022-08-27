package system;


import org.cord.Entities.MessageEntity;
import org.cord.daos.EMFactoryCreator;
import org.cord.daos.MessageDao;
import system.websockets.NotificationSocket;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;


class MessagesUpdateListener extends Thread {


    NotificationSocket notificationSocket;


    private final EMFactoryCreator emFactoryCreator;

    private Thread thread;

    private final Connection connection;

    private final org.postgresql.PGConnection pgconn;


    MessagesUpdateListener(Connection connection) throws SQLException {

        this.notificationSocket = NotificationSocket.getInstance();
        this.connection = connection;
        this.pgconn = (org.postgresql.PGConnection) connection;
        try(Statement stmt = connection.createStatement()) {
            stmt.execute("LISTEN mid");

        }
        this.emFactoryCreator = EMFactoryCreator.getInstance();
    }


    public void start() {

        if(thread
                == null) {
            thread = new Thread(this);
            thread.start();
        }
    }


    @Override
    public void run() {

        while(true) {

            try {
                // issue a dummy query to contact the backend
                // and receive any pending notifications.
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT 1");
                rs.close();
                stmt.close();

                org.postgresql.PGNotification[] notifications = pgconn.getNotifications();
                if(Objects.nonNull(notifications)) {
                    for(org.postgresql.PGNotification notification : notifications) {
                        MessageEntity message = new MessageEntity();
                        message.setId(Integer.parseInt(notification.getParameter()));

                        MessageDao messageDao = new MessageDao();
                        message = (MessageEntity) messageDao.getById(message,
                                                                     MessageEntity.class,
                                                                     messageDao.getEntityManager());

                        this.notificationSocket.broadcastMessage(message);
                        emFactoryCreator.getEntityManagerFactory()
                                        .getCache()
                                        .evict(
                                                MessageEntity.class,
                                                message.getId());

                    }

                }


                Thread.sleep(500);
            } catch(SQLException sqle) {
                sqle.printStackTrace();
            } catch(InterruptedException ie) {
                Thread.currentThread()
                      .interrupt();
                ie.printStackTrace();
            }
        }
    }


}