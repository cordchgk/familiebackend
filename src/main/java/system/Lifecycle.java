package system;


import io.quarkus.runtime.Startup;
import org.cord.daos.EMFactoryCreator;
import system.websockets.NotificationSocket;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;


@Singleton
@Startup
public class Lifecycle {


    @PostConstruct
    public void init() {

        EMFactoryCreator.getInstance();
        NotificationSocket.getInstance();

    }


}
