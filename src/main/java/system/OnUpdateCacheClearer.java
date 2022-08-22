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
        Connection lConn = null;
        try {
            lConn = DriverManager.getConnection(url, "postgres", "Blasbara123?!");
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }


        UserUpdateListener listener = new UserUpdateListener(lConn);
        listener.start();
        GroupUpdateListener groupUpdateListener = new GroupUpdateListener(lConn);
        groupUpdateListener.start();
    }

}
