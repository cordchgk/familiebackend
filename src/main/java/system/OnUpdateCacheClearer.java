package system;


import io.quarkus.runtime.Startup;

import javax.enterprise.context.ApplicationScoped;
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


        Listener listener = new Listener(lConn);
        listener.start();
    }

}
