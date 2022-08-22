package system;


import org.cord.Entities.Group;
import org.cord.Entities.User;
import org.cord.daos.EMFactoryCreator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;


class GroupUpdateListener extends Thread {


    private final EMFactoryCreator emFactoryCreator;

    private Thread thread;

    private final Connection connection;

    private final org.postgresql.PGConnection pgconn;


    GroupUpdateListener(Connection connection) throws SQLException {

        this.connection = connection;
        this.pgconn = (org.postgresql.PGConnection) connection;
        try(Statement stmt = connection.createStatement()) {
            stmt.execute("LISTEN gid");

        }
        this.emFactoryCreator = EMFactoryCreator.getInstance();
    }


    public void start() {

        if(thread == null) {
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
                        Group group = new Group();
                        group.setId(Integer.parseInt(notification.getParameter()));

                        emFactoryCreator.getEntityManagerFactory()
                                        .getCache()
                                        .evict(
                                                Group.class,
                                                group.getId());

                    }

                }

                // wait a while before checking again for new notifications
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